package org.example.projectdriving.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.UserBO;
import org.example.projectdriving.dto.UserDto;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField txtUsername;
    public PasswordField txtPassword;
    public Button btnLogin;

    private final UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);
    public TextField txtVisiblePassword;
    public CheckBox chkShowPassword;



    public void btnLoginOnAction(ActionEvent actionEvent) {

        String username = txtUsername.getText();
        String password = txtPassword.isVisible() ? txtPassword.getText() : txtVisiblePassword.getText();

        try {
            boolean usernameExists = userBO.isUsernameValid(username);
            if (!usernameExists) {
                new Alert(Alert.AlertType.ERROR, "Invalid Username").show();
                return;
            }

            UserDto user = userBO.authenticate(username, password);

            if(user != null) {
                loadDashBoard(user);
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

    private void loadDashBoard(UserDto user) throws IOException {

       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBboard.fxml"));
           Parent root = loader.load();

           DashboardController dashboardController = loader.getController();

           dashboardController.setUser(user);

           Stage dashboardStage = new Stage();
           dashboardStage.setTitle("DashBboard");
           dashboardStage.setScene(new Scene(root));
           dashboardStage.show();

       }catch (IOException e) {
           new Alert(Alert.AlertType.ERROR, " Dashboard load  failed").show();
           e.printStackTrace();
       }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chkShowPassword.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                txtVisiblePassword.setText(txtPassword.getText());
                txtVisiblePassword.setVisible(true);
                txtVisiblePassword.setManaged(true);
                txtPassword.setVisible(false);
                txtPassword.setManaged(false);
            }else {
                txtPassword.setText(txtVisiblePassword.getText());
                txtPassword.setVisible(true);
                txtPassword.setManaged(true);
                txtVisiblePassword.setVisible(false);
                txtVisiblePassword.setManaged(false);
            }
        });
        txtPassword.textProperty().bindBidirectional(txtVisiblePassword.textProperty());
    }
}
