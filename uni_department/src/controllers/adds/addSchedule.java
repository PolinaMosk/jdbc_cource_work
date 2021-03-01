package controllers.adds;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class addSchedule {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cabinet;

    @FXML
    private ChoiceBox<String> groups;

    @FXML
    private ChoiceBox<String> subjects;

    @FXML
    private ChoiceBox<String> teachers;

    @FXML
    private TextField time;

    @FXML
    private ChoiceBox<String> weekdays;

    private String fio;
    private Connection connection;
    private Main app;

    public void confirm(MouseEvent mouseEvent) {
        if (weekdays.getValue() == null || teachers.getValue() == null || subjects.getValue() == null || groups.getValue() == null || cabinet.getText().equals("") || time.getText().equals("")){
            app.openErrorWindow("Заполните все поля", 1);
            return;
        }
        String query = "{call add_schedule(?, ?, ?, ?, ?, ?)}";
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall(query);
            statement.setString(1, teachers.getValue());
            statement.setString(2, weekdays.getValue());
            statement.setString(3, groups.getValue());
            statement.setString(4, subjects.getValue());
            statement.setString(5, cabinet.getText());
            statement.setString(6, time.getText());
            statement.execute();
            app.openMainWindowAdmin(5);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
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
            while (rs.next()){
                list.add(rs.getString(1));
            }
            subjects.setItems(list);

            selectSql = "SELECT Fio FROM teacher";
            ResultSet rs1 = statement.executeQuery(selectSql);
            ObservableList<String> list1 = FXCollections.observableArrayList();
            while (rs1.next()){
                list1.add(rs1.getString(1));
            }
            teachers.setItems(list1);

            selectSql = "SELECT weekday_name FROM weekdays";
            ResultSet rs2 = statement.executeQuery(selectSql);
            ObservableList<String> list2 = FXCollections.observableArrayList();
            while (rs2.next()){
                list2.add(rs2.getString(1));
            }
            weekdays.setItems(list2);

            selectSql = "SELECT number FROM groups";
            ResultSet rs3 = statement.executeQuery(selectSql);
            ObservableList<String> list3 = FXCollections.observableArrayList();
            while (rs3.next()){
                list3.add(rs3.getString(1));
            }
            groups.setItems(list3);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }
}
