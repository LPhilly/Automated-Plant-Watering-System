package sample;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import sample.SerialPortService;
import sample.DataController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import java.io.IOException;





public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        var controller = new DataController();
        var sp = SerialPortService.getSerialPort("/dev/cu.usbserial-0001");
        var outputStream = sp.getOutputStream();
        sp.addDataListener(controller);

        var pane = new BorderPane();

        var button = new Button("Manual Override");
        button.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");
        button.setOnMousePressed(value ->
        {
            try {outputStream.write(255);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
        button.setOnMouseReleased(value ->
        {
            try {outputStream.write(0);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Obtain Input Value From Arduino
       var value = controller.GetValue();
        //System.out.println(value);


        if ( value == 107 || value == 108 || value == 109 || value == 110||value == 111) {
            Stage stage = new Stage ();
            stage.setMinWidth(250);
            Color colour = null;

            //Send 250 Value To Arduino
            try {outputStream.write(250);}
            catch (IOException e) {e.printStackTrace();}

            //Initialize Tooltip
            var tips = new Tooltip();

            //Initialize Label
            var label2 = new Label();
            String text = null;

            //Iterate through values using conditonal.
            if (value == 107)
            {  text = " ERROR 107: Enviorment is too humid for plant.";
                colour =  Color.rgb(0, 204, 204);
            tips.setText("Try opening a window or using a dehumidifier in order to lower the humidity in the enviorment");}

            else if (value == 108)
            { text = "ERROR 108: Temperature is too cold for the plant. Please move plant to sutable temperature";
                 colour =  Color.rgb(145, 158, 208);
            tips.setText("If it's warm out, open a window. If not, try turning up the heat in order to warm the envorment");}

            else if (value == 110)
            {text = " ERROR 110: Temperature is too warm for the plant. Please ensure plant is in a sutible temperature";
                colour =  Color.rgb(255, 161, 0);
                tips.setText("If it's not too cold, open a window. If not, try turning on the air conditioning.");
            }

            else if (value == 109)
            { text = "ERROR 109: Not enough light for the plant is unhealthy";
                colour =  Color.rgb(46, 26, 71);
            tips.setText("Use a grow light or place the plant in direct sunlight");}

            else if  (value == 111)
            { text = "ERROR 111: Plant enviorment is too arid";
            colour = Color.rgb(186, 148, 60);
            tips.setText("Adding humidity through a humidifer or by spraying water into the air will benefit the plant.");}

            //Label
            label2.setText(text);
            label2.setFont(Font.font("-fx-font: normal bold"));

            // Button
            var close = new Button ("Close");
            close.setTooltip(tips);
            close.setOnAction( ( e -> stage.close()));
            VBox window = new VBox(10);
            label2.setTextFill(colour);

            // Set Styles Obtained from... https://www.tutorialspoint.com/javafx/javafx_css.htm
            close.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");
            window.setStyle("-fx-background-color: DIMGRAY");

            // Initialize Pop-Up
            window.getChildren().addAll(label2, close);
            var scene = new Scene (window);
             stage.setScene(scene);
            stage.setTitle("A Wild Error Appears");
            stage.showAndWait();




        }
        else {
            //Create Password Pop-Up
            Stage stage = new Stage ();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Password Entry");
            var password = new PasswordField();
            var bottone = new Button("Password");
            var label = new Label();

            //Style Button
            bottone.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");

            //Give Button Close Functionality
            bottone.setOnAction(action -> {
            var pass = password.getText();
            if (pass.equals("a"))
            { stage.close();
              

                // Live Graph With Close Button and Manual Watering
                var now = System.currentTimeMillis();

                //Style new button
                var close = new Button("Close Program");
                close.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");

                var xAxis = new NumberAxis("time (ms since Jan 1, 1970)", now, now + 50000, 15000); // creates the x-axis (which automatically updates)
                var yAxis = new NumberAxis("Sensor Output", 0, 1000, 10); // creates the y-axis



                var series = new XYChart.Series<>(controller.getDataPoints()); // creates the series (all the data)
                var lineChart = new LineChart<>(xAxis, yAxis, FXCollections.singletonObservableList(series)); // creates the chart
                lineChart.setTitle("Moisture Sensor Monitoring");




                // What a **pane** (I think that's quite **punny**)
                pane.setBottom(button);
                pane.setCenter(lineChart);
                pane.setLeft(close);
                pane.setPadding(new Insets(0, 20, 0, 20));
                pane.setStyle("-fx-background-color: LIGHTGRAY;");
                Scene Scene = new Scene(pane, 1000, 1000);
                primaryStage.setScene(Scene);
                primaryStage.show();


                // Give Close Button Functionality
                close.setOnAction(e -> {primaryStage.close();

                // Summary Sheet Pop-Up. Reobtain final moisture reading
                    var val = controller.GetValue();
                var Stage_2 = new Stage();
                Stage_2.setTitle("Summary");
                var close_again = new Button("Close");
                var another_label = new Label("Final Sensor Value:" + " " + val);
                Label another_label2 = new Label();
                var another_label3 = new Label("Light, temp and humidity are just right");

                // Iterate Through Two Possibilities: Graph Ends with dry reading or moist reading
                if (val > 660) { another_label2.setText("The soil is dry");}
                else { another_label2.setText("Soil is moist");

                //Send 240 To Play A Celebratory Song
                try {outputStream.write(240);
                    }
                    catch (IOException f) {
                        f.printStackTrace();
                    }}
               close_again.setOnAction(w -> {Stage_2.close();});


                // Method for putting image together gotten from: http://tutorials.jenkov.com/javafx/imageview.html
                    FileInputStream input = null;
                    try {
                        input = new FileInputStream("/Users/l_filippelli/Downloads/Goldielocks-1.png");
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    var image = new Image(input);
                var view = new ImageView(image);

                    VBox box = new VBox(another_label, another_label2, another_label3, view, close_again);


                //Style; Background File Obtained From: https://www.tutorialspoint.com/javafx/javafx_css.htm

                    box.setStyle("-fx-background-color: DIMGRAY");
                    close_again.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");
                    another_label.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
                    another_label.setTextFill(Color.WHITESMOKE);
                    another_label2.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
                    another_label2.setTextFill(Color.WHITESMOKE);
                    another_label3.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
                    another_label3.setTextFill(Color.WHITESMOKE);

               var SecondaryScene = new Scene(box, 265,200);
               Stage_2.setScene(SecondaryScene);
               Stage_2.show();
                });



            }
            else {
                // Set Password Failure
                label.setText("Incorrect Password. You entered:" + password.getText());
                label.setFont(Font.font("Helvetica", FontWeight.BOLD, 15));
                label.setTextFill(Color.DARKRED);
            }
            });
           HBox window = new HBox (bottone, password, label);
           var scene = new Scene(window, 600, 35);

           //Background File Obtained From: https://www.tutorialspoint.com/javafx/javafx_css.htm
            window.setStyle("-fx-background-color: LIGHTGRAY");
           stage.setScene(scene);
           stage.showAndWait();

}}}
