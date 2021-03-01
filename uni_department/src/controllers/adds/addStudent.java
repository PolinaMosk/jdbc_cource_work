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

public class addStudent {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField fio_input;

    @FXML
    private ChoiceBox<String> group_input;

    @FXML
    private TextField login_input;

    @FXML
    private TextField password__input;

    @FXML
    private TextField phone_input;

    private String fio;
    private Connection connection;
    private Main app;


    public void setAll(String fio, Connection c, Main main){
        this.fio = fio;
        this.connection = c;
        this.app = main;
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT number FROM groups";
            ResultSet rs = statement.executeQuery(selectSql);
            ObservableList<String> list = FXCollections.observableArrayList();
            while (rs.next()){
                list.add(rs.getString(1));
            }
            group_input.setItems(list);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }

    public void confirm(MouseEvent mouseEvent) {
        if (group_input.getValue() == null || fio_input.getText().equals("") || login_input.getText().equals("") || password__input.getText().equals("")) {
            app.openErrorWindow("Заполните все поля", 1);
            return;
        }
        String query = "{call add_student(?, ?, ?, ?, ?)}";
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall(query);
            statement.setString(1, fio_input.getText());
            statement.setString(2, phone_input.getText());
            statement.setString(3, group_input.getValue());
            statement.setString(4, login_input.getText());
            statement.setString(5, password__input.getText());
            statement.execute();
            app.openMainWindowAdmin(3);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }
}
