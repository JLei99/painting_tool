import java.awt.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Model {
    // the data in the model, just a counter
    private Color LineC = Color.BLACK;
    private Color FillC = Color.LIGHTPINK;
    private int toolType = 1; // type from 1 to 6
    private double linePattern = 3.0;
    private double lineThickness = 3.0;
    private boolean selecting = false;
    private boolean LineColorChanged = false;
    private boolean FillColorChanged = false;
    private boolean ThicknessChanged = false;
    private boolean PatternChanged = false;



    // all views of this model
    private ArrayList<IView> views = new ArrayList<IView>();

    // method that the views can use to register themselves with the Model
    // once added, they are told to update and get state from the Model
    public void addView(IView view) {
        views.add(view);
        view.updateView(LineColorChanged, FillColorChanged, ThicknessChanged, PatternChanged);
    }

    // simple accessor method to fetch the current state
    public Color getLineColor() {
        return LineC;
    }

    public Color getFillColor() {
        return FillC;
    }

    public int getToolType(){ return toolType; }

    public double getLinePattern(){ return linePattern; }

    public double getLineThickness(){ return lineThickness; }

    public boolean isSelecting(){return selecting;}

    // method that the Controller uses to tell the Model to change state
    // in a larger application there would probably be multiple entry points like this
    public void changeLineColor(Color newColor) {
        LineC = newColor;
        //System.out.println("Model: increment counter to ");
        notifyObservers();
    }

    public void setSelecting(boolean newSelecting){
        this.selecting = newSelecting;
    }

    public void setLineColorChanged(boolean change){
        this.LineColorChanged = change;
    }

    public void setFillColorChanged(boolean change){
        this.FillColorChanged = change;
    }

    public boolean getFillColorChanged(){
        return FillColorChanged;
    }

    public void setThicknessChaged(boolean change){
        this.ThicknessChanged = change;
    }
    public void setPatternChanged(boolean change){
        this.PatternChanged = change;
    }

    public void changeFillColor(Color newColor) {
        FillC = newColor;
        //System.out.println("Model: increment counter to ");
        notifyObservers();
    }

    public void changeTool(int newTool) {
        toolType = newTool;
        //System.out.println("Model:change tool to select ");
        notifyObservers();
    }

    public void changeLinePattern(double pattern) {
        linePattern = pattern;
        //System.out.println("Model: increment counter to ");
        notifyObservers();
    }

    public void changeLineThickness(double thickness) {
        lineThickness = thickness;
        //System.out.println("Model: increment counter to");
        notifyObservers();
    }

    // the model uses this method to notify all of the Views that the data has changed
    // the expectation is that the Views will refresh themselves to display new data when appropriate
    private void notifyObservers() {
        for (IView view : this.views) {
            //System.out.println("Model: notify View");
            view.updateView(LineColorChanged, FillColorChanged, ThicknessChanged, PatternChanged);
        }
    }
}
