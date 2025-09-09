package org.example.projectdriving.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.example.projectdriving.dao.SuperDAO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Button btnStudent;
    public Button btnCourse;
    public Button btnInstructor;
    public Button btnLesson;
    public Button btnPayment;
    public AnchorPane ancMainContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        navigateTo("/view/Student.fxml");
    }



    public void btnStudentOnAction(ActionEvent actionEvent) {
        navigateTo("/view/Student.fxml");
    }

    public void btnCourseOnAction(ActionEvent actionEvent) {
        navigateTo("/view/Course.fxml");
    }

    public void btnInstructorOnAction(ActionEvent actionEvent) {
        navigateTo("/view/Instructor.fxml");
    }

    public void btnLessonOnAction(ActionEvent actionEvent) {
        navigateTo("/view/Lesson.fxml");
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) {
        navigateTo("/view/Payment.fxml");
    }

    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());

            ancMainContainer.getChildren().add(anchorPane);

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Page not found..!").show();
            throw new RuntimeException(e);
        }
    }


}
