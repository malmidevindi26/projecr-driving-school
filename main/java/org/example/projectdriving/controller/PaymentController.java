package org.example.projectdriving.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.CourseBO;
import org.example.projectdriving.bo.Custom.PaymentBO;
import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.dto.PaymentDto;
import org.example.projectdriving.dto.StudentDto;
import org.example.projectdriving.dto.tm.PaymentTM;
import org.example.projectdriving.dto.tm.StudentTM;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    public Label lblPaymentId;
    public ComboBox<String> cmbStudent;
    public ComboBox<String> cmbEnrollment;
    public TextField txtAmount;
    public ComboBox<String> cmbMethod;
    public ComboBox<String> cmbCourse;
    public TextField txtReference;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<PaymentTM> tblPayment;
    public TableColumn<PaymentTM, String> colId;
    public TableColumn<PaymentTM, String> colStudent;
    public TableColumn<PaymentTM, String> colEnrollment;
    public TableColumn<PaymentTM, String> colAmount;
    public TableColumn<PaymentTM, String> colMethod;
    public TableColumn<PaymentTM, String> colCourse;
    public TableColumn<PaymentTM, String> colReference;

    private final StudentBO studentBO = BOFactory.getInstance().getBO(BOTypes.STUDENT);
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);
    private final PaymentBO paymentBO = BOFactory.getInstance().getBO(BOTypes.PAYMENT);
    private static final BigDecimal REGISTRATION_FEE = new BigDecimal("5000.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         cmbMethod.setItems(FXCollections.observableArrayList("Cash", "Card", "Bank Transfer"));

         colId.setCellValueFactory(new PropertyValueFactory<>("id"));
         colStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
         colEnrollment.setCellValueFactory(new PropertyValueFactory<>("enrollment"));
         colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
         colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));
         colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
         colMethod.setCellValueFactory(new PropertyValueFactory<>("method"));

         try {
             loadStudentIds();
         } catch (Exception e) {
             e.printStackTrace();
         }

         cmbStudent.setOnAction(event -> handleStudentSelection());
         cmbEnrollment.setOnAction(event -> updateAmountField());

         cmbCourse.setOnAction(event ->handleCourseSelection());

        try {
            loadNextId();
            loadTableData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCourseSelection() {
        String studentId = cmbStudent.getSelectionModel().getSelectedItem();
        String course = cmbCourse.getSelectionModel().getSelectedItem();

        if(studentId != null && course != null){
            try {
                boolean ifFullPaid = paymentBO.isFullPaymentCompleted(studentId);
                int installmentCount = paymentBO.getPaidInstallments(studentId, course);

                if(ifFullPaid || installmentCount >=3){
                    new Alert(Alert.AlertType.INFORMATION, studentId + " student has completed the full payment ! ").show();
                    cmbEnrollment.setDisable(true);
                    txtAmount.setText("");
                }else {
                    cmbEnrollment.setDisable(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTableData() throws SQLException {
        tblPayment.setItems(FXCollections.observableArrayList(
                paymentBO.getAllPayments().stream().map(paymentDto ->
                        new PaymentTM(
                                paymentDto.getId(),
                                paymentDto.getAmount(),
                                paymentDto.getMethod(),
                                paymentDto.getCourse(),
                                paymentDto.getReference(),
                                paymentDto.getStudentId(),
                                paymentDto.getEnrollment()

                        )).toList()
        ));
    }

    private void loadNextId() throws SQLException {
        String nextId = paymentBO.getNextId();
        lblPaymentId.setText(nextId);
    }

    private void updateAmountField() {
        String studentId = cmbStudent.getSelectionModel().getSelectedItem();
        String selectedCourse = cmbCourse.getSelectionModel().getSelectedItem();
        String paymentType =  cmbEnrollment.getSelectionModel().getSelectedItem();

        if(studentId != null && selectedCourse != null && paymentType != null) {
            try {

                BigDecimal courseFee = paymentBO.getEnrolledCourseFee(studentId, selectedCourse);
                BigDecimal amount = BigDecimal.ZERO;

                if(paymentType.equals("Full Payment")){
                    amount = courseFee.add(REGISTRATION_FEE);
                }else if (paymentType.equals("Enrollment")){
                    int installmentCount = paymentBO.getPaidInstallments(studentId, selectedCourse);

                    if(installmentCount == 0){
                        amount = courseFee.multiply(new BigDecimal("0.40")).add(REGISTRATION_FEE);
                    } else if(installmentCount == 1){
                        amount = courseFee.multiply(new BigDecimal("0.30"));
                    }else if(installmentCount == 2){
                        amount = courseFee.multiply(new BigDecimal("0.30"));
                    }
                }
                BigDecimal finalAmount = amount.setScale(2, RoundingMode.HALF_UP);
                txtAmount.setText(finalAmount.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStudentSelection() {
        String selectedStudentId = cmbStudent.getSelectionModel().getSelectedItem();
        if(selectedStudentId != null) {
            try {

                List<String> enrolledCourses = paymentBO.getEnrolledCourses(selectedStudentId);
                cmbCourse.setItems(FXCollections.observableArrayList(enrolledCourses));

                cmbEnrollment.setItems(FXCollections.observableArrayList("Full Payment", "Enrollment"));

                cmbEnrollment.setDisable(false);
                cmbEnrollment.getSelectionModel().clearSelection();
                txtAmount.setText("");
                txtReference.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStudentIds() throws SQLException {
        List<StudentDto> students = studentBO.getAllStudents();
        ObservableList<String> studentIds = FXCollections.observableArrayList();
        for (StudentDto student  : students ) {
            studentIds.add(student.getId());
        }
        cmbStudent.setItems(studentIds);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {

        String studentId = cmbStudent.getSelectionModel().getSelectedItem();
        String course = cmbCourse.getSelectionModel().getSelectedItem();
        String enrollment = cmbEnrollment.getSelectionModel().getSelectedItem();

        try {

            BigDecimal amount = new BigDecimal(txtAmount.getText());
            String method =  cmbMethod.getSelectionModel().getSelectedItem();
            String reference = txtReference.getText();
            String id = lblPaymentId.getText();

            PaymentDto paymentDto = new PaymentDto(id, amount, method, course, reference,studentId, enrollment);

            paymentBO.savePayment(paymentDto);

            String message = "";
            if ("Full Payment".equals(enrollment)) {
                message = studentId + " student has completed the full payment.";
            } else {
                int installmentCount = paymentBO.getPaidInstallments(studentId, course);
                if (installmentCount == 1) {
                    message = "First installment completed.";
                } else if (installmentCount == 2) {
                    message = "Second installment completed.";
                } else if (installmentCount == 3) {
                    message = "Third installment completed and All payments completed.";
                }
            }
            new Alert(Alert.AlertType.INFORMATION, message).show();
            loadNextId();
            loadTableData();
            resetPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "Failed to save payment.").show();
        }


    }

    private void resetPage() throws SQLException {
        cmbStudent.setValue(null);
        cmbCourse.setValue(null);
        cmbMethod.setValue(null);
        cmbEnrollment.setValue(null);
        txtAmount.setText("");
        txtReference.setText("");
        cmbEnrollment.setDisable(true);


        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);
        loadNextId();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

        String studentId = cmbStudent.getSelectionModel().getSelectedItem();
        String course = cmbCourse.getSelectionModel().getSelectedItem();
        String enrollment = cmbEnrollment.getSelectionModel().getSelectedItem();

        try {

            BigDecimal amount = new BigDecimal(txtAmount.getText());
            String method =  cmbMethod.getSelectionModel().getSelectedItem();
            String reference = txtReference.getText();
            String id = lblPaymentId.getText();

            PaymentDto paymentDto = new PaymentDto(id, amount, method, course, reference,studentId, enrollment);

            paymentBO.updatePayment(paymentDto);

            String message = "";
            if ("Full Payment".equals(enrollment)) {
                message = studentId + " student has completed the full payment.";
            } else {
                int installmentCount = paymentBO.getPaidInstallments(studentId, course);
                if (installmentCount == 1) {
                    message = "First installment completed.";
                } else if (installmentCount == 2) {
                    message = "Second installment completed.";
                } else if (installmentCount == 3) {
                    message = "Third installment completed and All payments completed.";
                }
            }
            new Alert(Alert.AlertType.INFORMATION, message).show();
            loadNextId();
            loadTableData();
            resetPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "Failed to save payment.").show();
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
                String paymentId = lblPaymentId.getText();
                boolean isDelete = paymentBO.deletePayment(paymentId);

                if (isDelete) {
                    resetPage();
                    loadTableData();
                    new Alert(
                            Alert.AlertType.INFORMATION, "Pyment deleted successfully."
                    ).show();
                }else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete payment.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete payment.").show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {

        PaymentTM selectedItem = tblPayment.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblPaymentId.setText(selectedItem.getId());
            cmbStudent.setValue(selectedItem.getStudentId());
            cmbEnrollment.setValue(selectedItem.getEnrollment());
            cmbMethod.setValue(selectedItem.getMethod());
            cmbCourse.setValue(selectedItem.getCourse());
            txtReference.setText(selectedItem.getReference());
            txtAmount.setText(selectedItem.getAmount().toString());

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

}
