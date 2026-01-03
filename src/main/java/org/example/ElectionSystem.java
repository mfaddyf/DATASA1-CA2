package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ElectionSystem extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ElectionSystem.class.getResource("election_system.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Election Management System!");
        stage.setScene(scene);
        stage.show();
    }
}

// CLASSES TAKEN FROM PREVIOUS PROJECTS
// MLinkedList - Madison DataSA1-CA1
// Node - Madison DataSA1-CA1

// REFERENCES FOR WEBSITES I USED TO RESEARCH SOME THINGS
//  https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Spinner.html
//  https://openjfx.io/javadoc/25/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html
//  https://www.w3schools.com/java/ref_string_trim.asp
//
// FOR SORTING
//  https://www.w3schools.com/java/java_advanced_sorting.asp
//  https://www.geeksforgeeks.org/java/interfaces-in-java/