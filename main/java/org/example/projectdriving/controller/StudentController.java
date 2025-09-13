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
import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.bo.exception.InUseException;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.dto.StudentDto;
import org.example.projectdriving.dto.tm.CourseTM;
import org.example.projectdriving.dto.tm.StudentTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StudentController implements Initializable {
    public Label lblStudentId;
    public TextField txtName;
    public TextField txtNic;
    public TextField txtEmail;
    public TextField txtPhone;
    public ComboBox<CourseDto> cmbCourse;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<StudentTM> tblStudent;
    public TableColumn<StudentTM, String> colId;
    public TableColumn<StudentTM, String> colName;
    public TableColumn<StudentTM, String> colNic;
    public TableColumn<StudentTM, String> colEmail;
    public TableColumn<StudentTM, String> colPhone;
    public TableColumn<StudentTM, String> colCourse;


    private final StudentBO studentBO = BOFactory.getInstance().getBO(BOTypes.STUDENT);
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);
    //  private final StudentDto studentDto = new  StudentDto();

    private List<CourseDto> allCourses;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("course"));

        try {

            loadCourses();
            resetPage();

            loadStudents();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
            e.printStackTrace();
        }

    }

    private void loadStudents() {
        try {
            List<StudentDto> students = studentBO.getAllStudents();
            ObservableList<StudentTM> studentList = FXCollections.observableArrayList(
                    students.stream().map(dto -> new StudentTM(
                            dto.getId(),
                            dto.getFullName(),
                            dto.getNic(),
                            dto.getEmail(),
                            dto.getPhone(),
                            String.join(", ", dto.getCourseIds())
                    )).toList()
            );
            tblStudent.setItems(studentList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading students: " + e.getMessage()).show();
        }
    }


    private void loadCourses() throws SQLException {

        allCourses = courseBO.getAllCourses();
        ObservableList<CourseDto> courseList = FXCollections.observableArrayList(allCourses);
        cmbCourse.setItems(courseList);


        cmbCourse.setCellFactory(lv -> new ListCell<CourseDto>() {
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getName()); // show "id - name"
                }
            }
        });


        cmbCourse.setButtonCell(new ListCell<CourseDto>() {
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " - " + item.getName());
                }
            }
        });
    }

    private void resetPage() throws SQLException {
        loadNextId();
        loadTableData();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);

        txtName.setText("");
        txtNic.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbCourse.setValue(null);
    }

    private void loadTableData() throws SQLException {
        tblStudent.setItems(FXCollections.observableArrayList(
                studentBO.getAllStudents().stream().map(studentDto ->
                        new StudentTM(
                                studentDto.getId(),
                                studentDto.getFullName(),
                                studentDto.getEmail(),
                                studentDto.getPhone(),
                                studentDto.getNic(),
                                String.join(", ", studentDto.getCourseIds())
                        )).toList()
        ));
    }

    private void loadNextId() throws SQLException {
        String nextId = studentBO.getNextId();
        lblStudentId.setText(nextId);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
           List<CourseDto> selectedCourses = List.of(cmbCourse.getValue());
        try {
         //  CourseDto selectedCourses = cmbCourse.getValue();
            if (selectedCourses == null || selectedCourses.isEmpty()) {
               new Alert(Alert.AlertType.ERROR, "Please select a course").show();
                return;
            }

            StudentDto dto = new StudentDto(
                    lblStudentId.getText(),
                    txtName.getText(),
                    txtNic.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    selectedCourses.stream().map(CourseDto::getId).collect(Collectors.toList())
            );
            studentBO.saveStudent(dto);
            new Alert(Alert.AlertType.INFORMATION, "Student saved successfully").show();
            resetPage();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save data: " + e.getMessage()).show();
        }


    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        List<CourseDto> selectedCourses = List.of(cmbCourse.getValue());
        try {
           // CourseDto selectedCourse = cmbCourse.getValue();
            if (selectedCourses == null || selectedCourses.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select a course").show();
                return;
            }

            StudentDto dto = new StudentDto(
                    lblStudentId.getText(),
                    txtName.getText(),
                    txtNic.getText(),
                    txtEmail.getText(),
                    txtPhone.getText(),
                    selectedCourses.stream().map(CourseDto::getId).collect(Collectors.toList())
            );
            studentBO.updateStudent(dto);
            new Alert(Alert.AlertType.INFORMATION, "Student update successfully").show();
            resetPage();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to update data: " + e.getMessage()).show();
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
        if (response.isPresent() && response.get() == ButtonType.YES) {
            try {
                String studentId = lblStudentId.getText();
                boolean isDelete = studentBO.deleteStudent(studentId);

                if (isDelete) {
                    resetPage();
                    new Alert(
                            Alert.AlertType.INFORMATION, "Student deleted successfully."
                    ).show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete Student.").show();

                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR, "Fail to delete Student..!"
                ).show();
            }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {

        StudentTM selectedItem = tblStudent.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblStudentId.setText(selectedItem.getId());
            txtName.setText(selectedItem.getFullName());
            txtNic.setText(selectedItem.getNic());
            txtEmail.setText(selectedItem.getEmail());
            txtPhone.setText(selectedItem.getPhone());
            cmbCourse.getItems().stream()
                    .filter(c -> c.getId().equals(selectedItem.getCourse()))
                    .findFirst()
                    .ifPresent(cmbCourse::setValue);
        }

        btnSave.setDisable(true);

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }


}

