package com.nemuel.view.structures;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Process extends Structure {

    private StackPane process;
    private Rectangle rectangle;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeLine;

    public Process(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.process = new StackPane();
        this.process.relocate(x, y);
        //this.process.setStyle("-fx-background-color: red;");
        this.process.setAlignment(Pos.CENTER);
        this.process.setMinSize(40, 12.5);
        this.process.setPrefSize(w, h);
        this.process.setMaxSize(250, 100);
        this.process.setCursor(Cursor.HAND);

        this.rectangle = new Rectangle();
        this.rectangle.setFill(Color.rgb(240, 10, 102));
        this.rectangle.setStroke(Color.BLACK);
        this.rectangle.setStrokeWidth(2);
        if (toAnimate) {
            this.rectangle.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }
        this.rectangle.widthProperty().bind(this.process.widthProperty());
        this.rectangle.heightProperty().bind(this.process.heightProperty());

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.process.getChildren().add(this.rectangle);

        this.process.getChildren().add(this.instruction);

        this.paneContainer.getChildren().add(this.process);

        if (toAnimate) {
            this.writeInstruction();
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }
    }

    @Override
    public void writeInstruction() {
        this.process.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.process.getPrefWidth());
                ta.setPrefHeight(this.process.getPrefHeight());
                ta.setPadding(Insets.EMPTY);
                ta.setStyle("""
                            -fx-background-color: transparent;
                            -fx-control-inner-background: transparent;
                            -fx-background-insets: 0;
                            -fx-border-color:transparent;
                            -fx-text-fill: black;
                            -fx-highlight-fill: rgba(0,0,0,0.2);
                            -fx-highlight-text-fill: black;
                            -fx-font-family: 'Consolas';
                            -fx-font-size: 11;
                            """);
                ta.setFocusTraversable(true);

                Platform.runLater(() -> {
                    ta.requestFocus();
                    ta.positionCaret(ta.getText().length());
                });

                this.process.getChildren().remove(this.instruction);
                this.process.getChildren().add(ta);

                ta.setOnKeyPressed(ev -> {
                    if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
                        this.finishEdit(ta);
                    }
                });

                ta.focusedProperty().addListener((obs, oldV, newV) -> {
                    if (!newV) {
                        this.finishEdit(ta);
                    }
                });

                this.paneContainer.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            this.finishEdit(ta);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void resize() {
        final double TAM_HANDLE = 10;
        final double MIN_W = 80;
        final double MIN_H = 25;
        final double MAX_W = 250;
        final double MAX_H = 100;

        Rectangle handle = new Rectangle(TAM_HANDLE, TAM_HANDLE);
        handle.setFill(Color.TRANSPARENT);
        handle.setCursor(Cursor.SE_RESIZE);

        StackPane.setAlignment(handle, Pos.BOTTOM_RIGHT);

        this.process.getChildren().add(handle);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.process.getPrefWidth() + dx;
            double newH = this.process.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.process.setPrefSize(newW, newH);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.rectangle
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.rectangle.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.rectangle.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
        );
        this.timeLine.setCycleCount(Timeline.INDEFINITE);
        this.timeLine.setRate(-1.5);
        this.timeLine.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.process.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.process.toFront();
                this.process.setCursor(Cursor.HAND);
            }
        });

        this.process.setOnMouseDragged(e -> {
            this.process.relocate(
                    this.process.getLayoutX() + e.getX() - dragDelta.x,
                    this.process.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.process.getChildren().remove(ta);

        if (!this.process.getChildren().contains(this.instruction)) {
            this.process.getChildren().add(this.instruction);
        }
    }

}
