package org.example.projectdriving.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.CourseBO;
import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.dto.StudentDto;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AssignNewCourseController implements Initializable {
    public ComboBox<StudentDto> cmbStudent;
    public ListView<CourseDto> lvAvailableCourses;
    public ListView<CourseDto> lvEnrolledCourses;
    public Button btnSaveEnrollment;

    private final StudentBO studentBO = BOFactory.getInstance().getBO(BOTypes.STUDENT);
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);
    public Button btnReset;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadStudents();
       loadCourses();

       lvAvailableCourses.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

       setupComboxDisplay();
       setupListViewDisplay();

       cmbStudent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
           if (newVal != null) {
               displayEnrolledCourses(newVal);
           }else {
               lvEnrolledCourses.getItems().clear();
           }
       });
    }

    private void loadStudents() {
        try {
            List<StudentDto> students = studentBO.getAllStudents();
            cmbStudent.setItems(FXCollections.observableArrayList(students));
         } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load students");
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        try {
            List<CourseDto> coursees = courseBO.getAllCourses();
            lvAvailableCourses.setItems(FXCollections.observableArrayList(coursees));

        }catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load courses");
            e.printStackTrace();
        }
    }

    private void setupComboxDisplay() {
        cmbStudent.setCellFactory(lv -> new ListCell<StudentDto>(){
            @Override
            protected void updateItem(StudentDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getId() + " - " + item.getFullName());
            }
        });
        cmbStudent.setButtonCell(cmbStudent.getCellFactory().call(null));
    }

    private void setupListViewDisplay() {
        lvAvailableCourses.setCellFactory((lv -> new ListCell<CourseDto>(){
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getId() + " - " + item.getName());
            }
        }));

        lvEnrolledCourses.setCellFactory(lv -> new  ListCell<CourseDto>(){
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getId() + " - " + item.getName());
            }
        });
    }

    private void displayEnrolledCourses(StudentDto student) {

        try {
            List<CourseDto> enrolledCourses = courseBO.getCoursesByStudent(student.getId());
            lvEnrolledCourses.setItems(FXCollections.observableArrayList(enrolledCourses));
        } catch (SQLException e) {
            new  Alert(Alert.AlertType.ERROR, "Failed to load enrolled courses");
        }
    }

    public void btnSaveEnrollmentOnAction(ActionEvent actionEvent) {
        StudentDto selectedStudent = cmbStudent.getSelectionModel().getSelectedItem();
        ObservableList<CourseDto> selectedCourses = lvAvailableCourses.getSelectionModel().getSelectedItems();

        if (selectedStudent == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a student to enroll.").show();
            return;
        }
        if (selectedCourses.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select at least one course.").show();
            return;
        }

        try {

            List<String> enrolledCourseIds = courseBO.getCoursesByStudent(selectedStudent.getId()).stream()
                    .map(CourseDto::getId).collect(Collectors.toList());

            List<String> newCourseIds = selectedCourses.stream()
                    .map(CourseDto::getId).collect(Collectors.toList());

            for (String newId : newCourseIds) {
                if (enrolledCourseIds.contains(newId)) {
                    new Alert(Alert.AlertType.WARNING, "Student is already registered for this course").show();
                    return;
                }
            }

            enrolledCourseIds.addAll(newCourseIds);
            selectedStudent.setCourseIds(enrolledCourseIds);

            studentBO.updateStudent(selectedStudent);

            new Alert(Alert.AlertType.INFORMATION, "Successfully enrolled course").show();

            displayEnrolledCourses(selectedStudent);
            lvAvailableCourses.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load enrolled courses").show();
            e.printStackTrace();
        }
    }


    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    private void resetPage() {
        cmbStudent.getSelectionModel().clearSelection();
        lvAvailableCourses.getSelectionModel().clearSelection();
        lvEnrolledCourses.getItems().clear();
    }
}
