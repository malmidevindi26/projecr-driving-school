package org.example.projectdriving.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.InstructorBO;
import org.example.projectdriving.bo.exception.DuplicateException;
import org.example.projectdriving.bo.exception.InUseException;
import org.example.projectdriving.dto.InstructorDto;
import org.example.projectdriving.dto.tm.InstructorTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class InstructorController implements Initializable {

    private final InstructorBO instructorBO = BOFactory.getInstance().getBO(BOTypes.INSTRUCTOR);

    public Label lblInstructorId;
    public TextField txtFullName;
    public TextField txtPhone;
    public TextField txtEmail;
    public TextArea txtAvailability;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<InstructorTM> tblInstructor;
    public TableColumn<InstructorTM, String> colId;
    public TableColumn<InstructorTM, String> colFullName;
    public TableColumn<InstructorTM, String> colPhone;
    public TableColumn<InstructorTM, String> colEmail;
    public TableColumn<InstructorTM, String> colAvailability;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       colId.setCellValueFactory(new PropertyValueFactory<>("id"));
       colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
       colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
       colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
       colAvailability.setCellValueFactory(new PropertyValueFactory<>("availabilityNote"));

       try {
           resetPage();
       } catch (Exception e) {
           new Alert(
                   Alert.AlertType.ERROR, "Fail to load data..!"
           ).show();
       }
    }

    private void resetPage() throws SQLException {
        loadNextId();
        loadTableData();

        btnSave.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        txtAvailability.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtFullName.setText("");

    }

    private void loadNextId() throws SQLException {
        String nextId = instructorBO.getNextId();
        lblInstructorId.setText(nextId);
    }

    private void loadTableData() throws SQLException {
        tblInstructor.setItems(FXCollections.observableArrayList(
                instructorBO.getAllInstructors().stream().map(instructorDto ->
                        new InstructorTM(
                                instructorDto.getId(),
                                instructorDto.getFullName(),
                                instructorDto.getPhone(),
                                instructorDto.getEmail(),
                                instructorDto.getAvailabilityNote()
                        )).toList()
        ));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String availabilityNote = txtAvailability.getText();
        String id = lblInstructorId.getText();

        boolean isValidName = fullName.matches(namePattern);
        //boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        txtFullName.setStyle(txtFullName.getStyle() + ";-fx-border-color: #BB25B9;");
        //txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #BB25B9;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #BB25B9;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #BB25B9;");

        if (!isValidName) {
            txtFullName.setStyle(txtFullName.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }
        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        InstructorDto instructorDto = new  InstructorDto(
                id,
                fullName,
                phone,
                email,
                availabilityNote
        );

        if(isValidName && isValidEmail && isValidPhone) {
            try {
                instructorBO.saveInstructor(instructorDto);
                resetPage();
                new Alert(
                        Alert.AlertType.INFORMATION, "Instructor saved successfully..!"
                ).show();
            } catch (DuplicateException e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR, "Fail to save Instructor..!"
                ).show();
            }
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();
        String email = txtEmail.getText();
        String availabilityNote = txtAvailability.getText();
        String id = lblInstructorId.getText();

        boolean isValidName = fullName.matches(namePattern);
        //boolean isValidNic = nic.matches(nicPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);

        txtFullName.setStyle(txtFullName.getStyle() + ";-fx-border-color: #BB25B9;");
        //txtNic.setStyle(txtNic.getStyle() + ";-fx-border-color: #BB25B9;");
        txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: #BB25B9;");
        txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: #BB25B9;");

        if (!isValidName) {
            txtFullName.setStyle(txtFullName.getStyle() + ";-fx-border-color: red;");
        }

        if (!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red;");
        }
        if (!isValidPhone) {
            txtPhone.setStyle(txtPhone.getStyle() + ";-fx-border-color: red;");
        }

        InstructorDto instructorDto = new  InstructorDto(
                id,
                fullName,
                phone,
                email,
                availabilityNote
        );

        if(!isValidName && isValidEmail && isValidPhone) {
            try {
                instructorBO.updateInstructor(instructorDto);
                resetPage();
                new Alert(
                        Alert.AlertType.INFORMATION, "Instructor updated successfully..!"
                ).show();
            } catch (DuplicateException e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR, "Fail to updated Instructor..!"
                ).show();
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are your sure ?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES){
            try {
                String id = lblInstructorId.getText();
                boolean isDelete = instructorBO.deleteInstructor(id);

                if(isDelete) {
                    resetPage();
                    new Alert(
                            Alert.AlertType.INFORMATION, "Instructor deleted successfully."
                    ).show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete Instructor.").show();

                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR, "Fail to delete Instructor..!"
                ).show();
            }catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {

        InstructorTM selectItem = tblInstructor.getSelectionModel().getSelectedItem();

        if(selectItem != null) {
            lblInstructorId.setText(selectItem.getId());
            txtFullName.setText(selectItem.getFullName());
            txtPhone.setText(selectItem.getPhone());
            txtEmail.setText(selectItem.getEmail());
            txtAvailability.setText(selectItem.getAvailabilityNote());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }


}
