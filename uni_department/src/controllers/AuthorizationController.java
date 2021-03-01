package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;
import main.db.SQLDatabaseConnection;


public class AuthorizationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button confirm_auth;

    @FXML
    private TextField login_input;

    @FXML
    private TextField fio_input;

    @FXML
    private PasswordField password_input;

    private Connection connection;
    private Main app;
    private String fio;


    @FXML
    void initialize() {
    }

    public String getFio() {
        return fio;
    }

    public void setMainApp(Main app){
        this.app = app;
    }

    public void confirm(MouseEvent mouseEvent) {
        SQLDatabaseConnection DBConnection = new SQLDatabaseConnection();
        try {
            DBConnection.connect(login_input.getText(), password_input.getText(), this.app);
            if (DBConnection.getRole() == 2) {app.openMainWindowTeacher();} else
            if (DBConnection.getRole() == 3) {app.openMainWindowStudent();} else
            if (DBConnection.getRole() == 1) {app.openMainWindowAdmin(0);}
        } catch (Exception ex){
            app.openErrorWindow(ex.getMessage(), 4);
        }
    }
}
