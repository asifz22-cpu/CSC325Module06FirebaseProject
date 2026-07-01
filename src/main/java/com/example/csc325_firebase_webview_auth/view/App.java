package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Scene scene;

    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage stage) throws Exception {

        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();

        VBox splash = new VBox(15);
        splash.setAlignment(Pos.CENTER);
        splash.setStyle("-fx-background-color: #bf7f2d;");

        Label title = new Label("CSC325 Firebase Project");
        title.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");

        Label loading = new Label("Loading...");
        loading.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        ProgressIndicator circle = new ProgressIndicator();

        splash.getChildren().addAll(title, circle, loading);

        scene = new Scene(splash, 900, 550);

        stage.setTitle("CSC325 Module 06 Project");
        stage.setScene(scene);
        stage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        pause.setOnFinished(e -> {
            try {
                scene.setRoot(loadFXML("/files/AccessFBView.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pause.play();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}