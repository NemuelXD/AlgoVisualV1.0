package com.nemuel.view.structures;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SwichCase extends Structure {

    private StackPane switchC;
    private StackPane sCase;
    private Path path;
    private HBox hBox;
    private Button btnAdd;
    private Label label;

    private double maxDisplace;
    private Timeline timeLine;

    private boolean editing = false;

    public SwichCase(AnchorPane aPane) {
        this.aPane = aPane;
    }

    @Override
    public void buildStructure(double x, double y, double w, double h,boolean toAnimate) {
        this.switchC = new StackPane();
        this.switchC.relocate(300, 200);
        //this.switchC.setStyle("-fx-background-color: red;");
        this.switchC.setMinSize(80, 25);
        this.switchC.setPrefSize(140, 50);
        this.switchC.setMaxSize(250, 100);

        this.path = new Path();
        this.path.setFill(Color.rgb(25, 212, 184));
        this.path.setStroke(Color.BLACK);
        this.path.setStrokeWidth(2);
        this.path.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        this.switchC.widthProperty().addListener((obs, oldVal, newVal) -> updatPath(this.switchC.getPrefWidth(), this.switchC.getPrefHeight()));
        this.switchC.heightProperty().addListener((obs, oldVal, newVal) -> updatPath(this.switchC.getPrefWidth(), this.switchC.getPrefHeight()));
        StackPane.setAlignment(this.path, Pos.CENTER);

        this.hBox = new HBox(0);
        this.hBox.setStyle("-fx-background-color: Transparent;");
        this.hBox.setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(this.hBox, Pos.BOTTOM_CENTER);

        this.btnAdd = new Button("+");
        this.btnAdd.setCursor(Cursor.HAND);
        this.btnAdd.setStyle("-fx-padding: 0;");
        this.hBox.getChildren().add(this.btnAdd);

        this.sCase = new StackPane();
        this.sCase.relocate(0, 0);
        this.sCase.setStyle("""
                            /*-fx-background-color: blue;*/
                            -fx-border-color: black;
                            -fx-border-width: 1;
                            -fx-padding: -2;                           
                            """);

        this.label = new Label("");
        this.label.setAlignment(Pos.CENTER);
        this.label.setWrapText(true);
        this.label.setStyle("""
                           -fx-text-fill: black;
                           -fx-font-family: 'Consolas';
                           -fx-font-size: 12;
                           """);
        this.sCase.getChildren().add(this.label);
        this.writeSecondaryInstruction(this.sCase);

        this.sCase.setPrefHeight(this.switchC.getPrefHeight() - ((this.switchC.getPrefHeight() / 2) + 10));
        this.sCase.setPrefWidth(this.switchC.getPrefHeight() - ((this.switchC.getPrefHeight() / 2) + 10));
        hBox.getChildren().add(hBox.getChildren().size() - 1, this.sCase);

        btnAdd.setOnAction(e -> {
            this.sCase = new StackPane();
            this.sCase.relocate(0, 0);
            this.sCase.setStyle("""
                                /*-fx-background-color: blue;*/
                                -fx-border-color: black;
                                -fx-border-width: 1;
                                -fx-padding: -2;                           
                                """);

            this.label = new Label("");
            this.label.setAlignment(Pos.CENTER);
            this.label.setWrapText(true);
            this.label.setStyle("""
                           -fx-text-fill: black;
                           -fx-font-family: 'Consolas';
                           -fx-font-size: 12;
                           """);
            this.sCase.getChildren().add(this.label);
            this.writeSecondaryInstruction(this.sCase);

            this.sCase.setPrefHeight(this.switchC.getPrefHeight() - ((this.switchC.getPrefHeight() / 2) + 10));
            this.sCase.setPrefWidth(this.switchC.getPrefHeight() - ((this.switchC.getPrefHeight() / 2) + 10));
            hBox.getChildren().add(hBox.getChildren().size() - 1, this.sCase);
        });

        this.instruction = new Label("n");
        this.instruction.setAlignment(Pos.TOP_CENTER);
        this.instruction.setWrapText(true);
        this.instruction.setStyle("""
                                  /*-fx-background-color: blue;*/
                                  -fx-text-fill: black;
                                  -fx-font-family: 'Consolas';
                                  -fx-font-size: 12;
                                  """);

        this.switchC.getChildren().add(this.path);

        this.switchC.getChildren().add(this.instruction);

        this.switchC.getChildren().add(hBox);

        this.aPane.getChildren().add(this.switchC);

        this.writeInstruction();
        this.resize();
        this.animateBorder(toAnimate);
        this.drag();
    }

    @Override
    public void writeInstruction() {

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

        handle.setTranslateX((this.switchC.getPrefWidth() - TAM_HANDLE) / 2);
        handle.setTranslateY((this.switchC.getPrefHeight() - TAM_HANDLE) / 2);

        final Delta resizeDelta = new Delta();

        handle.setOnMousePressed(e -> {
            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();
            e.consume();
        });

        handle.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - resizeDelta.x;
            double dy = e.getSceneY() - resizeDelta.y;

            double newW = this.switchC.getPrefWidth() + dx;
            double newH = this.switchC.getPrefHeight() + dy;

            newW = Math.max(MIN_W, Math.min(MAX_W, newW));
            newH = Math.max(MIN_H, Math.min(MAX_H, newH));

            this.switchC.setPrefSize(newW, newH);

            handle.setTranslateX((newW - TAM_HANDLE) / 2);
            handle.setTranslateY((newH - TAM_HANDLE) / 2);

            resizeDelta.x = e.getSceneX();
            resizeDelta.y = e.getSceneY();

            e.consume();
        });

        this.switchC.getChildren().add(handle);
    }

    @Override
    public void animateBorder(boolean toAnimate) {
        this.maxDisplace = this.path
                .getStrokeDashArray()
                .stream()
                .mapToDouble(Double::doubleValue)
                .reduce(0d, (a, b) -> a + b);

        this.timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.path.strokeDashOffsetProperty(), this.maxDisplace, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(this.path.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)
                )
        );
        this.timeLine.setCycleCount(Timeline.INDEFINITE);
        this.timeLine.setRate(1.5);
        this.timeLine.play();
    }

    @Override
    public void drag() {
        final Delta dragDelta = new Delta();

        this.switchC.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                dragDelta.x = e.getX();
                dragDelta.y = e.getY();
                this.switchC.toFront();
                this.switchC.setCursor(Cursor.HAND);
            }
        });

        this.switchC.setOnMouseDragged(e -> {
            this.switchC.relocate(
                    this.switchC.getLayoutX() + e.getX() - dragDelta.x,
                    this.switchC.getLayoutY() + e.getY() - dragDelta.y
            );
        });
    }

    private void updatPath(double width, double height) {
        this.path.getElements().clear();
        this.path.getElements().addAll(
                new MoveTo(0, (height / 2) + 10),
                new LineTo(width, (height / 2) + 10),
                new LineTo(width / 2, 0),
                new LineTo(0, (height / 2) + 10),
                new LineTo(0, height),
                new LineTo(width, height),
                new LineTo(width, (height / 2) + 10)
        );

        this.hBox.setMinSize(width, height - ((height / 2) + 10));
        this.hBox.setPrefSize(width, height - ((height / 2) + 10));
        this.hBox.setMaxSize(width, height - ((height / 2) + 10));

        this.btnAdd.setMaxHeight(height - ((height / 2) + 10));
        this.btnAdd.setPrefWidth(height - ((height / 2) + 10));

        for (int i = 0; i < hBox.getChildren().size() - 1; i++) {
            StackPane sp = (StackPane) hBox.getChildren().get(i);
            sp.setPrefHeight(height - ((height / 2) + 10));
            sp.setPrefWidth(height - ((height / 2) + 10));
        }
    }

    private void writeSecondaryInstruction(StackPane sp) {
        sp.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !editing) {
                editing = true;
                Label lbl = (Label) sp.getChildren().get(0);
                TextArea ta = new TextArea(lbl.getText());
                ta.setWrapText(true);
                ta.setPrefWidth(sp.getPrefWidth());
                ta.setPrefHeight(sp.getPrefHeight());
                ta.setPadding(new Insets(1, 1, 1, 1));
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

                ta.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                ta.setPrefSize(sp.getWidth(), sp.getHeight());

                StackPane.setAlignment(ta, Pos.CENTER);

                Platform.runLater(() -> {
                    ta.requestFocus();
                    ta.positionCaret(ta.getText().length());
                });

                ta.setOnKeyPressed(ev -> {
                    if (ev.isControlDown() && ev.getCode() == KeyCode.ENTER) {
                        this.finishEdit(ta, lbl, sp);
                    }
                });

                ta.focusedProperty().addListener((obs, oldV, newV) -> {
                    if (!newV) {
                        this.finishEdit(ta, lbl, sp);
                    }
                });

                this.aPane.setOnMousePressed(ev -> {
                    if (editing) {
                        if (!ta.equals(ev.getTarget()) && !ta.isHover()) {
                            this.finishEdit(ta, lbl, sp);
                        }
                    }
                });

                sp.getChildren().remove(lbl);
                sp.getChildren().add(ta);
            }
        });
    }

    private void finishEdit(TextArea ta, Label lbl, StackPane sp) {
        if (!editing) {
            return;
        }

        editing = false;

        lbl.setText(ta.getText());
        sp.getChildren().remove(ta);

        if (!sp.getChildren().contains(lbl)) {
            sp.getChildren().add(lbl);
        }
    }
}
