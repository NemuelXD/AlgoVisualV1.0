package com.nemuel.view.structures;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class EndOfProcedure extends Structure {

    private StackPane endOfProcedure;
    private Circle circle;

    private double maxDisplace;
    private Timeline timeLine;

    public EndOfProcedure(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.endOfProcedure = new StackPane();
        this.endOfProcedure.relocate(x, y);
        //this.endOfProcedure.setStyle("-fx-background-color: red;");
        this.endOfProcedure.setAlignment(Pos.CENTER);
        this.endOfProcedure.setMinSize(25, 25);
        this.endOfProcedure.setPrefSize(w, h);
        this.endOfProcedure.setMaxSize(200, 200);

        this.circle = new Circle();
        this.circle.setFill(Color.rgb(121, 113, 107));
        this.circle.setStroke(Color.BLACK);
        this.circle.setStrokeWidth(2);
        if (toAnimate) {
            this.circle.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }
        this.endOfProcedure.widthProperty().addListener((obs, oldVal, newVal) -> updatePath(this.endOfProcedure.getPrefWidth(), this.endOfProcedure.getPrefHeight()));
        this.endOfProcedure.heightProperty().addListener((obs, oldVal, newVal) -> updatePath(this.endOfProcedure.getPrefWidth(), this.endOfProcedure.getPrefHeight()));

        this.endOfProcedure.getChildren().add(this.circle);

        this.paneContainer.getChildren().add(this.endOfProcedure);

        if (toAnimate) {
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }
    }

    @Override
    public void writeInstruction() {

    }

    @Override
    public void resize() {
        final double TAM_HANDLE = 10;
        final double MIN_W = 25;
        final double MIN_H = 25;
        final double MAX_W = 200;
        final double MAX_H = 200;

        Rectangle handle = new Rectangle(TAM_HANDLE, TAM_HANDLE);
        handle.setFill(Color.TRANSPARENT);
        handle.setCursor(Cursor.SE_RESIZE);
        handle.setTranslateX((this.endOfProcedure.getPrefWidth() - TAM_HANDLE) / 4);
        handle.setTranslateY((this.endOfProcedure.getPrefHeight() - TAM_HANDLE) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;

            double newSize = this.endOfProcedure.getPrefWidth() + dx;

            newSize = Math.max(MIN_W, Math.min(MAX_W, newSize));

            this.endOfProcedure.setPrefSize(newSize, newSize);

            handle.setTranslateX((newSize - TAM_HANDLE) / 4);
            handle.setTranslateY((newSize - TAM_HANDLE) / 2);

            resizeDelta.x = e.getSceneX();

            e.consume();
        });

        this.endOfProcedure.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.circle
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.circle.strokeDashOffsetProperty(), this.maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.circle.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
        );
        this.timeLine.setCycleCount(Timeline.INDEFINITE);
        this.timeLine.setRate(1.5);
        this.timeLine.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.endOfProcedure.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.endOfProcedure.toFront();
                this.endOfProcedure.setCursor(Cursor.HAND);
            }
        });

        this.endOfProcedure.setOnMouseDragged(e -> {
            this.endOfProcedure.relocate(
                    this.endOfProcedure.getLayoutX() + e.getX() - dragDelta.x,
                    this.endOfProcedure.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePath(double width, double height) {
        this.circle.setCenterX(width / 2);
        this.circle.setCenterY(height / 2);
        this.circle.setRadius(width / 2);
    }
}
