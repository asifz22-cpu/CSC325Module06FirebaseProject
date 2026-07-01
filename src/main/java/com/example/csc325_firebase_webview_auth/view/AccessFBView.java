package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class AccessFBView {

    @FXML
    private TextField nameField;

    @FXML
    private TextField majorField;

    @FXML
    private TextField ageField;

    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, String> nameColumn;

    @FXML
    private TableColumn<Person, String> majorColumn;

    @FXML
    private TableColumn<Person, Integer> ageColumn;
    @FXML
    private ImageView profileImage;

    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        majorColumn.setCellValueFactory(new PropertyValueFactory<>("major"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        tableView.setItems(listOfUsers);
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

    @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

    @FXML
    private void clearRecord(ActionEvent event) {
        clearBoxes();
    }

    @FXML
    private void regRecord(ActionEvent event) {
        showRegisterForm();
    }

    @FXML
    private void signInRecord(ActionEvent event) {
        showSignInForm();
    }

    public void addData() {
        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", Integer.parseInt(ageField.getText()));

        ApiFuture<WriteResult> result = docRef.set(data);

        Person person = new Person(
                nameField.getText(),
                majorField.getText(),
                Integer.parseInt(ageField.getText())
        );

        listOfUsers.add(person);
        clearBoxes();
    }

    public boolean readFirebase() {
        listOfUsers.clear();

        ApiFuture<QuerySnapshot> future = App.fstore.collection("References").get();

        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                String name = String.valueOf(document.getData().get("Name"));
                String major = String.valueOf(document.getData().get("Major"));
                int age = Integer.parseInt(String.valueOf(document.getData().get("Age")));

                Person person = new Person(name, major, age);
                listOfUsers.add(person);
            }

            return true;

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void clearBoxes() {
        nameField.clear();
        majorField.clear();
        ageField.clear();
    }

    public void showRegisterForm() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Register User");

        TextField emailBox = new TextField();
        emailBox.setPromptText("Email");

        PasswordField passwordBox = new PasswordField();
        passwordBox.setPromptText("Password");

        TextField nameBox = new TextField();
        nameBox.setPromptText("Name");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailBox, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordBox, 1, 1);

        grid.add(new Label("Name:"), 0, 2);
        grid.add(nameBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> answer = dialog.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            registerUser(emailBox.getText(), passwordBox.getText(), nameBox.getText());
        }
    }

    public void showSignInForm() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Sign In");

        TextField emailBox = new TextField();
        emailBox.setPromptText("Email");

        PasswordField passwordBox = new PasswordField();
        passwordBox.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailBox, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordBox, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> answer = dialog.showAndWait();

        if (answer.isPresent() && answer.get() == ButtonType.OK) {
            signInUser(emailBox.getText());
        }
    }

    public boolean registerUser(String email, String password, String name) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(name)
                .setDisabled(false);

        try {
            UserRecord userRecord = App.fauth.createUser(request);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registered");
            alert.setHeaderText(null);
            alert.setContentText("User created: " + userRecord.getEmail());
            alert.show();

            return true;

        } catch (FirebaseAuthException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register Error");
            alert.setHeaderText(null);
            alert.setContentText("User was not created.");
            alert.show();

            return false;
        }
    }

    public boolean signInUser(String email) {
        try {
            UserRecord userRecord = App.fauth.getUserByEmail(email);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign In");
            alert.setHeaderText(null);
            alert.setContentText("Signed in as: " + userRecord.getEmail());
            alert.show();

            return true;

        } catch (FirebaseAuthException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sign In Error");
            alert.setHeaderText(null);
            alert.setContentText("User was not found.");
            alert.show();

            return false;
        }
    }
    @FXML
    private void choosePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(profileImage.getScene().getWindow());

        if (file != null) {
            Image image = new Image(file.toURI().toString());
            profileImage.setImage(image);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Picture Selected");
            alert.setHeaderText(null);
            alert.setContentText("Picture selected. Firebase Storage needs billing, so this shows it locally.");
            alert.show();
        }
    }
}