package com.nemuel.controller;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GhostDragController {

    private double offsetX;
    private double offsetY;

    private Pane paneLayoutArea;
    private Pane root;

    private SnapshotParameters params;
    private WritableImage snapshot;
    private ImageView imgGhost;

    private final double RADIUS_CIRCLE = 20;
    private final double MARGIN = 10;

    public GhostDragController(Pane paneLayoutArea) {
        this.paneLayoutArea = paneLayoutArea;
    }

    public void dragStructure(Node node) {
        node.setOnMousePressed(mouseEvent -> {
            this.offsetX = mouseEvent.getX();
            this.offsetY = mouseEvent.getY();

            this.params = new SnapshotParameters();
            this.params.setFill(Color.TRANSPARENT);

            this.snapshot = node.snapshot(this.params, null);

            this.imgGhost = new ImageView(this.snapshot);
            this.imgGhost.setOpacity(0.7);
            this.imgGhost.setMouseTransparent(true);

            this.root = (Pane) this.paneLayoutArea.getScene().getRoot();
            this.root.getChildren().add(imgGhost);

            this.imgGhost.setLayoutX(mouseEvent.getSceneX() - offsetX);
            this.imgGhost.setLayoutY(mouseEvent.getSceneY() - offsetY);
        });

        node.setOnMouseDragged(mouseEvent -> {
            if (this.imgGhost != null) {
                this.imgGhost.setLayoutX(mouseEvent.getSceneX() - offsetX);
                this.imgGhost.setLayoutY(mouseEvent.getSceneY() - offsetY);
            }
        });

        node.setOnMouseReleased(mouseEvent -> {
            this.root = (Pane) this.paneLayoutArea.getScene().getRoot();

            if (this.imgGhost != null) {
                this.root.getChildren().remove(this.imgGhost);
                this.imgGhost = null;
            }

            double sceneX = mouseEvent.getSceneX();
            double sceneY = mouseEvent.getSceneY();

            boolean insideScene = sceneX >= this.paneLayoutArea.localToScene(0, 0).getX()
                    && sceneX <= this.paneLayoutArea.localToScene(this.paneLayoutArea.getWidth(), 0).getX()
                    && sceneY >= this.paneLayoutArea.localToScene(0, 0).getY()
                    && sceneY <= this.paneLayoutArea.localToScene(0, this.paneLayoutArea.getHeight()).getY();

            if (!insideScene) {
                return;
            }

            double x = this.paneLayoutArea.sceneToLocal(sceneX, sceneY).getX();
            double y = this.paneLayoutArea.sceneToLocal(sceneX, sceneY).getY();

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
