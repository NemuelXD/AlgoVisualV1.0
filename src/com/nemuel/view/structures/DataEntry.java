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

public class DataEntry extends Structure {

    private StackPane dataEntry;
    private Polygon polygon;

    private boolean editing = false;

    private double maxDisplace;
    private Timeline timeline;

    public DataEntry(AnchorPane aPane) {
        this.aPane = aPane;
    }

    @Override
    public void buildStructure(double x, double y, double w, double h, boolean toAnimate) {
        this.dataEntry = new StackPane();
        this.dataEntry.relocate(x, y);
        //this.process.setStyle("-fx-background-color: red;");
        this.dataEntry.setAlignment(Pos.CENTER);
        this.dataEntry.setMinSize(40, 12.5);
        this.dataEntry.setPrefSize(w, h);
        this.dataEntry.setMaxSize(250, 100);
        this.dataEntry.setCursor(Cursor.HAND);

        this.polygon = new Polygon();
        this.polygon.setFill(Color.rgb(150, 58, 232));
        this.polygon.setStroke(Color.BLACK);
        this.polygon.setStrokeWidth(2);
        if (toAnimate) {
            this.polygon.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        }

        dataEntry.widthProperty().addListener((obs, oldW, newW) -> updatePolygon(newW.doubleValue(), dataEntry.getHeight()));
        dataEntry.heightProperty().addListener((obs, oldH, newH) -> updatePolygon(dataEntry.getWidth(), newH.doubleValue()));

        this.instruction = new Label("");
        this.instruction.setAlignment(Pos.CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.dataEntry.getChildren().add(this.polygon);

        this.dataEntry.getChildren().add(this.instruction);

        this.aPane.getChildren().add(this.dataEntry);

        if (toAnimate) {
            this.writeInstruction();
            this.resize();
            this.animateBorder(toAnimate);
            this.drag();
        }
    }

    @Override
    public void writeInstruction() {
        this.dataEntry.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;

                TextArea ta = new TextArea(this.instruction.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(this.dataEntry.getPrefWidth());
                ta.setPrefHeight(this.dataEntry.getPrefHeight());
                ta.setPadding(new Insets(1, 30, 1, 30));
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

                this.aPane.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            finishEdit(ta);
                        }
                    }
                });

                this.dataEntry.getChildren().remove(this.instruction);
                this.dataEntry.getChildren().add(ta);
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

        handle.setTranslateX((this.dataEntry.getPrefWidth() - TAM_HANDLE - 65) / 2);
        handle.setTranslateY((this.dataEntry.getPrefHeight() - TAM_HANDLE) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.dataEntry.getPrefWidth() + dx;
            double newH = this.dataEntry.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.dataEntry.setPrefSize(newW, newH);

            handle.setTranslateX((newW - 65 - TAM_HANDLE) / 2);
            handle.setTranslateY((newH - TAM_HANDLE) / 2);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.dataEntry.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.polygon
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.polygon.strokeDashOffsetProperty(), maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.polygon.strokeDashOffsetProperty(), 0, Interpolator.LINEAR))
        );
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.setRate(1.5);
        this.timeline.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.dataEntry.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.dataEntry.toFront();
                this.dataEntry.setCursor(Cursor.HAND);
            }
        });

        this.dataEntry.setOnMouseDragged(e -> {
            this.dataEntry.relocate(
                    this.dataEntry.getLayoutX() + e.getX() - dragDelta.x,
                    this.dataEntry.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatePolygon(double w, double h) {
        polygon.getPoints().setAll(
                Double.valueOf(35), Double.valueOf(0),
                Double.valueOf(w), Double.valueOf(0),
                Double.valueOf(w - 35), Double.valueOf(h),
                Double.valueOf(0), Double.valueOf(h)
        );
    }

    private void finishEdit(TextArea ta) {
        if (!editing) {
            return;
        }

        editing = false;

        this.instruction.setText(ta.getText());
        this.dataEntry.getChildren().remove(ta);

        if (!this.dataEntry.getChildren().contains(this.instruction)) {
            this.dataEntry.getChildren().add(this.instruction);
        }
    }

}
