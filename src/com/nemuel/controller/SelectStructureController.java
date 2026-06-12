/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.nemuel.controller;

import com.nemuel.view.structures.Conditional;
import com.nemuel.view.structures.DataEntry;
import com.nemuel.view.structures.EndOfProcedure;
import com.nemuel.view.structures.For;
import com.nemuel.view.structures.Method;
import com.nemuel.view.structures.Print;
import com.nemuel.view.structures.StartEnd;
import com.nemuel.view.structures.SwichCase;
import com.nemuel.view.structures.Process;
import com.nemuel.view.structures.While;
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
    private While wh1le;
    private Method method;
    private EndOfProcedure endOfProcedure;

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

        this.conditional = new Conditional(aPaneConditional);
        this.conditional.buildStructure(5, 5, 80, 25, false);
        this.swichCase = new SwichCase(aPaneConditional);
        this.swichCase.buildStructure(5, 40, 100, 30, false);

        this.f0r = new For(aPaneRepetitive);
        this.f0r.buildStructure(5, 5, 80, 25, false);
        this.wh1le = new While(aPaneRepetitive);
        this.wh1le.buildStructure(5, 40, 80, 25, false);

        this.method = new Method(aPaneOthers);
        this.method.buildStructure(5, 5, 80, 25, false);
        this.endOfProcedure = new EndOfProcedure(aPaneOthers);
        this.endOfProcedure.buildStructure(5, 40, 40, 40, false);
    }

    public void showSequential() {
        this.aPaneSequential.setVisible(true);
        this.aPaneConditional.setVisible(false);
        this.aPaneRepetitive.setVisible(false);
        this.aPaneOthers.setVisible(false);
    }

    public void showConditional() {
        this.aPaneSequential.setVisible(false);
        this.aPaneConditional.setVisible(true);
        this.aPaneRepetitive.setVisible(false);
        this.aPaneOthers.setVisible(false);
    }

    public void showRepetitive() {
        this.aPaneSequential.setVisible(false);
        this.aPaneConditional.setVisible(false);
        this.aPaneRepetitive.setVisible(true);
        this.aPaneOthers.setVisible(false);
    }

    public void showOthers() {
        this.aPaneSequential.setVisible(false);
        this.aPaneConditional.setVisible(false);
        this.aPaneRepetitive.setVisible(false);
        this.aPaneOthers.setVisible(true);
    }

    public StartEnd getStart() {
        return this.start;
    }

    public StartEnd getEnd() {
        return this.end;
    }

    public DataEntry getDataEntry() {
        return this.dataEntry;
    }

    public Process getProcess() {
        return this.process;
    }

    public Print getPrint() {
        return this.print;
    }

    public Conditional getConditional() {
        return this.conditional;
    }

    public SwichCase getSwichCase() {
        return this.swichCase;
    }

    public For getF0r() {
        return this.f0r;
    }

    public While getWh1le() {
        return this.wh1le;
    }

    public Method getMethod() {
        return this.method;
    }

    public EndOfProcedure getEndOfProcedure() {
        return this.endOfProcedure;
    }

    public AnchorPane getApaneSequential() {
        return this.aPaneSequential;
    }

    public AnchorPane getaPaneRepetitive() {
        return this.aPaneRepetitive;
    }

    public AnchorPane getaPaneConditional() {
        return this.aPaneConditional;
    }

    public AnchorPane getaPaneOthers() {
        return this.aPaneOthers;
    }
}
