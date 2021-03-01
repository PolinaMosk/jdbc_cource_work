package controllers.adds;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;

public class addTask {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField deadline;

    @FXML
    private ChoiceBox<String> subjects;

    @FXML
    private TextField type_control;
    private String fio;
    private Connection connection;
    private Main app;


    public void confirm(MouseEvent mouseEvent) {
        if (subjects.getValue() == null || deadline.getText().equals("") || type_control.getText().equals("")){
            app.openErrorWindow("Заполните все поля", 1);
        } else {
            String query = "{call add_task(?, ?, ?)}";
            CallableStatement statement = null;
            try {
                statement = connection.prepareCall(query);
                statement.setString(1, subjects.getValue());
                statement.setString(2, deadline.getText());
                statement.setString(3, type_control.getText());
                statement.execute();
                app.openMainWindowAdmin(0);
            } catch (SQLException ex) {
                this.app.openErrorWindow(ex.getMessage(), 1);
            }
        }
    }

    public void setAll(String fio, Connection c, Main main){
        this.fio = fio;
        this.connection = c;
        this.app = main;
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT name FROM subjects";
            ResultSet rs = statement.executeQuery(selectSql);
            ObservableList<String> list = FXCollections.observableArrayList();
            while (rs.next()) list.add(rs.getString(1));
            subjects.setItems(list);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }
}
