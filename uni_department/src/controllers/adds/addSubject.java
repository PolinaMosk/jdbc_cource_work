package controllers.adds;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Main;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class addSubject {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField name;

    private String fio;
    private Connection connection;
    private Main app;


    public void confirm(javafx.scene.input.MouseEvent mouseEvent) {
        if (!name.getText().equals("")){
            String query = "{call add_subject(?)}";
            CallableStatement statement = null;
            try {
                statement = connection.prepareCall(query);
                statement.setString(1, name.getText());
                statement.execute();
                app.openMainWindowAdmin(4);
            } catch (SQLException ex) {
                this.app.openErrorWindow(ex.getMessage(), 1);
            }
        } else {
            app.openErrorWindow("Заполните все поля", 1);
        }
    }

    public void setAll(String fio, Connection c, Main main){
        this.fio = fio;
        this.connection = c;
        this.app = main;
    }
}
