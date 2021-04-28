

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
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
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
//import java.awt.*;
import javafx.scene.shape.*;
import java.util.ArrayList;
import javafx.scene.Group;
import java.awt.event.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;

class View2 extends Pane implements IView {

    private Model model; // reference to the model
    private Rectangle backGround = new Rectangle(990,800);
    ArrayList<Shape> shapes = new ArrayList<>();
    private Shape curShape = backGround;
    double curShape_thickness = 1;
    //private Canvas canvas = new Canvas();
    private Pane pane = new Pane();
    private Group root = new Group(pane); ///////////
    private Scene scene = new Scene(root, 1200, 800, Color.LIGHTPINK);
    private double startX;
    private double startY;
    //private boolean selecting = false;


    View2(Model model) {
        super();
        // keep track of the model
        this.model = model;
        this.backGround.setFill(Color.WHITE);
        // add background to the pane
        pane.getChildren().add(backGround);
        this.getChildren().add(pane);
        this.registerControllers();
        // register with the model when we're ready to start receiving data
        this.model.addView(this);
    }

    private void registerControllers() {
        pane.setOnMousePressed(mouseEvent -> {
            if (this.model.getToolType() == 1 && mouseEvent.getX() > 0){
                //System.out.println("now it is good to select");
                //boolean pressNothing = true;
                for (Shape item : shapes) {
                    if (mouseEvent.getTarget().toString().equals(item.toString())) {
                        this.model.setSelecting(true);
                        //selecting = true;
                        if(!item.equals(curShape)){
                            // when we change the curshape
                            curShape.setStrokeWidth(curShape_thickness);
                        }
                        if(!curShape.equals(item)){
                            // make sure when we select the same object twice, its linethickness doesn't change
                            curShape_thickness = item.getStrokeWidth();
                        }
                        this.curShape = item; // update the curitem
                        if(!curShape.toString().contains("Line")) {
                            this.model.changeFillColor(Color.valueOf(item.getFill().toString()));
                        }
                        this.model.changeLineColor(Color.valueOf(item.getStroke().toString()));
                        this.model.changeLineThickness(item.getStrokeWidth());
                        if(Math.abs(item.getStrokeDashArray().get(0) - 1) < 0.1){
                            this.model.changeLinePattern(1.0);
                        } else if(Math.abs(item.getStrokeDashArray().get(0) - 4) < 0.1){
                            this.model.changeLinePattern(2.0);
                        } else{
                            this.model.changeLinePattern(3.0);
                        }
                        //this.model.changeLinePattern(item.getStrokeDashOffset());
                        item.setStrokeWidth(5);
                        break;
                    } else{
                        //System.out.println(mouseEvent.getX());
                        //selecting = false;
                        this.model.setSelecting(false);
                        //curShape = backGround;
                    }
                }
                // if we cancel selecting
                if (this.model.isSelecting() == false && !curShape.equals(backGround)) {
                    curShape.setStrokeWidth(curShape_thickness);
                    curShape = backGround;
                }
            } else if(this.model.getToolType() == 2){
                //System.out.println("now try to use eraser to delete the item");

                //System.out.println("target name "+mouseEvent.getTarget().toString());
                //System.out.println("before deleting");
                //for(Shape item:shapes){
                    //System.out.println(item.toString());
                //}
                if(!mouseEvent.getTarget().equals(backGround)) {
                    pane.getChildren().remove(mouseEvent.getTarget());
                }
                for(Shape item: shapes){
                    if(item.toString().equals(mouseEvent.getTarget().toString())){
                        shapes.remove(item);
                        break;
                    }
                }
                //System.out.println("after deleting");
                //for(Shape item:shapes){
                    //System.out.println(item.toString());
                //}

            } else if(this.model.getToolType() == 3){ // draw Line
                //System.out.println("now it's line drawing in view2, key pressed");
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
                Line newLine = new Line();
                pane.getChildren().add(newLine);
                shapes.add(newLine);
            } else if(this.model.getToolType() == 4){ // draw a circle
                //System.out.println("now it's line drawing in view2, key pressed");
                this.model.setSelecting(false);
                //System.out.println(curShape.toString());
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
                //System.out.println("start X:" + startX);
                //System.out.println("start Y:" + startY);
                Circle newCircle = new Circle(startX,startY,0);
                pane.getChildren().add(newCircle);
                shapes.add(newCircle);
            } else if(this.model.getToolType() == 5){ // draw a rec
                //System.out.println("now it's line drawing in view2, key pressed");
                startX = mouseEvent.getX();
                startY = mouseEvent.getY();
                Rectangle newRec = new Rectangle();
                pane.getChildren().add(newRec);
                shapes.add(newRec);
            } else if(this.model.getToolType() == 6){ // try to fill TODO
                for(Shape item: shapes){
                    if(item.toString().equals(mouseEvent.getTarget().toString()) ){
                        item.setFill(model.getFillColor());
                        break;
                    }
                }
            }
        });


        this.pane.setOnMouseDragged(mouseEvent -> {
            if (this.model.getToolType() == 3 ) {
                //System.out.println("now it's line drawing in view2, mouse dragged");

                if(shapes.isEmpty() == false) {
                    shapes.remove(shapes.size() - 1);
                }

                pane.getChildren().remove(pane.getChildren().size() - 1);

                Line newLine = new Line(startX,startY,mouseEvent.getX(),mouseEvent.getY());
                newLine.setStroke(model.getLineColor());
                if (Math.abs(model.getLinePattern() - 1) < 0.1) {
                    newLine.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(model.getLinePattern() - 2) < 0.1){
                    newLine.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    newLine.getStrokeDashArray().addAll(9.0,9.0);
                }
                newLine.setStrokeWidth(model.getLineThickness());
                pane.getChildren().add(newLine);
                shapes.add(newLine);
            } else if (this.model.getToolType() == 4) {
               // System.out.println("now it's line drawing in view2, mouse dragged trying to draw circle");
                pane.getChildren().remove(pane.getChildren().size() - 1);
                if (shapes.isEmpty() == false) {
                    shapes.remove(shapes.size() - 1);
                }

                double deltax = mouseEvent.getX() - startX;
                double deltay = mouseEvent.getY() - startY;
                double r = Math.sqrt(deltax * deltax + deltay * deltay);
                Circle newCircle = new Circle(startX, startY, r);

                if (Math.abs(model.getLinePattern() - 1) < 0.1) {
                    newCircle.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(model.getLinePattern() - 2) < 0.1){
                    newCircle.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    newCircle.getStrokeDashArray().addAll(9.0,9.0);
                }

                newCircle.setStrokeWidth(model.getLineThickness());
                newCircle.setFill(model.getFillColor());
                newCircle.setStroke(model.getLineColor());
                shapes.add(newCircle);
                pane.getChildren().add(newCircle);
            } else if (this.model.getToolType() == 5) {
                //System.out.println("now it's line drawing in view2, mouse dragged trying to draw circle");
                pane.getChildren().remove(pane.getChildren().size() - 1);
                if(shapes.isEmpty() == false){
                    shapes.remove(shapes.size() - 1);
                }
                Rectangle newRec = new Rectangle();
                newRec.setX(startX);
                newRec.setY(startY);
                double deltax = Math.abs(mouseEvent.getX() - startX);
                double deltay = Math.abs(mouseEvent.getY() - startY);
                newRec.setWidth(deltax);
                newRec.setHeight(deltay);
                if (Math.abs(model.getLinePattern() - 1) < 0.1) {
                    newRec.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(model.getLinePattern() - 2) < 0.1){
                    newRec.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    newRec.getStrokeDashArray().addAll(9.0,9.0);
                }
                newRec.setStrokeWidth(model.getLineThickness());
                newRec.setFill(model.getFillColor());
                newRec.setStroke(model.getLineColor());
                pane.getChildren().add(newRec);
                shapes.add(newRec);
            } else if (this.model.getToolType() == 1 && this.model.isSelecting()) {
                if(!curShape.equals(backGround)) {
                    curShape.relocate(mouseEvent.getX(), mouseEvent.getY());
                }
            }
        });


    }

