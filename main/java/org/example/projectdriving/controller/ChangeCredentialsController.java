package org.example.projectdriving.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.UserBO;
import org.example.projectdriving.dto.UserDto;

public class ChangeCredentialsController {
    public PasswordField txtCurrentPassword;
    public TextField txtNewUsername;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;

    private UserDto loggedInUser;
    private final UserBO userBO = BOFactory.getInstance().getBO(BOTypes.USER);

    public void initData(UserDto user){
        this.loggedInUser = user;
        if(loggedInUser != null) {
            txtNewUsername.setText(loggedInUser.getUserName());
        }
    }

    public void onUpdateCredentialsAction(ActionEvent actionEvent) {
        String currentPassword = txtCurrentPassword.getText();
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String newUsername = txtNewUsername.getText();

        if(currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || newUsername.isEmpty()){
            new Alert(Alert.AlertType.ERROR, "All fields must be filled!").show();
            return;
        }

        if(!newPassword.equals(confirmPassword)){
            new Alert(Alert.AlertType.ERROR, "New Password and confirmation passwords do not match!").show();
            return;
        }

        try {
            if(userBO.authenticate(loggedInUser.getUserName(), currentPassword) != null){
                if(!newUsername.equalsIgnoreCase(loggedInUser.getUserName()) && userBO.isUsernameValid((newUsername))){
                    new Alert(Alert.AlertType.ERROR, "Username is already taken!").show();
                    return;
                }
                userBO.updateUserCredentials(loggedInUser.getUserId(), newUsername, newPassword);
                new Alert(Alert.AlertType.INFORMATION, "User Credentials Updated successfully...!").show();
                ((javafx.stage.Stage) txtCurrentPassword.getScene().getWindow()).close();
            }else {
                new Alert(Alert.AlertType.ERROR, "Username or password is incorrect!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update credentials...!").show();
            e.printStackTrace();
        }

    }
}
