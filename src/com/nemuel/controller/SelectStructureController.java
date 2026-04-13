/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nemuel.controller;

import com.nemuel.view.structures.Conditional;
import com.nemuel.view.structures.DataEntry;
import com.nemuel.view.structures.For;
import com.nemuel.view.structures.Print;
import com.nemuel.view.structures.StartEnd;
import com.nemuel.view.structures.SwichCase;
import com.nemuel.view.structures.Process;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author nemuel
 */
public class SelectStructureController implements Initializable {

    @FXML
    private AnchorPane aPaneRepetitive;
    @FXML
    private AnchorPane aPaneSequential;
    @FXML
    private AnchorPane aPaneConditional;
    @FXML
    private AnchorPane aPaneOthers;

    private StartEnd start;
    private StartEnd end;
    private DataEntry dataEntry;
    private Process process;
    private Print print;
    private Conditional conditional;
    private SwichCase swichCase;
    private For f0r;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.start = new StartEnd(aPaneSequential);
        this.start.buildStructure(5, 5, 80, 25, false);
        this.dataEntry = new DataEntry(aPaneSequential);
        this.dataEntry.buildStructure(5, 40, 80, 25, false);
        this.process = new Process(aPaneSequential);
        this.process.buildStructure(5, 75, 80, 25, false);
        this.print = new Print(aPaneSequential);
        this.print.buildStructure(5, 110, 80, 25, false);
        this.end = new StartEnd(aPaneSequential);
        this.end.buildStructure(5, 145, 80, 25, false);
    }

}
