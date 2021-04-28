
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.*;
import javafx.scene.control.ColorPicker;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.text.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

class View1 extends Pane implements IView {
    Image impattern2=new Image("pattern2.jpg");
    Image impattern3=new Image("pattern3.jpg");
    Image imthick=new Image("thick.jpg");
    Image imthin=new Image("thin.jpg");
    Image imline=new Image("line.jpg");
    Image imtool1=new Image("tool1.jpg");
    Image imtool2=new Image("tool2.jpg");
    Image imtool3=new Image("tool3.jpg");
    Image imtool4=new Image("tool4.jpg");
    Image imtool5=new Image("tool5.jpg");
    Image imtool6=new Image("tool6.jpg");

    private Button Select = new View1.StandardButton();
    private Button Erase = new View1.StandardButton();
    private Button Line = new View1.StandardButton();
    private Button Cir = new View1.StandardButton();
    private Button Rec = new View1.StandardButton();
    private Button Fill = new View1.StandardButton();
    private Label LineCol = new Label("Line Color");
    private Label FillCol = new Label("Fill Color");
    private ColorPicker lineColor = new ColorPicker();
    private ColorPicker fillColor = new ColorPicker();
    private ToggleGroup group1 = new ToggleGroup();
    private ToggleButton thick = new ToggleButton();
    private ToggleButton mid = new ToggleButton();
    private ToggleButton thin = new ToggleButton();
    private ToggleGroup group2 = new ToggleGroup();
    private ToggleButton pattern1 = new ToggleButton();
    private ToggleButton pattern2 = new ToggleButton();
    private ToggleButton pattern3 = new ToggleButton();
    private VBox linePattern = new VBox();
    private VBox thickness = new VBox();
    private GridPane gridpane = new GridPane();

    private Model model; // reference to the model

    View1(Model model) {
        super();
        pattern1.setGraphic(new ImageView(imline));
        pattern2.setGraphic(new ImageView(impattern2));
        pattern3.setGraphic(new ImageView(impattern3));
        thin.setGraphic(new ImageView(imthin));
        mid.setGraphic(new ImageView(imline));
        thick.setGraphic(new ImageView(imthick));

        Select.setGraphic(new ImageView(imtool1));
        Erase.setGraphic(new ImageView(imtool2));
        Cir.setGraphic(new ImageView(imtool4));
        Rec.setGraphic(new ImageView(imtool5));
        Line.setGraphic(new ImageView(imtool3));
        Fill.setGraphic(new ImageView(imtool6));



        LineCol.setFont(Font.font ("Verdana", 15));
        LineCol.setPrefSize(105,25);
        FillCol.setFont(Font.font ("Verdana", 15));
        FillCol.setPrefSize(105,25);
        lineColor.setValue(Color.GOLD);
        lineColor.setPrefSize(105,40);
        fillColor.setValue(Color.CORAL);
        fillColor.setPrefSize(105,40);

        // create toggle buttons and toggle group to control behaviour
        thick.setPrefSize(105,50);
        mid.setPrefSize(105,50);
        thin.setPrefSize(105,50);
        group1.getToggles().addAll(thick, mid, thin);
        // setup the layout
        thickness.getChildren().add(thick);
        thickness.getChildren().add(mid);
        thickness.getChildren().add(thin);

        // create toggle buttons and toggle group to control behaviour

        pattern1.setPrefSize(105,50);
        pattern2.setPrefSize(105,50);
        pattern3.setPrefSize(105,50);
        group2.getToggles().addAll(pattern1, pattern2, pattern3);
        // setup the layout
        linePattern.getChildren().add(pattern1);
        linePattern.getChildren().add(pattern2);
        linePattern.getChildren().add(pattern3);

        gridpane.add(Select,0,0);
        gridpane.add(Erase,1, 0);
        gridpane.add(Line, 0, 1);
        gridpane.add(Cir, 1,1);
        gridpane.add(Rec,0,2);
        gridpane.add(Fill,1,2);
        gridpane.add(LineCol, 0,3);
        gridpane.add(FillCol, 1,3);
        gridpane.add(lineColor, 0,4);
        gridpane.add(fillColor, 1,4);
        gridpane.add(thickness,0,8);
        gridpane.add(linePattern,1,8);

        this.model = model;
        // add gridpane to the pane
        this.getChildren().add(gridpane);
        this.registerControllers();
        // register with the model when we're ready to start receiving data
        model.addView(this);

    }


