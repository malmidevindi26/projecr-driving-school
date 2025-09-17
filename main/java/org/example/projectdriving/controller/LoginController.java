package org.example.projectdriving.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.UserBO;
import org.example.projectdriving.dto.UserDto;

import java.io.IOException;

public class LoginController {
    public TextField txtUsername;
    public PasswordField txtPassword;
    public Button btnLogin;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);

    public void btnLoginOnAction(ActionEvent actionEvent) {

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            boolean usernameExists = userBO.isUsernameValid(username);
            if (!usernameExists) {
                new Alert(Alert.AlertType.ERROR, "Invalid Username").show();
                return;
            }

            UserDto user = userBO.authenticate(username, password);

            if(user != null) {
                loadDashBoard(user.getRole());
               // new Alert(Alert.AlertType.INFORMATION, "Login successful").show();
               Stage currentStage = (Stage) txtUsername.getScene().getWindow();
               currentStage.close();
            }else {
                new Alert(Alert.AlertType.ERROR, "Incorrect password").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Login failed").show();
            e.printStackTrace();
        }
    }

    private void loadDashBoard(String role) throws IOException {

       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBboard.fxml"));
           Parent root = loader.load();

           DashboardController dashboardController = loader.getController();

           dashboardController.setUserRole(role);

           Stage dashboardStage = new Stage();
           dashboardStage.setTitle("DashBboard");
           dashboardStage.setScene(new Scene(root));
           dashboardStage.show();

       }catch (IOException e) {
           new Alert(Alert.AlertType.ERROR, " Dashboard load  failed").show();
           e.printStackTrace();
       }
    }
}