    // When notified by the model that things have changed,
    // update to display the new value

    public void clearCanvas(){
        this.pane.getChildren().clear();
        this.pane.getChildren().add(backGround);
        this.shapes.clear();
    }

    int getPatternInfo(Shape shape){
        if(Math.abs(shape.getStrokeDashArray().get(0) - 1) < 0.1){
            return 1;
        } else if(Math.abs(shape.getStrokeDashArray().get(0) - 2) < 0.1){
            return 2;
        } else {
            return 3;
        }
    }

    private String getInfoLine(Line line){
        String info = "Line ";
        int pattern = getPatternInfo(line);
        info = info + line.getStartX() + " " + line.getEndX() + " " +
                line.getStartY() + " " + line.getEndY() + " " +
                line.getStroke().toString() + " " + line.getStrokeWidth() + " " + pattern;
        return info;
    }

    private String getInfoCir(Circle cir){
        String info = "Circle ";
        int pattern = getPatternInfo(cir);
        info = info + cir.getCenterX() + " " + cir.getCenterY() + " " +
                cir.getRadius() + " " + cir.getFill().toString() + " " + cir.getStroke().toString() + " "
                + cir.getStrokeWidth() + " " + pattern;
        return info;
    }

    private String getInfoRec(Rectangle rec){
        String info = "Rectangle ";
        int pattern = getPatternInfo(rec);
        info = info + rec.getX() + " " + rec.getY() + " " + rec.getWidth() + " " +
                rec.getHeight() + " " +   rec.getFill().toString() + " "
                + rec.getStroke().toString() +  " " + rec.getStrokeWidth() + " " + pattern;
        return info;
    }

