package controllers.adds;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddProgressController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ready;

    @FXML
    private ChoiceBox<String> subject_name;

    @FXML
    private ChoiceBox<String> type_control;

    private Main app;
    private Connection connection;
    private String fio_user;

    @FXML
    void initialize() {
    }

    public void setMainApp(Main app){
        this.app = app;
        this.connection = app.getConnection();
        this.fio_user = app.getFio();

        ObservableList<String> list = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT subjects.name FROM schedule " +
                    "JOIN subjects ON schedule.subject_id = subjects.id AND schedule.group_id = (SELECT group_id FROM students WHERE Fio = '" + this.fio_user + "')";
            ResultSet res = statement.executeQuery(selectSql);
            while (res.next()){
                list.add(res.getString(1));
            }
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            return;
        }
        this.subject_name.setItems(list);
        this.subject_name.setValue(list.get(0));
        ObservableList<String> list1 = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT type_control FROM schedule " +
                    "JOIN subjects ON subjects.id = schedule.subject_id " +
                    "JOIN task ON task.subject = subjects.id AND schedule.group_id = (SELECT group_id FROM students WHERE Fio = '" + this.fio_user + "') ";
            ResultSet res = statement.executeQuery(selectSql);
            while (res.next()){
                list1.add(res.getString(1));
            }
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            return;
        }
        this.type_control.setItems(list1);
        this.type_control.setValue("");
    }

    public void add(MouseEvent mouseEvent) {
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT * FROM task WHERE task.subject = (SELECT id from subjects WHERE name = '" + subject_name.getValue() + "') AND task.type_control = '" + type_control.getValue() + "'";
            ResultSet res = statement.executeQuery(selectSql);
            if (!res.next()){
                app.openErrorWindow("Нет предмета с таким типом контроля", 3);
                return;
            }
            CallableStatement statement1 = null;
            String query = "{call add_progress(?, ?, ?)}";
            statement1 = connection.prepareCall(query);
            statement1.setString(1, fio_user);
            statement1.setString(2, subject_name.getValue());
            statement1.setString(3, type_control.getValue());
            statement1.execute();
            this.app.openMainWindowStudent();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
        }
    }

    public void cancel(MouseEvent mouseEvent) {
        app.openMainWindowStudent();
    }
}
