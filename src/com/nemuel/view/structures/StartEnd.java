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

public class StartEnd extends Structure {

    private StackPane startEnd;
    private Rectangle rectangle;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeline;

    public StartEnd(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.startEnd = new StackPane();
        //this.start.setStyle("-fx-background-color: red;");
        this.startEnd.relocate(x, y);
        this.startEnd.setAlignment(Pos.CENTER);
        this.startEnd.setMinSize(40, 12.5);
        this.startEnd.setPrefSize(w, h);
        this.startEnd.setMaxSize(250, 100);
        this.startEnd.setCursor(Cursor.HAND);

        this.rectangle = new Rectangle(w, h);
        this.rectangle.setArcWidth(20);
        this.rectangle.setArcHeight(20);
        this.rectangle.setFill(Color.rgb(19, 194, 163));
        this.rectangle.setStroke(Color.BLACK);
        this.rectangle.setStrokeWidth(2);
        if (toAnimate) {
            this.rectangle.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.startEnd.getChildren().add(this.rectangle);

        this.startEnd.getChildren().add(this.instruction);

        this.paneContainer.getChildren().addAll(this.startEnd);

        this.rectangle.widthProperty().bind(this.startEnd.widthProperty());
        this.rectangle.heightProperty().bind(this.startEnd.heightProperty());

        if (toAnimate) {
            this.resize();
            //this.writeInstruction();
            this.animateBorder(toAnimate);
            this.drag();
        }

    }

    @Override
    public void writeInstruction() {
        this.startEnd.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());

                ta.setWrapText(true);
                ta.setPrefWidth(this.startEnd.getPrefWidth());
                ta.setPrefHeight(this.startEnd.getPrefHeight());
                ta.setPadding(Insets.EMPTY);

                ta.setStyle("""
                            -fx-background-color: transparent;
                            -fx-control-inner-background: transparent;
                            -fx-background-insets: 0;
                            -fx-border-color: transparent;
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

                this.startEnd.getChildren().remove(this.instruction);
                this.startEnd.getChildren().add(ta);

                // Confirmar con Ctrl + Enter
                ta.setOnKeyPressed(ev -> {
                    if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
                        finishEdit(ta);
                    }
                });

                // Confirmar al perder foco
                ta.focusedProperty().addListener((obs, oldV, newV) -> {
                    if (!newV) {
                        finishEdit(ta);
                    }
                });

                this.paneContainer.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            finishEdit(ta);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void resize() {
        final double TAM_HANDLE = 10;
        final double MIN_w = 80;
        final double MIN_H = 25;
        final double MAX_W = 250;
        final double MAX_H = 100;

        Rectangle handle = new Rectangle(TAM_HANDLE, TAM_HANDLE);
        handle.setFill(Color.TRANSPARENT);
        handle.setCursor(Cursor.SE_RESIZE);

        StackPane.setAlignment(handle, Pos.BOTTOM_RIGHT);

        this.startEnd.getChildren().add(handle);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.startEnd.getPrefWidth() + dx;
            double newH = this.startEnd.getPrefHeight() + dy;

            newW = Math.max(MIN_w, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.startEnd.setPrefSize(newW, newH);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        if (toAnimate) {
            this.maxDisplace = this.rectangle
                    .getStrokeDashArray()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .reduce(0d, (a, b) -> a + b);

            this.timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(this.rectangle.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(this.rectangle.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
            );
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.setRate(-1.5);
            this.timeline.play();
        }
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.startEnd.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.startEnd.toFront();
                this.startEnd.setCursor(Cursor.HAND);
            }
        });

        this.startEnd.setOnMouseDragged(e -> {
            this.startEnd.relocate(
                    this.startEnd.getLayoutX() + e.getX() - dragDelta.x,
                    this.startEnd.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.startEnd.getChildren().remove(ta);

        if (!this.startEnd.getChildren().contains(this.instruction)) {
            this.startEnd.getChildren().add(this.instruction);
        }
    }

    public StackPane getStartEnd() {
        return this.startEnd;
    }
}
