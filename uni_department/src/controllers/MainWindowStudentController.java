package controllers;

import controllers.adds.AddProgressController;
import controllers.util.TableUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainWindowStudentController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<ObservableList> academic_plan;

    @FXML
    private Button add_diary;

    @FXML
    private Button ask;

    @FXML
    private Button ask_;

    @FXML
    private ChoiceBox<String> change_status;

    @FXML
    private TableColumn<?, ?> classroom;

    @FXML
    private TextField avg_mark;

    @FXML
    private TableColumn<?, ?> control_subject;

    @FXML
    private TableView<ObservableList> diary;

    @FXML
    private TableColumn<?, ?> diary_control;

    @FXML
    private TableColumn<?, ?> diary_diary;

    @FXML
    private TableColumn<?, ?> diary_status;

    @FXML
    private TableColumn<?, ?> diary_subject;

    @FXML
    private TableColumn<?, ?> feedback_answer;

    @FXML
    private TableColumn<?, ?> feedback_question;

    @FXML
    private TableColumn<?, ?> feedback_teacher;

    @FXML
    private TableColumn<?, ?> fio_teachers;

    @FXML
    private TableColumn<?, ?> phone_teachers;

    @FXML
    private TableColumn<?, ?> plan_subject;

    @FXML
    private TableView<ObservableList> schedule_table;

    @FXML
    private TableView<ObservableList> forum_table;

    @FXML
    private RadioButton sort_subjects;

    @FXML
    private RadioButton sort_weekdays;

    @FXML
    private TableColumn<?, ?> subject;

    @FXML
    private TableColumn<?, ?> subject_teachers;

    @FXML
    private TableColumn<?, ?> teacher;

    @FXML
    private TableView<ObservableList> teachers_table;

    @FXML
    private TableColumn<?, ?> weekday;

    @FXML
    private TableColumn<?, ?> time;

    @FXML
    void initialize(){
        ObservableList<String> status = FXCollections.observableArrayList("начато", "в процесее", "готово");
        this.change_status.setItems(status);
        this.change_status.setValue("начато");
        sort_subjects.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            schedule_table.getItems().clear();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT weekday, time, classroom, subjects.name, teacher.Fio FROM schedule " +
                        "JOIN subjects ON subjects.id = schedule.subject_id " +
                        "JOIN weekdays ON weekdays.weekday_name = weekday " +
                        "JOIN teacher ON teacher_id = teacher.id " +
                        "WHERE schedule.group_id = (SELECT group_id FROM students WHERE students.Fio = '" + this.fio_user + "') " +
                        "ORDER BY subjects.name";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, schedule_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 3);
                ex.printStackTrace();
            }
        }));

        sort_weekdays.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            schedule_table.getItems().clear();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT weekday, time, classroom, subjects.name, teacher.Fio FROM schedule " +
                        "JOIN subjects ON subjects.id = schedule.subject_id " +
                        "JOIN weekdays ON weekdays.weekday_name = weekday " +
                        "JOIN teacher ON teacher_id = teacher.id " +
                        "WHERE schedule.group_id = (SELECT group_id FROM students WHERE students.Fio = '" + this.fio_user + "') " +
                        "ORDER BY weekdays.weekday_index";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, schedule_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 3);
                ex.printStackTrace();
            }
        }));
    }

    private Main app;
    private Connection connection;
    private String fio_user;
    private TableUtil table_util = new TableUtil();

    public void setMainApp(Main app){
        this.app = app;
        this.connection = app.getConnection();
        this.fio_user = app.getFio();
    }


    public void loadSchedule(){
        this.connection = app.getConnection();
        this.fio_user = app.getFio();
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT weekday, time, classroom, subjects.name, teacher.Fio FROM schedule " +
                    "JOIN subjects ON subjects.id = schedule.subject_id " +
                    "JOIN weekdays ON weekdays.weekday_name = weekday " +
                    "JOIN teacher ON teacher_id = teacher.id " +
                    "WHERE schedule.group_id = (SELECT group_id FROM students WHERE students.Fio = '" + this.fio_user + "') " +
                    "ORDER BY weekdays.weekday_index";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, schedule_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void loadDiary(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT subjects.name, task.type_control, status, mark, deadline FROM progress " +
                    "JOIN task ON task.task_id = progress.task " +
                    "JOIN subjects ON subjects.id = task.subject " +
                    "WHERE progress.Student = (SELECT id FROM students WHERE students.Fio = '" + this.fio_user + "') ";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, diary);
            selectSql = "SELECT AVG(Cast(mark as Float)) as average_mark FROM progress " +
                    "JOIN students ON students.id = Student " +
                    "WHERE mark is not null AND Student = (SELECT id from students WHERE Fio = '" + fio_user + "') ";
            rs = statement.executeQuery(selectSql);
            if (rs.next()) avg_mark.setText(String.valueOf(rs.getDouble(1)));
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void change_status(MouseEvent mouseEvent) {
        try {
            if (diary.getSelectionModel() == null) {
                app.openErrorWindow("Вы ничего не выбрали", 3);
                return;
            }
            if (diary.getSelectionModel().getSelectedItem().get(2) == "сдача просрочена" || diary.getSelectionModel().getSelectedItem().get(3) != null) {
                app.openErrorWindow("Нельзя поменять статус оцененной работы", 3);
                return;
            }
            Statement statement = connection.createStatement();
            String selectSql = "UPDATE progress SET status= '" + change_status.getValue() + "' FROM progress " +
                    "JOIN task ON task.task_id = progress.task " +
                    "JOIN subjects on subjects.id = task.subject " +
                    "WHERE progress.Student = (SELECT id from students WHERE students.Fio = '" + this.fio_user + "') " +
                    "AND subjects.name = '" + diary.getSelectionModel().getSelectedItem().get(0) + "' " +
                    "AND task.type_control = '" + diary.getSelectionModel().getSelectedItem().get(1) + "'";
            int res = statement.executeUpdate(selectSql);
            diary.getItems().clear();
            loadDiary();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void add_progress(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_progress.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
            AddProgressController add_controller = new AddProgressController();
            add_controller = loader.getController();
            add_controller.setMainApp(this.app);
            this.app.getPrimary_stage().show();
        } catch (IOException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void loadTeachers(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT Fio, subjects.name, phone FROM teacher " +
                    "JOIN schedule ON schedule.group_id = (SELECT group_id from students WHERE students.Fio = '" + this.fio_user + "') " +
                    "JOIN mtm_teacher_subject ON mtm_teacher_subject.teacher_id = schedule.teacher_id " +
                    "JOIN subjects ON subjects.id = mtm_teacher_subject.subject_id " +
                    "WHERE schedule.teacher_id = teacher.id AND subjects.id = schedule.subject_id";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, teachers_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void open_ask_form(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ask_form.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
            AskFormController askFormController = loader.getController();
            if (teachers_table.getSelectionModel().getSelectedItem() != null){
                askFormController.setSelectedTeacher((String) teachers_table.getSelectionModel().getSelectedItem().get(0), (String) teachers_table.getSelectionModel().getSelectedItem().get(1));
            }
            askFormController.setMainApp(this.app);
            this.app.getPrimary_stage().show();
        } catch (IOException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void loadForum(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT teacher.Fio, question, answer FROM feedback " +
                    "JOIN teacher ON teacher.id = feedback.teacher_id " +
                    "JOIN students ON students.id = feedback.student_id " +
                    "WHERE students.Fio = '" + fio_user + "'";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, forum_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void loadPlan(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT name, type_control FROM schedule " +
                    "JOIN subjects ON schedule.subject_id = subjects.id AND schedule.group_id = (SELECT group_id from students WHERE Fio = '" + fio_user +"') " +
                    "JOIN task ON task.subject = subjects.id";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, academic_plan);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }

    public void forum_ask(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ask_form.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
            AskFormController askFormController = loader.getController();
            askFormController.setMainApp(this.app);
            this.app.getPrimary_stage().show();
        } catch (IOException ex) {
            app.openErrorWindow(ex.getMessage(), 3);
            ex.printStackTrace();
        }
    }
}
