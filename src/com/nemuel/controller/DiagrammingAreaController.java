/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nemuel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.drawGuideLines();
    }
    
    private void drawGuideLines(){
        Canvas canvas = new Canvas(this.paneLayoutArea.getPrefWidth(), this.paneLayoutArea.getPrefHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIGHTGRAY);
        double cellSize = 15;
        for (double y = 0; y < canvas.getHeight(); y += cellSize) {
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }
        for (double x = 0; x < canvas.getWidth(); x += cellSize) {
            gc.strokeLine(x, 0, x, canvas.getHeight());
        }
        this.paneLayoutArea.getChildren().add(canvas);       
    }
    
}