    public String saveWork(){
        String work = "";
        if(pane.getChildren().size() == 1){
            return work;
        }
        Node node = pane.getChildren().get(1);
        if (node instanceof Line) {
            work = getInfoLine(((Line) node));
        } else if(node instanceof Circle){
            work = getInfoCir(((Circle) node));
        } else if(node instanceof Rectangle){
            work = getInfoRec(((Rectangle) node));
        }

        //System.out.println(work);

        for(int i = 2; i < pane.getChildren().size(); i++){
            String tmp = "";
            node = pane.getChildren().get(i);
            if (node instanceof Line) {
                tmp = getInfoLine(((Line) node));
            } else if(node instanceof Circle){
                tmp = getInfoCir(((Circle) node));
            } else if(node instanceof Rectangle){
                tmp = getInfoRec(((Rectangle) node));
            }

            //System.out.println(tmp);
            work = work + "\n" + tmp;
        }
        return work;
    }

    public void loadWork(ArrayList<String[]> works){
        for(String[] item : works){
            if(item[0].equals("Line")){
                double startx = Double.parseDouble(item[1]);
                double endx = Double.parseDouble(item[2]);
                double starty = Double.parseDouble(item[3]);
                double endy = Double.parseDouble(item[4]);
                String LineColorName = item[5];
                double width = Double.parseDouble(item[6]);
                double pat = Double.parseDouble(item[7]);
                Line newLine = new Line();
                newLine.setStartX(startx);
                newLine.setStartY(starty);
                newLine.setEndX(endx);
                newLine.setEndY(endy);
                newLine.setStroke(Paint.valueOf(LineColorName));
                newLine.setStrokeWidth(width);
                if (Math.abs(pat - 1) < 0.1) {
                    newLine.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(pat - 2) < 0.1){
                    newLine.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    newLine.getStrokeDashArray().addAll(9.0,9.0);
                }
                pane.getChildren().add(newLine);
                shapes.add(newLine);
            } else if(item[0].equals("Circle")){
                double x = Double.parseDouble(item[1]);
                double y = Double.parseDouble(item[2]);
                double r = Double.parseDouble(item[3]);
                String FillColorName = item[4];
                String LineColorName = item[5];
                double width = Double.parseDouble(item[6]);
                double pat = Double.parseDouble(item[7]);
                Circle cir = new Circle();
                cir.setCenterX(x);
                cir.setCenterY(y);
                cir.setRadius(r);
                cir.setFill(Paint.valueOf(FillColorName));
                cir.setStroke(Paint.valueOf(LineColorName));
                cir.setStrokeWidth(width);
                if (Math.abs(pat - 1) < 0.1) {
                    cir.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(pat - 2) < 0.1){
                    cir.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    cir.getStrokeDashArray().addAll(9.0,9.0);
                }
                pane.getChildren().add(cir);
                shapes.add(cir);

            } else if(item[0].equals("Rectangle")){
                double x = Double.parseDouble(item[1]);
                double y = Double.parseDouble(item[2]);
                double width = Double.parseDouble(item[3]);
                double height = Double.parseDouble(item[4]);
                String FillColorName = item[5];
                String LineColorName = item[6];
                double strokewidth = Double.parseDouble(item[7]);
                double pat = Double.parseDouble(item[8]);
                Rectangle rec = new Rectangle();
                rec.setX(x);
                rec.setY(y);
                rec.setWidth(width);
                rec.setHeight(height);
                rec.setFill(Paint.valueOf(FillColorName));
                rec.setStroke(Paint.valueOf(LineColorName));
                rec.setStrokeWidth(strokewidth);
                if (Math.abs(pat - 1) < 0.1) {
                    rec.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(pat - 2) < 0.1){
                    rec.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    rec.getStrokeDashArray().addAll(9.0,9.0);
                }
                pane.getChildren().add(rec);
                shapes.add(rec);

            }
        }
    }

