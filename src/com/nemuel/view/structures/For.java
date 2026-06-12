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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class For extends Structure {

    private StackPane f0r;
    private Path path;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeLine;

    public For(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.f0r = new StackPane();
        this.f0r.relocate(x, y);
        //this.f0r.setStyle("-fx-background-color: red;");
        this.f0r.setAlignment(Pos.CENTER);
        this.f0r.setMinSize(80, 25);
        this.f0r.setPrefSize(w, h);
        this.f0r.setMaxSize(250, 100);
        this.f0r.setCursor(Cursor.HAND);

        this.path = new Path();
        this.path.setFill(Color.rgb(61, 196, 76));
        this.path.setStroke(Color.BLACK);
        this.path.setStrokeWidth(2);
        if (toAnimate) {
            this.path.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }

        this.f0r.widthProperty().addListener((obs, oldVal, newVal) -> updatePath(this.f0r.getWidth(), this.f0r.getHeight()));
        this.f0r.heightProperty().addListener((obs, oldVal, newVal) -> updatePath(this.f0r.getWidth(), this.f0r.getHeight()));

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.f0r.getChildren().add(this.path);
        this.f0r.getChildren().add(this.instruction);
        this.paneContainer.getChildren().add(this.f0r);

        if (toAnimate) {
            this.writeInstruction();
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }
    }

    @Override
    public void writeInstruction() {
        this.f0r.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.f0r.getPrefWidth());
                ta.setPrefHeight(this.f0r.getPrefHeight());
                ta.setPadding(new Insets(1, 1, 1, 1));
                ta.setOpacity(1);
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

                this.paneContainer.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            finishEdit(ta);
                        }
                    }
                });

                this.f0r.getChildren().remove(this.instruction);
                this.f0r.getChildren().add(ta);
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
        handle.setTranslateX((this.f0r.getPrefWidth() - 60 - TAM_HANDLE) / 2);
        handle.setTranslateY((this.f0r.getPrefHeight() - TAM_HANDLE) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.f0r.getPrefWidth() + dx;
            double newH = this.f0r.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.f0r.setPrefSize(newW, newH);

            handle.setTranslateX((newW - 60 - TAM_HANDLE) / 2);
            handle.setTranslateY((newH - TAM_HANDLE) / 2);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.f0r.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.path
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.path.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.path.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
        );
        this.timeLine.setCycleCount(Timeline.INDEFINITE);
        this.timeLine.setRate(1.5);
        this.timeLine.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.f0r.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.f0r.toFront();
                this.f0r.setCursor(Cursor.HAND);
            }
        });

        this.f0r.setOnMouseDragged(e -> {
            this.f0r.relocate(
                    this.f0r.getLayoutX() + e.getX() - dragDelta.x,
                    this.f0r.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePath(double width, double height) {
        this.path.getElements().clear();
        double x = 0;
        double y = 0;
        this.path.getElements().addAll(
                new MoveTo(0, height / 2),
                new LineTo(35, 0),
                new LineTo(width - 35, 0),
                new LineTo(width, height / 2),
                new LineTo(width - 35, height),
                new LineTo(35, height),
                new ClosePath()
        );
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.f0r.getChildren().remove(ta);

        if (!this.f0r.getChildren().contains(this.instruction)) {
            this.f0r.getChildren().add(this.instruction);
        }
    }

    public StackPane getF0r() {
        return this.f0r;
    }
}
