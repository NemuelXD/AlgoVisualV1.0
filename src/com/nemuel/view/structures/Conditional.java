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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Conditional extends Structure {

    private StackPane condition;
    private Polygon polygon;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeLine;

    public Conditional(AnchorPane aPane) {
        this.aPane = aPane;
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.condition = new StackPane();
        this.condition.relocate(100, 200);
        //this.condition.setStyle("-fx-background-color: red;");
        this.condition.setAlignment(Pos.CENTER);
        this.condition.setMinSize(80, 25);
        this.condition.setPrefSize(140, 50);
        this.condition.setMaxSize(250, 100);

        this.polygon = new Polygon();
        this.polygon.setFill(Color.rgb(20, 103, 184));
        this.polygon.setStroke(Color.BLACK);
        this.polygon.setStrokeWidth(2);
        this.polygon.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);

        this.condition.widthProperty().addListener((obs, oldW, newW) -> updatePolygon(newW.doubleValue(), this.condition.getHeight()));
        this.condition.heightProperty().addListener((obs, oldH, newH) -> updatePolygon(this.condition.getWidth(), newH.doubleValue()));

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.condition.getChildren().add(this.polygon);

        this.condition.getChildren().add(this.instruction);

        this.aPane.getChildren().add(this.condition);

        this.writeInstruction();
        this.resize();
        this.animateBorder(toAnimate);
        this.drag();
    }

    @Override
    public void writeInstruction() {
        this.condition.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                this.editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.condition.getPrefWidth());
                ta.setPrefHeight(this.condition.getPrefHeight());
                ta.setPadding(new Insets(10, this.condition.getWidth() / 4, 10, this.condition.getWidth() / 4));
                ta.setOpacity(0.75);
                ta.setBackground(Background.EMPTY);
                ta.setStyle("""
                            -fx-background-color: transparent;
                            -fx-control-inner-background: transparent;
                            -fx-background-insets: 0;
                            -fx-background-radius: 0;
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

                ta.setOnKeyPressed(ev -> {
                    if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
                        this.finishEdit(ta);
                    }
                });

                ta.focusedProperty().addListener((obs, oldV, newV) -> {
                    if (!newV) {
                        finishEdit(ta);
                    }
                });

                this.aPane.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            finishEdit(ta);
                        }
                    }
                });

                this.condition.getChildren().remove(this.instruction);
                this.condition.getChildren().add(ta);
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

        handle.setTranslateX((this.condition.getPrefWidth() - TAM_HANDLE) / 2);
        handle.setTranslateY((this.condition.getPrefHeight() - TAM_HANDLE) / 40);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.condition.getPrefWidth() + dx;
            double newH = this.condition.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.condition.setPrefSize(newW, newH);

            handle.setTranslateX((newW - TAM_HANDLE) / 2);
            handle.setTranslateY((newH - TAM_HANDLE) / 40);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.condition.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.polygon
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.polygon.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.polygon.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
        );
        this.timeLine.setCycleCount(Timeline.INDEFINITE);
        this.timeLine.setRate(1.5);
        this.timeLine.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.condition.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.condition.toFront();
                this.condition.setCursor(Cursor.HAND);
            }
        });

        this.condition.setOnMouseDragged(e -> {
            this.condition.relocate(
                    this.condition.getLayoutX() + e.getX() - dragDelta.x,
                    this.condition.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePolygon(double width, double height) {
        this.polygon.getPoints().setAll(
                width / 2.0, 0.0,
                width, height / 2.0,
                width / 2.0, height,
                0.0, height / 2.0
        );
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.condition.getChildren().remove(ta);

        if (!this.condition.getChildren().contains(this.instruction)) {
            this.condition.getChildren().add(this.instruction);
        }
    }

}
