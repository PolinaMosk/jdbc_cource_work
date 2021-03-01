package controllers;

import controllers.adds.*;
import controllers.util.ErrorWindowController;
import controllers.util.TableUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MainWindowAdminController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> column2;

    @FXML
    private TableColumn<?, ?> fio;

    @FXML
    private Button open_add_group;

    @FXML
    private Button open_add_student;

    @FXML
    private Button open_add_subject;

    @FXML
    private Button open_add_task;

    @FXML
    private Button open_add_teacher;

    @FXML
    private Button add_schedule;

    @FXML
    private TableColumn<?, ?> phone;

    @FXML
    private TableView<ObservableList> table_1;

    @FXML
    private TableView<ObservableList> phone_book;

    @FXML
    private TableView<ObservableList> journal;

    @FXML
    private RadioButton teachers_radio;

    @FXML
    private RadioButton students_radio;

    @FXML
    private RadioButton only_not_started;

    @FXML
    private RadioButton only_passed;

    @FXML
    private RadioButton all;

    @FXML
    private RadioButton sort_mark_high;

    @FXML
    private RadioButton sort_spec;

    @FXML
    private TableView<ObservableList> stat_table;

    @FXML
    private TextField max_mark;

    @FXML
    private TextField min_mark;

    @FXML
    private RadioButton mark_filter;

    @FXML
    private ToggleGroup sort;
    private Main app;
    private Connection connection;
    private TableUtil table_util = new TableUtil();

    @FXML
    void initialize(){
        all.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                Statement statement = connection.createStatement();;
                if (all.isSelected()){
                    journal.getItems().clear();
                    loadJournal();
                }
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 1);
                ex.printStackTrace();
            }
        }));

        only_passed.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                Statement statement = connection.createStatement();;
                if (only_passed.isSelected()){
                    journal.getItems().clear();
                    String selectSql = "SELECT DISTINCT Fio, number, name, status, mark FROM schedule " +
                            "JOIN students ON students.group_id = schedule.group_id " +
                            "JOIN subjects ON schedule.subject_id = subjects.id " +
                            "JOIN groups ON students.group_id = groups.group_id " +
                            "JOIN task ON task.subject = subjects.id " +
                            "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id " +
                            "WHERE mark IN ('3', '4', '5')";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.init_columns(rs, journal);
                } else {
                    journal.getItems().clear();
                    loadJournal();
                }
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 1);
                ex.printStackTrace();
            }
        }));

        only_not_started.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            try {
                Statement statement = connection.createStatement();;
                if (only_not_started.isSelected()){
                    journal.getItems().clear();
                    String selectSql = "SELECT DISTINCT Fio, number, name, status, mark FROM schedule " +
                            "JOIN students ON students.group_id = schedule.group_id " +
                            "JOIN subjects ON schedule.subject_id = subjects.id " +
                            "JOIN groups ON students.group_id = groups.group_id " +
                            "JOIN task ON task.subject = subjects.id " +
                            "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id " +
                            "WHERE mark is null AND progress.status is null";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.init_columns(rs, journal);
                } else {
                    journal.getItems().clear();
                    loadJournal();
                }
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 1);
                ex.printStackTrace();
            }
        }));

        teachers_radio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            Statement statement = null;
            try {
                if (teachers_radio.isSelected()){
                    if (students_radio.isSelected()){
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, position FROM teacher UNION SELECT Fio, phone, 'студент' FROM students";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    } else {
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, position FROM teacher";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    }
                } else {
                    if (students_radio.isSelected()){
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, 'student' FROM students";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    } else {
                        phone_book.getItems().clear();
                    }
                }
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 1);
                ex.printStackTrace();
            }
        }));

        students_radio.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            Statement statement = null;
            try {
                if (students_radio.isSelected()){
                    if (teachers_radio.isSelected()){
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, position FROM teacher UNION SELECT Fio, phone, 'студент' FROM students";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    } else {
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, 'студент' FROM students";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    }
                } else {
                    if (teachers_radio.isSelected()){
                        phone_book.getItems().clear();
                        statement = connection.createStatement();
                        String selectSql = "SELECT Fio, phone, position FROM teacher";
                        ResultSet rs = statement.executeQuery(selectSql);
                        table_util.initRows(rs, phone_book);
                    } else {
                        phone_book.getItems().clear();
                    }
                }
            } catch (SQLException ex) {
                app.openErrorWindow(ex.getMessage(), 1);
                ex.printStackTrace();
            }
        }));

        mark_filter.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (mark_filter.isSelected()) {
                if (min_mark.getText() == null || max_mark.getText() == null) {
                    app.openErrorWindow("Заполните все поля", 1);
                    return;
                }
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String selectSql = "SELECT DISTINCT groups.number, specialization.name, AVG(Cast(mark as Float)) as average_mark FROM schedule " +
                            "JOIN students ON students.group_id = schedule.group_id " +
                            "JOIN subjects ON schedule.subject_id = subjects.id " +
                            "JOIN groups ON students.group_id = groups.group_id " +
                            "JOIN specialization ON specialization.id = groups.specialization " +
                            "JOIN task ON task.subject = subjects.id " +
                            "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id " +
                            "GROUP BY groups.number, specialization.name " +
                            "HAVING AVG(Cast(mark as Float)) > " + Float.valueOf(min_mark.getText()) + " AND AVG(Cast(mark as Float)) < " + Float.valueOf(max_mark.getText());
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.init_columns(rs, stat_table);
                } catch (SQLException ex) {
                    app.openErrorWindow(ex.getMessage(), 1);
                    ex.printStackTrace();
                }
            } else {
                loadStatistics();
            }
        }));

        sort_spec.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (sort_spec.isSelected()) {
                loadStatistics();
            }
        }));

        sort_mark_high.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (sort_mark_high.isSelected()) {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    String selectSql = "SELECT DISTINCT groups.number, specialization.name, AVG(Cast(mark as Float)) as average_mark FROM schedule " +
                            "JOIN students ON students.group_id = schedule.group_id " +
                            "JOIN subjects ON schedule.subject_id = subjects.id " +
                            "JOIN groups ON students.group_id = groups.group_id " +
                            "JOIN specialization ON specialization.id = groups.specialization " +
                            "JOIN task ON task.subject = subjects.id " +
                            "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id " +
                            "GROUP BY groups.number, specialization.name " +
                            "ORDER BY average_mark DESC";
                    ResultSet rs = statement.executeQuery(selectSql);
                    table_util.init_columns(rs, stat_table);
                } catch (SQLException ex) {
                    app.openErrorWindow(ex.getMessage(), 1);
                    ex.printStackTrace();
                }
            } else {
                loadStatistics();
            }
        }));
    }


    public void open_add_group(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_group.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addGroup addGroup_controller = loader.getController();
        addGroup_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void open_add_subject(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_subject.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addSubject addSubject_controller = loader.getController();
        addSubject_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void open_add_task(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_task.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addTask addTask_controller = loader.getController();
        addTask_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void open_add_teacher(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_teacher.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addTeacher addTeacher_controller = loader.getController();
        addTeacher_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void open_add_student(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_student.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addStudent addGStudent_controller = loader.getController();
        addGStudent_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void loadStudents(){
        fio.setText("ФИО");
        column2.setText("Группа");
        phone.setText("Телефон");
        if (table_1.getColumns().size() == 4) table_1.getColumns().remove(3);
        column2.setVisible(true);
        phone.setVisible(true);
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT Fio, number, phone FROM students " +
                    "JOIN groups ON students.group_id = groups.group_id";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, table_1);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void loadTeachers(){
        fio.setText("ФИО");
        column2.setText("Должность");
        phone.setText("Телефон");
        if (table_1.getColumns().size() == 4) table_1.getColumns().remove(3);
        column2.setVisible(true);
        phone.setVisible(true);
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT Fio, position, phone FROM teacher ";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, table_1);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void loadPhoneBook(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT Fio, phone, position FROM teacher UNION SELECT Fio, phone, 'студент' FROM students";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, phone_book);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void loadJournal(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT Fio, number, name, status, mark FROM schedule " +
                    "JOIN students ON students.group_id = schedule.group_id " +
                    "JOIN subjects ON schedule.subject_id = subjects.id " +
                    "JOIN groups ON students.group_id = groups.group_id " +
                    "JOIN task ON task.subject = subjects.id " +
                    "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, journal);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void loadStatistics(){
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT DISTINCT groups.number, specialization.name, AVG(Cast(mark as Float)) as average_mark FROM schedule " +
                    "JOIN students ON students.group_id = schedule.group_id " +
                    "JOIN subjects ON schedule.subject_id = subjects.id " +
                    "JOIN groups ON students.group_id = groups.group_id " +
                    "JOIN specialization ON specialization.id = groups.specialization " +
                    "JOIN task ON task.subject = subjects.id " +
                    "LEFT JOIN progress ON progress.Student = students.id AND progress.task = task.task_id " +
                    "GROUP BY groups.number, specialization.name " +
                    "ORDER BY specialization.name";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, stat_table);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void setMainApp(Main main) {
        this.app = main;
    }
    public void setConnection(Connection c){
        this.connection = c;
    }

    public void loadSubjects() {
        fio.setText("Название");
        column2.setVisible(false);
        phone.setVisible(false);
        if (table_1.getColumns().size() == 4) table_1.getColumns().remove(3);
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT name FROM subjects ";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, table_1);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }

    public void open_add_schedule(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_schedule.fxml"));
        try {
            this.app.getPrimary_stage().setScene(new Scene(loader.load()));
        } catch (IOException e) {
            this.app.openErrorWindow(e.getMessage(), 1);
            e.printStackTrace();
        }
        addSchedule addSchedule_controller = loader.getController();
        addSchedule_controller.setAll(this.app.getFio(), connection, app);
        this.app.getPrimary_stage().show();
    }

    public void loadSchedule() {
        fio.setText("День недели");
        column2.setText("Группа");
        phone.setText("Преподаватель");
        if (table_1.getColumns().size() < 4) {
            TableColumn c1 = new TableColumn();
            c1.setText("Время");
            table_1.getColumns().addAll(c1);
        }
        column2.setVisible(true);
        phone.setVisible(true);
        try {
            Statement statement = connection.createStatement();
            String selectSql = "SELECT weekday, number, Fio, time FROM schedule " +
                    "JOIN groups ON groups.group_id = schedule.group_id " +
                    "JOIN teacher ON teacher.id = schedule.teacher_id";
            ResultSet rs = statement.executeQuery(selectSql);
            table_util.init_columns(rs, table_1);
        } catch (SQLException ex) {
            app.openErrorWindow(ex.getMessage(), 1);
            ex.printStackTrace();
        }
    }
}