    public void unselect(){
        curShape.setStrokeWidth(curShape_thickness);
        curShape = backGround;
        this.model.setSelecting(false);
    }

    public void deleteitem(){
        for(Shape item: shapes){
            if(item.toString().equals(curShape.toString())){
                shapes.remove(item);
                break;
            }
        }

        if(!curShape.equals(backGround)) {
            pane.getChildren().remove(curShape);
        }
    }



    public void updateView(boolean LineColorChanged, boolean FillColorChanged,
                           boolean ThicknessChanged, boolean PatternChanged) {
        //System.out.println("View2: updateView");
        //System.out.println(FillColorChanged);
        if(this.model.getToolType()!=1){
            curShape = backGround;
        }
        if(this.model.isSelecting() && curShape.equals(backGround) == false){
            if(LineColorChanged){
                curShape.setStroke(this.model.getLineColor());
                this.model.setLineColorChanged(false);
            } else if(FillColorChanged){
                //System.out.println("haha");
                curShape.setFill(this.model.getFillColor());
                this.model.setFillColorChanged(false);
            } else if(ThicknessChanged){
                curShape.setStrokeWidth(this.model.getLineThickness());
                this.model.setFillColorChanged(false);
            }else if(PatternChanged){
                if (Math.abs(model.getLinePattern() - 1) < 0.1) {
                    curShape.getStrokeDashArray().remove(0,1);
                    curShape.getStrokeDashArray().addAll(1.0, 1.0);
                } else if(Math.abs(model.getLinePattern() - 2) < 0.1){
                    curShape.getStrokeDashArray().remove(0,1);
                    curShape.getStrokeDashArray().addAll(4.0, 4.0);
                } else{
                    curShape.getStrokeDashArray().remove(0,1);
                    curShape.getStrokeDashArray().addAll(9.0,9.0);
                }
                this.model.setPatternChanged(false);
            }
        }
    }
}


