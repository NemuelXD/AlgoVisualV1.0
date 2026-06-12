package com.nemuel.controller;

import com.nemuel.view.structures.Conditional;
import com.nemuel.view.structures.DataEntry;
import com.nemuel.view.structures.EndOfProcedure;
import com.nemuel.view.structures.For;
import com.nemuel.view.structures.Method;
import com.nemuel.view.structures.Print;
import com.nemuel.view.structures.StartEnd;
import com.nemuel.view.structures.Process;
import com.nemuel.view.structures.SwichCase;
import com.nemuel.view.structures.While;
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

    public void dragStructure(Node node, String typeStructure) {
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

            this.drawCircle(x, y, typeStructure);
        });
    }

    private void drawCircle(double x, double y, String typeStructure) {
        Circle circle = new Circle(x, y, 20, Color.RED);
        this.paneLayoutArea.getChildren().add(circle);

        switch (typeStructure) {
            case "Start" -> {
                StartEnd start = new StartEnd(paneLayoutArea);
                start.buildStructure(x - (80 / 2), y - (25 / 2), 80, 25, true);
            }
            case "DataEntry" -> {
                DataEntry dataEntry = new DataEntry(paneLayoutArea);
                dataEntry.buildStructure(x - (80 / 2), y - (25 / 2), 80, 25, true);
            }
            case "Process" -> {
                Process process = new Process(paneLayoutArea);
                process.buildStructure(x - (80 / 2), y - (25 / 2), 80, 25, true);
            }
            case "Print" -> {
                Print print = new Print(paneLayoutArea);
                print.buildStructure(x - (80 / 2), y - (25 / 2), 80, 25, true);
            }
            case "End" -> {
                StartEnd end = new StartEnd(paneLayoutArea);
                end.buildStructure(x - (80 / 2), y - (25 / 2), 80, 25, true);
            }
            case "Conditional" -> {
                Conditional conditional = new Conditional(paneLayoutArea);
                conditional.buildStructure(x - (80 / 2), y - (25 / 2), 100, 25, true);
            }
            case "SwitchCase" -> {
                SwichCase switchC = new SwichCase(paneLayoutArea);
                switchC.buildStructure(x - (80 / 2), y - (25 / 2), 100, 35, true);
            }
            case "For" -> {
                For f0r = new For(paneLayoutArea);
                f0r.buildStructure(x - (80 / 2), y - (25 / 2), 100, 25, true);
            }
            case "While" -> {
                While wh1le = new While(paneLayoutArea);
                wh1le.buildStructure(x - (80 / 2), y - (25 / 2), 100, 25, true);
            }
            case "Method" -> {
                Method method = new Method(paneLayoutArea);
                method.buildStructure(x - (80 / 2), y - (25 / 2), 100, 25, true);
            }
            case "EndOfProcedure" -> {
                EndOfProcedure endOfProcedure = new EndOfProcedure(paneLayoutArea);
                endOfProcedure.buildStructure(x - (80 / 2), y - (25 / 2), 25, 25, true);
            }            
            default ->
                System.out.println("Error estructura no encontrada :(");
        }

    }
}
