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

    @FXML
    private TitledPane titledPaneConditional;

    @FXML
    private TitledPane titledPaneRepetitive;

    @FXML
    private TitledPane titledPaneOthers;

    private FXMLLoader loadSelectStructure1;
    private FXMLLoader loadSelectStructure2;
    private FXMLLoader loadSelectStructure3;
    private FXMLLoader loadSelectStructure4;

    public SelectStructureController selectStructureController1;
    public SelectStructureController selectStructureController2;
    public SelectStructureController selectStructureController3;
    public SelectStructureController selectStructureController4;

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
            this.loadSelectStructure1 = new FXMLLoader(getClass().getResource("/com/nemuel/view/SelectStructureView.fxml"));
            StackPane selectStructure1 = this.loadSelectStructure1.load();
            this.selectStructureController1 = this.loadSelectStructure1.getController();

            this.loadSelectStructure2 = new FXMLLoader(getClass().getResource("/com/nemuel/view/SelectStructureView.fxml"));
            StackPane selectStructure2 = this.loadSelectStructure2.load();
            this.selectStructureController2 = this.loadSelectStructure2.getController();

            this.loadSelectStructure3 = new FXMLLoader(getClass().getResource("/com/nemuel/view/SelectStructureView.fxml"));
            StackPane selectStructure3 = this.loadSelectStructure3.load();
            this.selectStructureController3 = this.loadSelectStructure3.getController();

            this.loadSelectStructure4 = new FXMLLoader(getClass().getResource("/com/nemuel/view/SelectStructureView.fxml"));
            StackPane selectStructure4 = this.loadSelectStructure4.load();
            this.selectStructureController4 = this.loadSelectStructure4.getController();

            selectStructureController1.showSequential();
            selectStructureController2.showConditional();
            selectStructureController3.showRepetitive();
            selectStructureController4.showOthers();

            this.titledPaneSequential.setContent(selectStructure1);
            this.titledPaneConditional.setContent(selectStructure2);
            this.titledPaneRepetitive.setContent(selectStructure3);
            this.titledPaneOthers.setContent(selectStructure4);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudieron cargar las estructuras");
            alert.setContentText("Detalle: " + ex.getMessage());
            alert.showAndWait();
            System.getLogger(MainWindowController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            System.exit(0);
        }
    }

    public SelectStructureController getSelectStructureController() {
        return this.selectStructureController1;
    }
}
