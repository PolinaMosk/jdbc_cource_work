package controllers.util;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import main.Main;

public class ErrorWindowController {
    @FXML
    private TextArea error_output;

    @FXML
    private Button ok;
    private Main app;
    private int status;

    public void setMainApp(Main app){
        this.app = app;
    }

    public void show(String msg, int status){
        this.error_output.setText(msg);
        this.status = status;
    }

    public void close(MouseEvent mouseEvent) {
        if (this.status == 1) this.app.openMainWindowAdmin(0);
        if (this.status == 3) this.app.openMainWindowStudent();
        if (this.status == 2) this.app.openMainWindowTeacher();
        if (this.status == 4) {
            try {
                this.app.start(app.getPrimary_stage());
            } catch (Exception e) {
                app.openErrorWindow(e.getMessage(), 4);
            }
        }
    }
}
