package controllers.adds;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Main;

public class addTeacher {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField fio_input;

    @FXML
    private TextField login_input;

    @FXML
    private TextField password_input;

    @FXML
    private TextField phone_input;

    @FXML
    private TextField position_input;

    @FXML
    private ChoiceBox<String> subjects;

    private String fio;
    private Connection connection;
    private Main app;
    private List<String> subjects_list = new ArrayList<>();


    @FXML
    void confirm(MouseEvent event) {
        if (position_input.getText().equals("") || fio_input.getText().equals("") || login_input.getText().equals("") || password_input.getText().equals("")) {
            app.openErrorWindow("Заполните все поля", 1);
            return;
        }
        for (String s: subjects_list){
            String query = "{call add_teacher(?, ?, ?, ?, ?, ?)}";
            CallableStatement statement = null;
            try {
                statement = connection.prepareCall(query);
                statement.setString(1, fio_input.getText());
                statement.setString(2, phone_input.getText());
                statement.setString(3, position_input.getText());
                statement.setString(4, login_input.getText());
                statement.setString(5, password_input.getText());
                statement.setString(6, s);
                statement.execute();
                app.openMainWindowAdmin(2);
            } catch (SQLException ex) {
                this.app.openErrorWindow(ex.getMessage(), 1);
            }
        }
    }

    public void setAll(String fio, Connection c, Main main){
        this.fio = fio;
        this.connection = c;
        this.app = main;
        this.subjects_list.add("");
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT name FROM subjects";
            ResultSet rs = statement.executeQuery(selectSql);
            ObservableList<String> list = FXCollections.observableArrayList();
            while (rs.next()){
                list.add(rs.getString(1));
            }
            subjects.setItems(list);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }

    public void add_subject(MouseEvent mouseEvent) {
        if (subjects.getValue() != null) subjects_list.add(subjects.getValue());
    }
}
