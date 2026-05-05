package com.nemuel.view.structures;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class For extends Structure {

    private Pane f0r;
    private Path path;
    private Path pathLine;

    public For(Pane paneContainer) {
        super(paneContainer);
    }

    @Override
    public void buildStructure(double x, double y, double w, double h,boolean toAnimate) {
        this.f0r = new Pane();
        this.f0r.relocate(500, 200);
        this.f0r.setStyle("-fx-background-color: red;");
        this.f0r.setMinSize(80, 25);
        this.f0r.setPrefSize(140, 50);
        this.f0r.setMaxSize(250, 100);

        this.path = new Path();
        this.path.setFill(Color.rgb(162, 44, 230));
        this.path.setStroke(Color.BLACK);
        this.path.setStrokeWidth(2);
        this.path.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
        this.path.getElements().addAll(
                new MoveTo(20, 20),
                new LineTo(50, 0),
                new LineTo(110, 0),
                new LineTo(140, 20),
                new LineTo(110, 40),
                new LineTo(50, 40),
                new LineTo(20, 20)
        );
        //StackPane.setAlignment(this.path, Pos.TOP_LEFT);

        this.pathLine = new Path();
        this.pathLine.getElements().addAll(
                new MoveTo(20, 20),
                new LineTo(0, 20),
                new LineTo(0, 50),
                new LineTo(90, 50),
                new LineTo(90, 40)
        );
        //StackPane.setAlignment(this.pathLine, Pos.TOP_LEFT);

        this.f0r.getChildren().add(this.path);

        this.f0r.getChildren().add(this.pathLine);

        this.paneContainer.getChildren().add(this.f0r);
    }

    @Override
    public void writeInstruction() {

    }

    @Override
    public void resize() {

    }

    @Override
    public void animateBorder(boolean toAnimate) {

    }

    @Override
    public void drag() {

    }

}