    private void registerControllers() {
        this.lineColor.setOnAction(new EventHandler() {
            public void handle(Event t) {
                model.setLineColorChanged(true);
                //System.out.println("Controller: changing Model (actionPerformed)");
                model.changeLineColor(lineColor.getValue());
                model.setLineColorChanged(false);
            }
        });

        this.fillColor.setOnAction(new EventHandler() {
            public void handle(Event t) {
                //System.out.println("Controller: changing Model (actionPerformed)");
                model.setFillColorChanged(true);
                //System.out.println("from fillColor.setOnAction change fillColorChanged to "+ model.getFillColorChanged());
                model.changeFillColor(fillColor.getValue());
                model.setFillColorChanged(false);
            }
        });

        this.Select.setOnAction(event -> {
            model.changeTool(1);
            fillColor.setDisable(false);
            model.setSelecting(true);
        });
        this.Erase.setOnAction(event -> {
            model.changeTool(2);
            fillColor.setDisable(false);
            model.setSelecting(false);
        });
        this.Line.setOnAction(event -> {
            model.changeTool(3);
            fillColor.setDisable(true);
            model.setSelecting(false);
            //System.out.println("select the line");
        });
        this.Cir.setOnAction(event -> {

            model.changeTool(4);
            fillColor.setDisable(false);
            model.setSelecting(false);
            //System.out.println("pressed the Cir button");
            //System.out.println(model.getToolType());
        });
        this.Rec.setOnAction(event -> {
            model.setSelecting(false);
            model.changeTool(5);
            fillColor.setDisable(false);
        });
        this.Fill.setOnAction(event -> {
            model.setSelecting(false);
            model.changeTool(6);
            fillColor.setDisable(false);
        });

        this.thick.setOnAction(event -> {
            model.setThicknessChaged(true);
            model.changeLineThickness(3);
            model.setThicknessChaged(false);
        });
        this.mid.setOnAction(event -> {
            model.setThicknessChaged(true);
            model.changeLineThickness(2);
            model.setThicknessChaged(false);
        });
        this.thin.setOnAction(event -> {
            model.setThicknessChaged(true);
            model.changeLineThickness(1);
            model.setThicknessChaged(false);
        });
        this.pattern1.setOnAction(event -> {
            model.setPatternChanged(true);
            model.changeLinePattern(1.0);
            model.setPatternChanged(false);
        });
        this.pattern2.setOnAction(event -> {
            model.setPatternChanged(true);
            model.changeLinePattern(2.0);
            model.setPatternChanged(false);
        });
        this.pattern3.setOnAction(event -> {
            model.setPatternChanged(true);
            model.changeLinePattern(3.0);
            model.setPatternChanged(false);
        });

    }

    // When notified by the model that things have changed,
    // update to display the new value
    public void updateView(boolean LineColorChanged, boolean FillColorChanged,
                           boolean ThicknessChaged, boolean PatternChanged) {
        //System.out.println("View: updateView");
        this.lineColor.setValue(model.getLineColor());
        this.fillColor.setValue(model.getFillColor());
        if(Math.abs(model.getLinePattern() - 1) < 0.1){
            this.pattern1.setSelected(true);
        } else if (Math.abs(model.getLinePattern() - 2) < 0.1){
            this.pattern2.setSelected(true);
        } else {
            this.pattern3.setSelected(true);
        }
        if(Math.abs(model.getLineThickness() - 3) < 0.1){
            this.thick.setSelected(true);
        } else if (Math.abs(model.getLineThickness() - 2) < 0.1){
            this.mid.setSelected(true);
        } else {
            this.thin.setSelected(true);
        }
    }

    private class StandardButton extends Button {
        StandardButton() {
            this("");
        }

        StandardButton(String caption) {
            super(caption);
            // setText(caption); // call to super class already does this
            setVisible(true);
            setPrefHeight(50);
            setPrefWidth(105);

        }
    }
}
