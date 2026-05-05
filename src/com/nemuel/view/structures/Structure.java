package com.nemuel.view.structures;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public abstract class Structure {

    protected Label instruction;
    protected Pane paneContainer;
    protected TextField editor;

    public Structure(Pane paneContainer) {
        this.paneContainer = paneContainer;
    }

    public abstract void buildStructure(double x, double y, double w, double h, boolean toAnimate);

    public abstract void writeInstruction();

    public abstract void resize();

    public abstract void animateBorder(boolean toAnimate);

    public abstract void drag();
}
