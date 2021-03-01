package main;

import controllers.AuthorizationController;
import controllers.MainWindowAdminController;
import controllers.MainWindowStudentController;
import controllers.MainWindowTeacherController;
import controllers.util.ErrorWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main extends Application {
    private Connection connection;
    private AuthorizationController auth_controller;
    private MainWindowTeacherController teacher_controller;
    private MainWindowStudentController student_controller;
    private MainWindowAdminController admin_controller;
    private Stage primary_stage;
    private String fio;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primary_stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/authorization.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        auth_controller = loader.getController();
        primaryStage.show();
        auth_controller.setMainApp(this);
    }

    public Stage getPrimary_stage() {
        return primary_stage;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void openMainWindowTeacher(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_window_teacher.fxml"));
        try {
            this.primary_stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            openErrorWindow(e.getMessage(), 4);
        }
        this.teacher_controller = loader.getController();
        this.teacher_controller.setMainApp(this);
        this.primary_stage.show();
        this.teacher_controller.loadSchedule();
        this.teacher_controller.loadStudents();
        this.teacher_controller.loadForum();
        this.teacher_controller.loadProgress();
    }

    public void openMainWindowStudent(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_window_student.fxml"));
        try {
            this.primary_stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            openErrorWindow(e.getMessage(), 4);
        }
        this.student_controller = loader.getController();
        this.student_controller.setMainApp(this);
        this.primary_stage.show();
        this.student_controller.loadSchedule();
        this.student_controller.loadDiary();
        this.student_controller.loadTeachers();
        this.student_controller.loadForum();
        this.student_controller.loadPlan();
    }

    public void openMainWindowAdmin(int i){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_window_admin.fxml"));
        try {
            this.primary_stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            openErrorWindow(e.getMessage(), 4);
        }
        this.admin_controller = loader.getController();
        this.admin_controller.setMainApp(this);
        this.admin_controller.setConnection(connection);
        this.admin_controller.loadPhoneBook();
        this.admin_controller.loadJournal();
        this.admin_controller.loadStatistics();
        if (i == 3) {
            admin_controller.loadStudents();
        }
        if (i == 2) {
            admin_controller.loadTeachers();
        }
        if (i == 4) {
            admin_controller.loadSubjects();
        }
        if (i == 5) {
            admin_controller.loadSchedule();
        }
        this.primary_stage.show();
    }

    public void openErrorWindow(String msg, int status){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/error_window.fxml"));
        try {
            this.primary_stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            openErrorWindow(e.getMessage(), 4);
        }
        ErrorWindowController errorWindowController = loader.getController();
        errorWindowController.setMainApp(this);
        errorWindowController.show(msg, status);
        this.primary_stage.show();
    }

    public String getFio() {
        return fio;
    }
    /*  ТРИГГЕР - ПРОВЕРКА ПРОГРЕССА НА ДЕДЛАЙН */

    public static void main(String[] args) {
        launch(args);
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}
