package com.nemuel.controller;

import com.nemuel.view.structures.StartEnd;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

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
    private AnchorPane aPaneDiagrammingArea;
    @FXML
    private AnchorPane aPaneDesktopTestingArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Tooltip.install(imgViewNewFile, new Tooltip("Nuevo archivo"));
        this.loadModules();
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
            FXMLLoader loadStructureMenu = new FXMLLoader(getClass().getResource("/com/nemuel/view/StructuresMenuView.fxml"));
            AnchorPane structureMenu = loadStructureMenu.load();
            StructuresMenuController structureMenuController = loadStructureMenu.getController();
            this.aPaneStructuresMenu.getChildren().setAll(structureMenu);
            AnchorPane.setTopAnchor(structureMenu, 0.0);
            AnchorPane.setBottomAnchor(structureMenu, 0.0);
            AnchorPane.setLeftAnchor(structureMenu, 0.0);
            AnchorPane.setRightAnchor(structureMenu, 0.0);

            FXMLLoader loadDiagrammingArea = new FXMLLoader(getClass().getResource("/com/nemuel/view/DiagrammingAreaView.fxml"));
            TabPane diagrammingArea = loadDiagrammingArea.load();
            DiagrammingAreaController diagrammingAreaController = loadDiagrammingArea.getController();
            this.aPaneDiagrammingArea.getChildren().setAll(diagrammingArea);
            AnchorPane.setTopAnchor(diagrammingArea, 0.0);
            AnchorPane.setBottomAnchor(diagrammingArea, 0.0);
            AnchorPane.setLeftAnchor(diagrammingArea, 0.0);
            AnchorPane.setRightAnchor(diagrammingArea, 0.0);

            structureMenuController.getSelectStructureController().getStart().getStartEnd().setOnMousePressed(e -> {
                StartEnd newStart = new StartEnd(structureMenuController.getSelectStructureController().getApaneSequential());
                newStart.buildStructure(5, 5, 80, 25, false);
                newStart.getStartEnd().requestFocus();
                diagrammingAreaController.addStructure(newStart.getStartEnd());
            });

            FXMLLoader loadDesktopTestingArea = new FXMLLoader(getClass().getResource("/com/nemuel/view/DesktopTestingAreaView.fxml"));
            ScrollPane desktopTestingArea = loadDesktopTestingArea.load();
            DesktopTestingAreaController desktopTestingAreaController = loadDesktopTestingArea.getController();
            AnchorPane.setTopAnchor(desktopTestingArea, 0.0);
            AnchorPane.setBottomAnchor(desktopTestingArea, 0.0);
            AnchorPane.setLeftAnchor(desktopTestingArea, 0.0);
            AnchorPane.setRightAnchor(desktopTestingArea, 0.0);
            this.aPaneDesktopTestingArea.getChildren().setAll(desktopTestingArea);

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo cargar el módulo");
            alert.setContentText("Detalle: " + ex.getMessage());
            alert.showAndWait();
            System.getLogger(MainWindowController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
