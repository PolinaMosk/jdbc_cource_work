package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import controllers.util.TableUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import main.Main;


public class MainWindowTeacherController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button answer;

    @FXML
    private Button check_deadline;

    @FXML
    private TableColumn<?, ?> classroom;

    @FXML
    private TableView<ObservableList> feedback_table;

    @FXML
    private TableColumn<?, ?> fio;

    @FXML
    private TableColumn<?, ?> group;

    @FXML
    private TableColumn<?, ?> group_number;

    @FXML
    private ToggleGroup group_options;

    @FXML
    private RadioButton groups_sort;

    @FXML
    private TableColumn<?, ?> mark;

    @FXML
    private CheckBox my_groups;

    @FXML
    private CheckBox only_ready;

    @FXML
    private CheckBox only_unread;

    @FXML
    private TableColumn<?, ?> phone;

    @FXML
    private TableColumn<?, ?> progress_subject;

    @FXML
    private TableView<ObservableList> progress_table;

    @FXML
    private TableColumn<?, ?> question;

    @FXML
    private Button rate;

    @FXML
    private TableView<ObservableList> schedule_table;

    @FXML
    private TextField search_feedback;

    @FXML
    private TextField search_progress;

    @FXML
    private TextField search_students;

    @FXML
    private TableColumn<?, ?> status;

    @FXML
    private TableColumn<?, ?> student;

    @FXML
    private TableColumn<?, ?> student_progress;

    @FXML
    private TableView<ObservableList> students_table;

    @FXML
    private TableColumn<?, ?> subject;

    @FXML
    private TableColumn<?, ?> subject_feedback;

    @FXML
    private TableColumn<?, ?> answer_column;

    @FXML
    private RadioButton subjects_sort;

    @FXML
    private TableColumn<?, ?> time;

    @FXML
    private TableColumn<?, ?> type_control;

    @FXML
    private TableColumn<?, ?> weekday;

    @FXML
    private RadioButton weekdays_sort;

    @FXML
    private ChoiceBox<Integer> mark_selection;

    private TableUtil table_util = new TableUtil();


    @FXML
    void addMark(MouseEvent event) {
        Statement statement = null;
        try {
            ObservableList selected = progress_table.getSelectionModel().getSelectedItem();
            statement = connection.createStatement();
            String selectSql = "UPDATE progress SET status = 'принято', mark = " + mark_selection.getValue() + " FROM progress " +
                    "JOIN task ON task.task_id = progress.task " +
                    "JOIN students ON students.id = Student " +
                    "JOIN subjects ON subjects.id = task.subject " +
                    "WHERE students.Fio = '" + selected.get(0) + "' AND subjects.name = '" + selected.get(1) + "' AND task.type_control = '" + selected.get(2) + "'";
            int rs = statement.executeUpdate(selectSql);
            progress_table.getItems().clear();
            loadProgress();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    @FXML
    void checkDeadlines(MouseEvent event) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String selectSql = "UPDATE progress SET status = 'сдача просрочена', mark = 2 FROM progress " +
                    "JOIN task ON task.task_id = progress.task " +
                    "WHERE progress.mark IS NULL AND progress.status != 'готово' AND task.deadline < '" + new Date(new java.util.Date().getTime()) + "'";
            int rs = statement.executeUpdate(selectSql);
            progress_table.getItems().clear();
            loadProgress();
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    private Main app;
    private Connection connection;
    private String fio_user;

    public void setMainApp(Main app){
        this.app = app;
    }

    @FXML
    void initialize() {

        ObservableList<Integer> marks = FXCollections.observableArrayList(2, 3, 4, 5);
        this.mark_selection.setItems(marks);
        this.mark_selection.setValue(5);
        subjects_sort.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            schedule_table.getItems().clear();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT weekday, time, classroom, groups.number, subjects.name FROM schedule " +
                        "JOIN subjects ON subjects.id = schedule.subject_id " +
                        "JOIN groups ON groups.group_id = schedule.group_id " +
                        "JOIN teacher ON teacher_id = teacher.id " +
                        "WHERE teacher.Fio = '" + fio_user + "' " +
                        "ORDER BY subjects.name";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, schedule_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }
        }));

        weekdays_sort.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            schedule_table.getItems().clear();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT weekday, time, classroom, groups.number, subjects.name FROM schedule " +
                        "JOIN subjects ON subjects.id = schedule.subject_id " +
                        "JOIN groups ON groups.group_id = schedule.group_id " +
                        "JOIN weekdays ON weekdays.weekday_name = weekday " +
                        "JOIN teacher ON teacher_id = teacher.id " +
                        "WHERE teacher.Fio = '" + fio_user + "' " +
                        "ORDER BY weekdays.weekday_index";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, schedule_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }
        }));

        groups_sort.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            schedule_table.getItems().clear();
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT weekday, time, classroom, groups.number, subjects.name FROM schedule " +
                        "JOIN subjects ON subjects.id = schedule.subject_id " +
                        "JOIN groups ON groups.group_id = schedule.group_id " +
                        "JOIN teacher ON teacher_id = teacher.id " +
                        "WHERE teacher.Fio = '" + fio_user + "' " +
                        "ORDER BY groups.number";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, schedule_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }
        }));

        my_groups.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            students_table.getItems().clear();
            if (my_groups.isSelected()) {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String selectSql = "SELECT DISTINCT students.Fio, number, students.phone FROM students " +
                            "JOIN groups ON groups.group_id = students.group_id " +
                            "JOIN schedule ON schedule.group_id = groups.group_id " +
                            "JOIN teacher ON schedule.teacher_id = teacher.id " +
                            "WHERE teacher.Fio = '" + fio_user + "'";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.initRows(rs, students_table);
                } catch (SQLException ex) {
                    app.openErrorWindow(ex.getMessage(), 2);
                }
            } else {
                loadStudents();
            }

        }));

        only_unread.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            feedback_table.getItems().clear();
            if (only_unread.isSelected()) {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String selectSql = "SELECT DISTINCT students.Fio, subjects.name, question, answer FROM feedback " +
                            "JOIN students ON students.id = feedback.student_id " +
                            "JOIN subjects ON subjects.id = feedback.subject_id " +
                            "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                            "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                            "WHERE teacher.Fio = '" + fio_user + "' AND feedback.teacher_id is null OR feedback.teacher_id = (SELECT id FROM teacher WHERE Fio = '" + fio_user + "') AND answer is null";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.initRows(rs, feedback_table);
                } catch (SQLException ex) {
                    app.openErrorWindow(ex.getMessage(), 2);
                }
            } else {
                loadForum();
            }

        }));

        only_ready.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            progress_table.getItems().clear();
            if (only_ready.isSelected()) {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String selectSql = "SELECT students.Fio, subjects.name, task.type_control, progress.status, mark FROM progress " +
                            "JOIN students ON students.id = progress.Student " +
                            "JOIN task ON task.task_id = progress.task " +
                            "JOIN subjects ON subjects.id = task.subject " +
                            "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                            "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                            "WHERE teacher.Fio = '" + fio_user + "' AND progress.status = 'готово' AND mark is null";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.initRows(rs, progress_table);
                } catch (SQLException ex) {
                    app.openErrorWindow(ex.getMessage(), 2);
                }
            } else {
                loadProgress();
            }

        }));

        search_students.textProperty().addListener((observable, oldValue, newValue) -> {
            students_table.getItems().clear();
            if (search_students.getText().equals("")) {
                loadStudents();
                return;
            }
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT DISTINCT students.Fio, number, phone FROM students " +
                        "JOIN groups ON groups.group_id = students.group_id " +
                        "WHERE CHARINDEX('" + search_students.getText() + "', students.Fio) > 0";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, students_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }

        });

        search_feedback.textProperty().addListener((observable, oldValue, newValue) -> {
            feedback_table.getItems().clear();
            if (search_feedback.getText().equals("")) {
                loadForum();
                return;
            }
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT students.Fio, subjects.name, question, answer FROM feedback " +
                        "JOIN students ON students.id = feedback.student_id " +
                        "JOIN subjects ON subjects.id = feedback.subject_id " +
                        "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                        "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                        "WHERE teacher.Fio = '" + fio_user + "' AND (feedback.teacher_id is null OR feedback.teacher_id = (SELECT id FROM teacher WHERE Fio = '" + fio_user + "')) AND (CHARINDEX('" + search_feedback.getText() + "', students.Fio) > 0 OR " +
                        "CHARINDEX('" + search_feedback.getText() + "', question) > 0)";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, feedback_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }

        });

        search_progress.textProperty().addListener((observable, oldValue, newValue) -> {
            progress_table.getItems().clear();
            if (search_progress.getText().equals("")) {
                loadProgress();
                return;
            }
            Statement statement = null;
            try {
                statement = connection.createStatement();
                String selectSql = "SELECT students.Fio, subjects.name, task.type_control, progress.status, mark FROM progress " +
                        "JOIN students ON students.id = progress.Student " +
                        "JOIN task ON task.task_id = progress.task " +
                        "JOIN subjects ON subjects.id = task.subject " +
                        "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                        "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                        "WHERE teacher.Fio = '" + fio_user + "' AND (CHARINDEX('" + search_progress.getText() + "', students.Fio) > 0 OR CHARINDEX('" + search_progress.getText() + "', subjects.name) > 0)";
                ResultSet rs = statement.executeQuery(selectSql);
                table_util.initRows(rs, progress_table);
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 2);
            }

        });
    }

    public void loadSchedule(){
        this.connection = app.getConnection();
        this.fio_user = app.getFio();
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT weekday, time, classroom, groups.number, subjects.name FROM schedule " +
                    "JOIN subjects ON subjects.id = schedule.subject_id " +
                    "JOIN groups ON groups.group_id = schedule.group_id " +
                    "JOIN weekdays ON weekdays.weekday_name = weekday " +
                    "JOIN teacher ON teacher_id = teacher.id " +
                    "WHERE teacher.Fio = '" + fio_user + "' " +
                    "ORDER BY weekdays.weekday_index";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, schedule_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    public void loadStudents(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT students.Fio, number, phone FROM students " +
                    "JOIN groups ON groups.group_id = students.group_id ";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, students_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    public void loadForum(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT students.Fio, subjects.name, question, answer FROM feedback " +
                    "JOIN students ON students.id = feedback.student_id " +
                    "JOIN subjects ON subjects.id = feedback.subject_id " +
                    "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                    "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                    "WHERE teacher.Fio = '" + fio_user + "' AND (feedback.teacher_id is null OR feedback.teacher_id = (SELECT id FROM teacher WHERE Fio = '" + fio_user + "'))";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, feedback_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    public void loadProgress(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT students.Fio, subjects.name, task.type_control, progress.status, mark FROM progress " +
                    "JOIN students ON students.id = progress.Student " +
                    "JOIN task ON task.task_id = progress.task " +
                    "JOIN subjects ON subjects.id = task.subject " +
                    "JOIN mtm_teacher_subject ON mtm_teacher_subject.subject_id = subjects.id " +
                    "JOIN teacher ON teacher.id = mtm_teacher_subject.teacher_id " +
                    "WHERE teacher.Fio = '" + fio_user + "' ";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, progress_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 2);
        }
    }

    public void openAnswerForm(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/answer_form.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
            this.app.getPrimary_stage().show();
            AnswerController answerController = loader.getController();
            if (feedback_table.getSelectionModel().getSelectedItem() == null){
                app.openErrorWindow("Вы не выбрали студента", 2);
                return;
            }
            answerController.setQuestion((String) feedback_table.getSelectionModel().getSelectedItem().get(2));
            answerController.setTeacherId(this.fio_user);
            answerController.setConnection(this.connection);
            answerController.setMainApp(this.app);
        } catch (IOException e) {
            app.openErrorWindow(e.getMessage(), 2);
        }
    }


}
