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
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Print extends Structure {

    private StackPane print;
    private Path path;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeline;

    public Print(AnchorPane aPane) {
        this.aPane = aPane;
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.print = new StackPane();
        this.print.relocate(x, y);
        //this.print.setStyle("-fx-background-color: red;");
        this.print.setAlignment(Pos.CENTER);
        this.print.setMinSize(40, 12.5);
        this.print.setPrefSize(w, h);
        this.print.setMaxSize(250, 100);
        this.print.setCursor(Cursor.HAND);

        this.path = new Path();
        this.path.setFill(Color.rgb(61, 196, 76));
        this.path.setStroke(Color.BLACK);
        this.path.setStrokeWidth(2);
        if (toAnimate) {
            this.path.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }
        this.print.widthProperty().addListener((obs, oldVal, newVal) -> updatePath(this.print.getWidth(), this.print.getHeight()));
        this.print.heightProperty().addListener((obs, oldVal, newVal) -> updatePath(this.print.getWidth(), this.print.getHeight()));

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.print.getChildren().add(this.path);

        this.print.getChildren().add(this.instruction);

        this.aPane.getChildren().add(this.print);

        if (toAnimate) {
            this.writeInstruction();
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }
    }

    @Override
    public void writeInstruction() {
        this.print.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.print.getPrefWidth());
                ta.setPrefHeight(this.print.getPrefHeight());
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

                this.aPane.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            this.finishEdit(ta);
                        }
                    }
                });

                this.print.getChildren().remove(this.instruction);
                this.print.getChildren().add(ta);
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

        handle.setTranslateX((this.print.getPrefWidth() - TAM_HANDLE) / 2);
        handle.setTranslateY(((this.print.getPrefHeight() - TAM_HANDLE) - 30) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.print.getPrefWidth() + dx;
            double newH = this.print.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.print.setPrefSize(newW, newH);

            handle.setTranslateX((newW - TAM_HANDLE) / 2);
            handle.setTranslateY(((newH - TAM_HANDLE) - 30) / 2);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.print.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.path
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.path.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.path.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)
                )
        );
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.setRate(1.5);
        this.timeline.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.print.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.print.toFront();
                this.print.setCursor(Cursor.HAND);
            }
        });

        this.print.setOnMouseDragged(e -> {
            this.print.relocate(
                    this.print.getLayoutX() + e.getX() - dragDelta.x,
                    this.print.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePath(double width, double height) {
        this.path.getElements().clear();
        double x = 0;
        double y = 0;
        double waveHeight = 15;
        this.path.getElements().addAll(
                new MoveTo(x, y),
                new LineTo(x + width, y),
                new LineTo(x + width, y + height - waveHeight),
                new QuadCurveTo(
                        x + width * 0.80, y + height - waveHeight * 2,
                        x + width * 0.5, y + height
                ),
                new QuadCurveTo(
                        x + width * 0.20, y + height + waveHeight,
                        x, y + height - waveHeight
                ),
                new LineTo(x, y),
                new ClosePath()
        );
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.print.getChildren().remove(ta);

        if (!this.print.getChildren().contains(this.instruction)) {
            this.print.getChildren().add(this.instruction);
        }
    }
}
