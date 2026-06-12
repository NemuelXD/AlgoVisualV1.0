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

public class While extends Structure {

    private StackPane wh1le;
    private Path path;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeLine;

    public While(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.wh1le = new StackPane();
        this.wh1le.relocate(x, y);
        //this.wh1le.setStyle("-fx-background-color: red;");
        this.wh1le.setAlignment(Pos.CENTER);
        this.wh1le.setMinSize(80, 25);
        this.wh1le.setPrefSize(w, h);
        this.wh1le.setMaxSize(250, 100);
        this.wh1le.setCursor(Cursor.HAND);

        this.path = new Path();
        this.path.setFill(Color.rgb(61, 196, 76));
        this.path.setStroke(Color.BLACK);
        this.path.setStrokeWidth(2);
        if (toAnimate) {
            this.path.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }
        this.wh1le.widthProperty().addListener((obs, oldVal, newVal) -> updatePath(this.wh1le.getWidth(), this.wh1le.getHeight()));
        this.wh1le.heightProperty().addListener((obs, oldVal, newVal) -> updatePath(this.wh1le.getWidth(), this.wh1le.getHeight()));

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.wh1le.getChildren().add(this.path);
        this.wh1le.getChildren().add(this.instruction);
        this.paneContainer.getChildren().add(this.wh1le);

        if (toAnimate) {
            this.writeInstruction();
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }

    }

    @Override
    public void writeInstruction() {
        this.wh1le.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.wh1le.getPrefWidth());
                ta.setPrefHeight(this.wh1le.getPrefHeight());
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

                this.wh1le.getChildren().remove(this.instruction);
                this.wh1le.getChildren().add(ta);
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
        handle.setTranslateX((this.wh1le.getPrefWidth() - TAM_HANDLE) / 2);
        handle.setTranslateY((this.wh1le.getPrefHeight() - TAM_HANDLE) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.wh1le.getPrefWidth() + dx;
            double newH = this.wh1le.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.wh1le.setPrefSize(newW, newH);

            handle.setTranslateX((newW - TAM_HANDLE) / 2);
            handle.setTranslateY((newH - TAM_HANDLE) / 2);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.wh1le.getChildren().add(handle);
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

        this.wh1le.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.wh1le.toFront();
                this.wh1le.setCursor(Cursor.HAND);
            }
        });

        this.wh1le.setOnMouseDragged(e -> {
            this.wh1le.relocate(
                    this.wh1le.getLayoutX() + e.getX() - dragDelta.x,
                    this.wh1le.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePath(double width, double height) {
        this.path.getElements().clear();
        this.path.getElements().addAll(
                new MoveTo(0, height / 2),
                new LineTo(35, 0),
                new LineTo(width, 0),
                new LineTo(width, height),
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
        this.wh1le.getChildren().remove(ta);

        if (!this.wh1le.getChildren().contains(this.instruction)) {
            this.wh1le.getChildren().add(this.instruction);
        }
    }

    public StackPane getWh1le() {
        return this.wh1le;
    }

}
