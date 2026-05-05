/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nemuel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author nemuel
 */
public class StructuresMenuController implements Initializable {

    @FXML
    private TitledPane titledPaneStructures;

    @FXML
    private TitledPane titledPaneSequential;
    
    private FXMLLoader loadSelectStructure;
    public SelectStructureController selectStructureController;

    Image iconStructures = new Image(
            getClass().getResourceAsStream("/com/nemuel/resources/icons/estructuras.png"),
            20,
            20,
            true,
            true
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.titledPaneStructures.setGraphic(new ImageView(this.iconStructures));
        loadStructures();
    }

    private void loadStructures() {
        try {
            this.loadSelectStructure = new FXMLLoader(getClass().getResource("/com/nemuel/view/SelectStructureView.fxml"));
            StackPane selectStructure = this.loadSelectStructure.load();
            this.selectStructureController = this.loadSelectStructure.getController();
            this.titledPaneSequential.setContent(selectStructure);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudieron cargar las estructuras");
            alert.setContentText("Detalle: " + ex.getMessage());
            alert.showAndWait();
            System.getLogger(MainWindowController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public SelectStructureController getSelectStructureController(){
        return this.selectStructureController;
    }
}
