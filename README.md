--------------CSC325 Module 06 Firebase Project-------------:
This project is a JavaFX application connected to Firebase. It includes a splash screen, Firebase Firestore read/write, Firebase Authentication, a styled JavaFX GUI, menu items, a TableView, and a profile picture selection feature.

-----------Features------------:
- Splash screen when the app starts
- JavaFX GUI styled with CSS
- Menu bar with File, Edit, Theme, and Help menus
- Firebase Firestore connection
- Write student data to Firebase
- Read Firebase data into a TableView
- Register users with Firebase Authentication
- Sign in check using Firebase Authentication
- Choose and display a local profile picture

------------How to Run----------------:
Open the project in IntelliJ IDEA.
Make sure the project uses JDK 17 or higher.
Run the project using Maven:
Maven - Plugins - javafx - javafx:run
Do not run the Java file directly, because JavaFX runtime components may be missing. The JavaFX dependencies are handled through Maven in the pom.xml file.

------Firebase Key Setup---------------:
This project requires a Firebase service account key file named: key.json
For security reasons, key.json is not included in this repository.
To run the project, create or use a Firebase project with Firestore and Authentication enabled. Then download the Firebase service account key, rename it to key.json, and place it here:
src/main/resources/files/key.json

------Firebase Storage Note---------------:
Firebase Storage required a billing setup, so the picture feature allows the user to choose and display a local profile picture instead of uploading it to Firebase Storage.
