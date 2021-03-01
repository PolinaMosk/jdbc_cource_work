package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import main.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AnswerController {

    private Connection connection;
    private String question;
    private String teacher_fio;
    @FXML
    private TextArea answer_input;

    @FXML
    private TextArea question_field;

    @FXML
    private Button ready_btn;

    private Main app;


    @FXML
    void answer(MouseEvent event) {
        try {
            if (answer_input.getText() == null || answer_input.getText().equals("")){
                app.openErrorWindow("Запоните все поля", 2);
                return;
            }
            Statement statement = connection.createStatement();
            String selectSql = "UPDATE feedback SET answer= '" + answer_input.getText() + "', teacher_id = " +
                    "(SELECT id from teacher WHERE teacher.Fio = '" + this.teacher_fio +"')" +
                    " FROM feedback WHERE CHARINDEX('" + this.question + "', feedback.question) > 0";
            int res = statement.executeUpdate(selectSql);
            app.openMainWindowTeacher();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    public void setMainApp(Main app){
        this.app = app;
    }

    public void setTeacherId(String fio){
        this.teacher_fio = fio;
    }

    public void setConnection(Connection c){
        this.connection = c;
    }

    public void setQuestion( String q){
        this.question = q;
        this.question_field.setText(q);
    }

    public void cancel(MouseEvent mouseEvent) {
        app.openMainWindowTeacher();
    }
}
