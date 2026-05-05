package com.nemuel.controller;

import com.nemuel.view.structures.StartEnd;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DragController {

    private double posMouseX;
    private double posMouseY;

    private double relocateX;
    private double relocateY;

    private Pane paneLayoutArea;

    private static final double RADIUS_CIRCLE = 20;
    private static final double MARGIN = 10;

    public DragController(Pane paneLayoutArea) {
        this.paneLayoutArea = paneLayoutArea;
    }

    public void dragStructure(Node node) {
        double initialDesplacementX = node.getTranslateX();
        double initialDesplacementY = node.getTranslateY();

        node.setOnMousePressed((mouseEvent) -> {
            this.posMouseX = mouseEvent.getSceneX();
            this.posMouseY = mouseEvent.getSceneY();

            this.relocateX = ((Node) (mouseEvent.getSource())).getTranslateX();
            this.relocateY = ((Node) (mouseEvent.getSource())).getTranslateY();

            node.setOpacity(0.5);
        });

        node.setOnMouseDragged((mouseEvent) -> {
            double dragX = mouseEvent.getSceneX() - this.posMouseX;
            double dragY = mouseEvent.getSceneY() - this.posMouseY;

            double newRelocateX = this.relocateX + dragX;
            double newRelocateY = this.relocateY + dragY;

            node.setTranslateX(newRelocateX);
            node.setTranslateY(newRelocateY);
        });

        node.setOnMouseReleased((mouseEvent) -> {
            node.setTranslateX(initialDesplacementX);
            node.setTranslateY(initialDesplacementY);
            node.setOpacity(1.0);

            double x = this.paneLayoutArea.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
            double y = this.paneLayoutArea.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getY();

            x = Math.max(RADIUS_CIRCLE + MARGIN, Math.min(x, this.paneLayoutArea.getWidth() - RADIUS_CIRCLE - MARGIN));
            y = Math.max(RADIUS_CIRCLE + MARGIN, Math.min(y, this.paneLayoutArea.getHeight() - RADIUS_CIRCLE - MARGIN));

            this.drawCircle(x, y);
        });
    }

    private void drawCircle(double x, double y) {
        Circle circle = new Circle(x, y, 20, Color.RED);
        this.paneLayoutArea.getChildren().add(circle);        
    }
}
