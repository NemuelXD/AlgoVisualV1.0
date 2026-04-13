package com.nemuel.view.structures;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public abstract class Structure {

    protected Label instruction;
    protected AnchorPane aPane;
    protected TextField editor;

    public abstract void buildStructure(double x, double y, double w, double h, boolean toAnimate);

    public abstract void writeInstruction();

    public abstract void resize();

    public abstract void animateBorder(boolean toAnimate);

    public abstract void drag();
}
