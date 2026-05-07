package com.nemuel.controller;

import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DragController {
    
    private double posMouseX;
    private double posMouseY;

    private double relocateX;
    private double relocateY;

    private double offsetX;
    private double offsetY;

    private Pane paneLayoutArea;
    private ImageView ghost;

    private static final double RADIUS_CIRCLE = 20;
    private static final double MARGIN = 10;

    public DragController(Pane paneLayoutArea) {
        this.paneLayoutArea = paneLayoutArea;
    }

    public void dragStructure(Node node) {

        node.setOnMousePressed(mouseEvent -> {

            // 📍 calcular offset (dónde hiciste click dentro del nodo)
            offsetX = mouseEvent.getX();
            offsetY = mouseEvent.getY();

            // 📸 snapshot transparente
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);

            WritableImage snapshot = node.snapshot(params, null);

            ghost = new ImageView(snapshot);
            ghost.setOpacity(0.7);
            ghost.setMouseTransparent(true);

            Pane root = (Pane) paneLayoutArea.getScene().getRoot();
            root.getChildren().add(ghost);

            // 🔥 AQUÍ está la clave → aplicar offset desde el inicio
            ghost.setLayoutX(mouseEvent.getSceneX() - offsetX);
            ghost.setLayoutY(mouseEvent.getSceneY() - offsetY);
        });

        node.setOnMouseDragged(mouseEvent -> {
            if (ghost != null) {
                ghost.setLayoutX(mouseEvent.getSceneX() - offsetX);
                ghost.setLayoutY(mouseEvent.getSceneY() - offsetY);
            }
        });

        node.setOnMouseReleased(mouseEvent -> {

            Pane root = (Pane) paneLayoutArea.getScene().getRoot();

            if (ghost != null) {
                root.getChildren().remove(ghost);
                ghost = null;
            }

            // 🔴 1. VERIFICAR EN COORDENADAS DE ESCENA (ANTES DE CONVERTIR)
            double sceneX = mouseEvent.getSceneX();
            double sceneY = mouseEvent.getSceneY();

            boolean insideScene
                    = sceneX >= paneLayoutArea.localToScene(0, 0).getX()
                    && sceneX <= paneLayoutArea.localToScene(paneLayoutArea.getWidth(), 0).getX()
                    && sceneY >= paneLayoutArea.localToScene(0, 0).getY()
                    && sceneY <= paneLayoutArea.localToScene(0, paneLayoutArea.getHeight()).getY();

            if (!insideScene) {
                return; // ❌ fuera del pane → no hacer nada
            }

            // 🔵 2. AHORA SÍ convertir a local
            double x = paneLayoutArea.sceneToLocal(sceneX, sceneY).getX();
            double y = paneLayoutArea.sceneToLocal(sceneX, sceneY).getY();

            // 🔒 3. clamp dentro del área
            x = Math.max(RADIUS_CIRCLE + MARGIN,
                    Math.min(x, paneLayoutArea.getWidth() - RADIUS_CIRCLE - MARGIN));

            y = Math.max(RADIUS_CIRCLE + MARGIN,
                    Math.min(y, paneLayoutArea.getHeight() - RADIUS_CIRCLE - MARGIN));

            // 🎯 acción final
            drawCircle(x, y);
        });
    }

    private void drawCircle(double x, double y) {
        Circle circle = new Circle(x, y, 20, Color.RED);
        this.paneLayoutArea.getChildren().add(circle);
    }
}
