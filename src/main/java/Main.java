
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.*;

import java.io.*;
import java.util.ArrayList;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import java.util.Optional;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        // create and initialize the Model to hold our counter
        Model model = new Model();
        final FileChooser fileChooser = new FileChooser();
        // create and register views
        View1 view1 = new View1(model);
        View2 view2 = new View2(model);
        stage.setTitle("SketchIt");
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setMaxWidth(1200);
        stage.setMaxHeight(800);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400, 100);
        ArrayList<Shape> shapes = new ArrayList<>();




        //************Top part of the SKetchIt (menu bar)***************//
        MenuBar menuBar = new MenuBar();
        HBox toparea = new HBox(menuBar);

        Menu File = new Menu("File");
        MenuItem New = new MenuItem("New");
        MenuItem Load = new MenuItem("Load");
        MenuItem Save = new MenuItem("Save");
        MenuItem Quit = new MenuItem("Quit");
        Quit.setOnAction(e -> {
            final Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            //Setting the title
            dialog.setTitle("Quit");
            dialog.setContentText("Are you sure to leave this application?");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                System.exit(0);
            }
        });

        New.setOnAction(e -> {
            final Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            //Setting the title
            dialog.setTitle("New");
            dialog.setContentText("You will create a new blank drawing.\n"
                    + "Do you want to save the current work?");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                save(view2.saveWork());
                view2.clearCanvas();
            } else{
                view2.clearCanvas();
            }
        });

        Save.setOnAction(e -> {
            save(view2.saveWork());
        });

        Load.setOnAction(e -> {
            final Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
            //Setting the title
            dialog.setTitle("Load");
            dialog.setContentText("You will load a drawing.\n"
                    + "Do you want to save the current work?");
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                save(view2.saveWork());
            } else{
                view2.clearCanvas();
            }
            // loading the file
            File file = fileChooser.showOpenDialog(stage);
            ArrayList<String[]> loadValue = load(file);
            //TODO: draw the file
            view2.loadWork(loadValue);
        });


        File.getItems().add(New);
        File.getItems().add(Load);
        File.getItems().add(Save);
        File.getItems().add(Quit);
        menuBar.getMenus().add(File);
        // menu: help
        Menu Help = new Menu("Help");
        MenuItem About = new MenuItem("About");
        Help.getItems().add(About);
        About.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setContentText("Program Name: SketchIt\n" +
                    "Student Name: Jiaxin Lei\n" +
                    "WatID: j36lei");
            alert.showAndWait();

        });
        menuBar.getMenus().add(Help);
        //***********************************************************//


        scene.setOnKeyPressed(event -> {
            if (model.getToolType() == 1){
                if (event.getCode().equals(KeyCode.ESCAPE) && model.isSelecting()) {
                    view2.unselect();
                } else if (event.getCode().equals(KeyCode.DELETE) && model.isSelecting()) { // delete the curitem
                    view2.deleteitem();
                }
            }
        });

        root.setTop(menuBar);
        root.setRight(view2);
        root.setLeft(view1);

        stage.setScene(scene);
        stage.show();
    }

    // Customized button
    // Used to set default values for all buttons
    // Lets us manipulate MIN, MAX, PREFERRED sizes in one place for all demos
    private class StandardButton extends Button {
        StandardButton() {
            this("Untitled");
        }

        StandardButton(String caption) {
            super(caption);
            // setText(caption); // call to super class already does this
            setVisible(true);
            setPrefHeight(50);
            setPrefWidth(105);

        }
    }

    // NOTE: this part of code is from sample code in class
    private ArrayList<String[]> load(File myfile) {
        FileReader file = null;
        BufferedReader reader = null;
        ArrayList<String[]> values = new ArrayList<String[]>();

        // open input file
        try {
            file = new FileReader(myfile.getName());
            reader = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // read and process lines one at a time
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                // DELIMITER separates values on a row
                values.add(line.split(" "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    // NOTE: this part of code is from sample code in class
    private void save(String work) {
        FileWriter file = null;
        BufferedWriter writer = null;

        try {
            file = new FileWriter("SketchIt_saved_work.txt");
            writer = new BufferedWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // one line is written for each row of data
            writer.write(
                    work
            );

            writer.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



