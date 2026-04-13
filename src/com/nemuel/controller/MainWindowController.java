package com.nemuel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author nemuel
 */
public class MainWindowController implements Initializable {

    @FXML
    private ImageView imgViewNewFile;

    private Tooltip toolTip;

    @FXML
    private HBox hBoxFileModule;
    @FXML
    private HBox hBoxConfigurationModule;
    @FXML
    private HBox hBoxAppearanceModule;
    @FXML
    private HBox hBoxExecutionModule;
    @FXML
    private HBox hBoxToolsModule;
    @FXML
    private AnchorPane aPaneStructuresMenu;
    @FXML
    private HBox hBoxHelpModule;
    @FXML
    private Pane paneLayoutArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(imgViewNewFile, new Tooltip("Nuevo archivo"));
        this.loadModules();
        this.drawGuideLines();
    }

    @FXML
    private void showFileModule(Event event) {
        hBoxFileModule.setVisible(true);
        hBoxConfigurationModule.setVisible(false);
        hBoxAppearanceModule.setVisible(false);
        hBoxExecutionModule.setVisible(false);
        hBoxToolsModule.setVisible(false);
        hBoxHelpModule.setVisible(false);
    }

    @FXML
    private void showConfigurationModule(Event event) {
        hBoxFileModule.setVisible(false);
        hBoxConfigurationModule.setVisible(true);
        hBoxAppearanceModule.setVisible(false);
        hBoxExecutionModule.setVisible(false);
        hBoxToolsModule.setVisible(false);
        hBoxHelpModule.setVisible(false);
    }

    @FXML
    private void showAppearanceModule(Event event) {
        hBoxFileModule.setVisible(false);
        hBoxConfigurationModule.setVisible(false);
        hBoxAppearanceModule.setVisible(true);
        hBoxExecutionModule.setVisible(false);
        hBoxToolsModule.setVisible(false);
        hBoxHelpModule.setVisible(false);
    }

    @FXML
    void showExecutionModule(Event event) {
        hBoxFileModule.setVisible(false);
        hBoxConfigurationModule.setVisible(false);
        hBoxAppearanceModule.setVisible(false);
        hBoxExecutionModule.setVisible(true);
        hBoxToolsModule.setVisible(false);
        hBoxHelpModule.setVisible(false);
    }

    @FXML
    private void showToolsModule(Event event) {
        hBoxFileModule.setVisible(false);
        hBoxConfigurationModule.setVisible(false);
        hBoxAppearanceModule.setVisible(false);
        hBoxExecutionModule.setVisible(false);
        hBoxToolsModule.setVisible(true);
        hBoxHelpModule.setVisible(false);
    }

    @FXML
    private void showHelpModule(ActionEvent event) {
        hBoxFileModule.setVisible(false);
        hBoxConfigurationModule.setVisible(false);
        hBoxAppearanceModule.setVisible(false);
        hBoxExecutionModule.setVisible(false);
        hBoxToolsModule.setVisible(false);
        hBoxHelpModule.setVisible(true);
    }

    private void loadModules() {
        try {
            Node node = (AnchorPane) FXMLLoader.load(getClass().getResource("/com/nemuel/view/StructuresMenuView.fxml"));
            this.aPaneStructuresMenu.getChildren().setAll(node);
            AnchorPane.setTopAnchor(node, 0.0);
            AnchorPane.setBottomAnchor(node, 0.0);
            AnchorPane.setLeftAnchor(node, 0.0);
            AnchorPane.setRightAnchor(node, 0.0);

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar el módulo");
            alert.setContentText("Detalle: " + ex.getMessage());
            alert.showAndWait();
            System.getLogger(MainWindowController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private void drawGuideLines() {
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
