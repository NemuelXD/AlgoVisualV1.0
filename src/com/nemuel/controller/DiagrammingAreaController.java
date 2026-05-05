/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nemuel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Nemuel
 */
public class DiagrammingAreaController implements Initializable {

    @FXML
    private Pane paneLayoutArea;

    private DragController dragController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dragController = new DragController(paneLayoutArea);
        this.drawGuideLines();
    }

    public void addStructure(Node structure) {
        this.dragController.dragStructure(structure);
    }

    private void drawGuideLines() {
        double cellSize = 15;
        double blockSize = 150;

        Canvas block = new Canvas(blockSize, blockSize);
        GraphicsContext gc = block.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);

        for (double y = 0; y < blockSize; y += cellSize) {
            gc.strokeLine(0, y, blockSize, y);
        }
        for (double x = 0; x < blockSize; x += cellSize) {
            gc.strokeLine(x, 0, x, blockSize);
        }

        javafx.scene.image.Image pattern = block.snapshot(null, null);
        this.paneLayoutArea.setBackground(
                new javafx.scene.layout.Background(
                        new javafx.scene.layout.BackgroundImage(
                                pattern,
                                javafx.scene.layout.BackgroundRepeat.REPEAT,
                                javafx.scene.layout.BackgroundRepeat.REPEAT,
                                javafx.scene.layout.BackgroundPosition.DEFAULT,
                                javafx.scene.layout.BackgroundSize.DEFAULT
                        )
                )
        );
    }
}
