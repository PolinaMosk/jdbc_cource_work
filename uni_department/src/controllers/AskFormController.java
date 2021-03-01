package controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AskFormController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea question_input;

    @FXML
    private Button send_question;

    @FXML
    private ChoiceBox<String> subject_input;

    @FXML
    private ChoiceBox<String> teacher_input;

    private Connection connection;
    private Main app;
    private String fio;
    private String teacher_fio;
    private String subject;

    @FXML
    void initialize(){
        this.teacher_fio = null;
        this.subject = null;
    }

    public void confirm_q(MouseEvent mouseEvent) {
        try {
            if (subject_input.getValue() == null || question_input.getText().equals("")) {
                app.openErrorWindow("Заполните обязательное поле", 3);
                return;
            }
            Statement statement = connection.createStatement();
            String selectSql;
            selectSql = "SELECT * FROM mtm_teacher_subject " +
                    "JOIN subjects ON subjects.id = mtm_teacher_subject.subject_id " +
                    "JOIN teacher ON mtm_teacher_subject.teacher_id = teacher.id " +
                    "WHERE subjects.name = '" + subject_input.getValue() + "' AND teacher.Fio = '" + teacher_input.getValue() + "'";
            ResultSet res = statement.executeQuery(selectSql);
            if (!res.next()) {
                app.openErrorWindow("Данный преподаватель не ведет этот предмет", 3);
                return;
            }
            if (teacher_input.getValue() == null) {
                selectSql = "INSERT feedback(question, student_id, subject_id) " +
                        "VALUES('" + question_input.getText() + "', (SELECT id FROM students WHERE Fio = '" + fio + "'), (SELECT id FROM subjects WHERE (name = '" + subject_input.getValue() + "') ";
            } else {
                selectSql = "INSERT feedback(question, student_id, subject_id, teacher_id) " +
                        "VALUES('" + question_input.getText() + "', (SELECT id FROM students WHERE Fio = '" + fio + "'), (SELECT id FROM subjects WHERE name = '" + subject_input.getValue() + "'), (SELECT id FROM teacher WHERE Fio = '" + teacher_input.getValue() + "'))";
            }
            int ress = statement.executeUpdate(selectSql);
            this.app.openMainWindowStudent();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
        }
    }

     public void setMainApp(Main app){
        this.app = app;
        this.connection = app.getConnection();
        this.fio = app.getFio();
        try {
            Statement statement = connection.createStatement();
            if (this.teacher_fio == null) {
                String selectSql = "SELECT DISTINCT Fio FROM teacher " +
                        "JOIN schedule ON schedule.group_id = (SELECT group_id from students WHERE students.Fio = '" + this.fio + "') " +
                        "JOIN mtm_teacher_subject ON mtm_teacher_subject.teacher_id = schedule.teacher_id " +
                        "JOIN subjects ON subjects.id = mtm_teacher_subject.subject_id " +
                        "WHERE schedule.teacher_id = teacher.id";
                ResultSet res = statement.executeQuery(selectSql);
                ObservableList<String> list = FXCollections.observableArrayList();
                list.add("");
                while (res.next()) {
                    list.add(res.getString(1));
                }
                teacher_input.setItems(list);
            } else {
                ObservableList<String> list = FXCollections.observableArrayList();
                list.add(this.teacher_fio);
                teacher_input.setItems(list);
                teacher_input.setValue(this.teacher_fio);
            }

            if (this.subject == null) {
                String selectSql = "SELECT DISTINCT subjects.name FROM schedule " +
                        "JOIN subjects ON schedule.subject_id = subjects.id AND schedule.group_id = (SELECT group_id FROM students WHERE Fio = '" + this.fio + "')";
                ResultSet res = statement.executeQuery(selectSql);
                ObservableList<String> list2 = FXCollections.observableArrayList();
                list2.add("");
                while (res.next()) {
                    list2.add(res.getString(1));
                }
                subject_input.setItems(list2);
            } else {
                ObservableList<String> list = FXCollections.observableArrayList();
                list.add(this.subject);
                subject_input.setItems(list);
                subject_input.setValue(this.subject);
            }
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
        }
     }

     public void setSelectedTeacher(String teacher_fio, String subject){
        this.teacher_fio = teacher_fio;
        this.subject = subject;
     }

    public void cancel(MouseEvent mouseEvent) {
        app.openMainWindowStudent();
    }
}
