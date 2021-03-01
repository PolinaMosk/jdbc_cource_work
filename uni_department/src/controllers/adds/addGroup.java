package controllers.adds;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import main.Main;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class addGroup {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField group_number;

    @FXML
    private ChoiceBox<String> group_spec;
    private String fio;
    private Connection connection;
    private Main app;



    public void setAll(String fio, Connection c, Main main){
        this.fio = fio;
        this.connection = c;
        this.app = main;
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT name FROM specialization";
            ResultSet rs = statement.executeQuery(selectSql);
            ObservableList<String> list = FXCollections.observableArrayList();
            while (rs.next()){
                list.add(rs.getString(1));
            }
            group_spec.setItems(list);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
        }
    }

    public void confirm(javafx.scene.input.MouseEvent mouseEvent) {
        if (group_spec.getValue() == null || group_number.getText().equals("")) {
            app.openErrorWindow("Заполните все поля", 1);
            return;
        }
        String query = "{call add_group(?, ?)}";
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall(query);
            statement.setString(1, group_number.getText());
            statement.setString(2, group_spec.getValue());
            statement.execute();
            app.openMainWindowAdmin(0);
        } catch (SQLException ex) {
            this.app.openErrorWindow(ex.getMessage(), 1);
            return;
        }
    }
}
