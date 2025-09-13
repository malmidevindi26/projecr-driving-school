package org.example.projectdriving.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.CourseBO;
import org.example.projectdriving.bo.Custom.InstructorBO;
import org.example.projectdriving.bo.Custom.LessonBO;
import org.example.projectdriving.bo.Custom.StudentBO;
import org.example.projectdriving.bo.exception.InUseException;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.dto.InstructorDto;
import org.example.projectdriving.dto.LessonDto;
import org.example.projectdriving.dto.StudentDto;
import org.example.projectdriving.dto.tm.CourseTM;
import org.example.projectdriving.dto.tm.LessonTM;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LessonController implements Initializable {
    public Label lblLessonId;
    public ComboBox<StudentDto> cmbStudent;
    public ComboBox<CourseDto> cmbCourse;
    public ComboBox<InstructorDto> cmbInstructor;
    public DatePicker dpStartDate;
    public TextField txtStartTime;
    public DatePicker dpEndDate;
    public TextField txtEndTime;
    public ComboBox<String> cmbStatus;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<LessonTM> tblLesson;
    public TableColumn<LessonTM, String> colId;
    public TableColumn<LessonTM, String> colStudent;
    public TableColumn<LessonTM, String> colCourse;
    public TableColumn<LessonTM, String> colInstructor;
    public TableColumn<LessonTM, String> colStart;
    public TableColumn<LessonTM, String> colEnd;
    public TableColumn<LessonTM, String> colStatus;

    private final LessonBO lessonBO = BOFactory.getInstance().getBO(BOTypes.LESSON);
    private final StudentBO studentBO = BOFactory.getInstance().getBO(BOTypes.STUDENT);
    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);
    private final InstructorBO instructorBO = BOFactory.getInstance().getBO(BOTypes.INSTRUCTOR);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colInstructor.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        try {
            resetPage();
            setupStudentListener();

            cmbStatus.setItems(FXCollections.observableArrayList(
                    "Scheduled", "Completed", "Cancelled"
            ));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Fail to load data..!").show();
        }

        txtStartTime.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                LocalTime start = LocalTime.parse(newValue);
                LocalTime end = start.plusHours(1);


                if (start.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(17, 0))) {

                    System.out.println("Lesson must be between 08:00 and 17:00");
                    txtEndTime.clear();
                    return;
                }
                txtEndTime.setText(end.toString());

            } catch (Exception e) {

                System.out.println("Invalid time format: " + newValue);
                txtEndTime.clear();
                e.printStackTrace();
            }
        });
    }

    private void loadAllData() {
        loadStudents();
        loadCourses();
        loadInstructors();
    }

    private void resetPage() throws SQLException {


        loadNextId();
        loadTableData();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);


        cmbStudent.setValue(null);
        cmbCourse.setValue(null);
        cmbInstructor.setValue(null);


        cmbStatus.setValue(null);
        txtStartTime.setText("");
        txtEndTime.setText("");
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);


        loadAllData();

    }

    public void loadCourses() {
        try {
            List<CourseDto> courses = courseBO.getAllCourses();
            cmbCourse.getItems().clear();
            setupCourseDisplay();
            cmbCourse.getItems().addAll(courses);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load courses: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void loadInstructors() {
        try {
            List<InstructorDto> instructors = instructorBO.getAllInstructors();
            cmbInstructor.getItems().clear();
            setupInstructorDisplay();
            cmbInstructor.getItems().addAll(instructors);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load instructors: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try {
            List<LessonTM> lessonList = lessonBO.getAllLessons().stream().map(lessonDto ->
                    new LessonTM(
                            lessonDto.getId(),
                            lessonDto.getStartTime(),
                            lessonDto.getEndTime(),
                            lessonDto.getStudentId(),
                            lessonDto.getCourseId(),
                            lessonDto.getInstructorId(),
                            lessonDto.getStatus()
                    )
            ).toList();

            tblLesson.setItems(FXCollections.observableArrayList(lessonList));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load lessons: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }


    private void loadNextId() throws SQLException {
        String nextId = lessonBO.getNextId();
        lblLessonId.setText(nextId);
    }

    public void setupStudentListener(){
        cmbStudent.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStudent, newStudent) -> {
                    if(newStudent != null) {
                        loadCourseForStudent(newStudent);
                    }else {

                        cmbCourse.getItems().clear();
                        cmbInstructor.getItems().clear();
                    }
                }
        );
    }

    private void loadCourseForStudent(StudentDto student) {
        try {
            cmbCourse.getItems().clear();
            setupCourseDisplay();
            List<CourseDto> studentCourses = courseBO.getCoursesByStudent(student.getId());
            cmbCourse.getItems().addAll(studentCourses);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load courses for student: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    public void setupCourseDisplay(){
        cmbCourse.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });

        cmbCourse.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CourseDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {

        String lessonId = lblLessonId.getText();
        StudentDto student = cmbStudent.getValue();
        CourseDto course = cmbCourse.getValue();
        InstructorDto instructor = cmbInstructor.getValue();
        String status = cmbStatus.getValue();
        LocalDate startDate = dpStartDate.getValue();
        String startTimeText = txtStartTime.getText();


        if (student == null || course == null || instructor == null || startDate == null ||
                startTimeText.isEmpty() || status == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").show();
            return;
        }

        try {

            LocalTime start = LocalTime.parse(startTimeText);

            LocalDateTime startDateTime = LocalDateTime.of(startDate, start);

            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(17, 0);
            LocalTime end = start.plusHours(1);

            if (start.isBefore(open) || end.isAfter(close)) {
                new Alert(Alert.AlertType.ERROR, "Lesson must be scheduled between 08:00 and 17:00.").show();
                return;
            }


            LessonDto lessonDto = new LessonDto(
                    lessonId,
                    startDateTime,
                    null,
                    student.getId(),
                    course.getId(),
                    instructor.getId(),
                    status
            );

            lessonBO.saveLesson(lessonDto);

            new Alert(Alert.AlertType.INFORMATION, "Lesson saved successfully!").show();
            resetPage();

        } catch (java.time.format.DateTimeParseException e) {

            new Alert(Alert.AlertType.ERROR, "Invalid time format. Please use HH:mm (e.g., 09:00)").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save lesson: " + e.getMessage()).show();
            e.printStackTrace();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String lessonId = lblLessonId.getText();
        StudentDto student = cmbStudent.getValue();
        CourseDto course = cmbCourse.getValue();
        InstructorDto instructor = cmbInstructor.getValue();
        String status = cmbStatus.getValue();
        LocalDate endDate = dpEndDate.getValue();
        LocalDate startDate = dpStartDate.getValue();
        String startTimeText = txtStartTime.getText();
        String endTimeText = txtEndTime.getText();

        if (student == null || course == null || instructor == null || startDate == null ||
                startTimeText.isEmpty() || endTimeText.isEmpty() || status == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields!").show();
            return;
        }

        boolean isValidTime = startTimeText.matches("^([01]\\d|2[0-3]):([0-5]\\d)$");
        txtStartTime.setStyle(txtStartTime.getStyle() + ";-fx-border-color: #BB25B9;");

        if (!isValidTime) {
            txtStartTime.setStyle(txtStartTime.getStyle() + ";-fx-border-color: red;");
            new Alert(Alert.AlertType.ERROR, "Invalid start time format. Use HH:mm").show();
            return;
        }

        try {
            LocalTime start = LocalTime.parse(startTimeText);
            LocalTime end = LocalTime.parse(endTimeText);

            if (start.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(17, 0))) {
                txtStartTime.setStyle(txtStartTime.getStyle() + ";-fx-border-color: red;");
                new Alert(Alert.AlertType.ERROR, "Lesson must be between 08:00 and 17:00").show();
                return;
            }
            java.time.LocalDateTime startDateTime = java.time.LocalDateTime.of(startDate, start);
            java.time.LocalDateTime endDateTime = java.time.LocalDateTime.of(endDate, end);

            LessonDto lessonDto = new LessonDto(
                    lessonId,
                    startDateTime,
                    endDateTime,
                    student.getId(),
                    course.getId(),
                    instructor.getId(),
                    status
            );

            lessonBO.updateLesson(lessonDto);


            new Alert(Alert.AlertType.INFORMATION, "Lesson saved successfully!").show();
            resetPage();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load lessons: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are your sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES){
           try {
               String lessonId = lblLessonId.getText();
               boolean isDelete = lessonBO.deleteLesson(lessonId);

               if (isDelete){
                   resetPage();
                   new Alert(Alert.AlertType.INFORMATION, "Lesson deleted successfully!").show();
               }else {
                   new Alert(Alert.AlertType.ERROR, "Failed to delete lesson!").show();

               }
           }catch (InUseException e){
               new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
               e.printStackTrace();
           }catch (Exception e){
               e.printStackTrace();
               new Alert(
                       Alert.AlertType.ERROR, "Fail to delete customer..!"
               ).show();

           }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException {
        loadNextId();
        loadTableData();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        LessonTM selectedItem = tblLesson.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            lblLessonId.setText(selectedItem.getId());
            StudentDto student = cmbStudent.getItems().stream()
                    .filter(s -> s.getId().equals(selectedItem.getStudentId()))
                    .findFirst()
                    .orElse(null);
            cmbStudent.setValue(student);

            CourseDto course = cmbCourse.getItems().stream()
                    .filter(c -> c.getId().equals(selectedItem.getCourseId()))
                    .findFirst()
                    .orElse(null);
            cmbCourse.setValue(course);

            InstructorDto instructor = cmbInstructor.getItems().stream()
                    .filter(i -> i.getId().equals(selectedItem.getInstructorId()))
                    .findFirst()
                    .orElse(null);
            cmbInstructor.setValue(instructor);

            cmbStatus.setValue(selectedItem.getStatus());

            btnSave.setDisable(true);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);

        }

    }
    @FXML
    private void onCourseSelected(ActionEvent event) {

        CourseDto course = cmbCourse.getSelectionModel().getSelectedItem();
        if (course != null) {
            loadInstructorsForCourse(course);
        }
        updateEndDate();
    }

    @FXML
    private void onStartTimeEntered(ActionEvent event) {
        try {
            LocalTime start = LocalTime.parse(txtStartTime.getText());
            LocalTime end = start.plusHours(1);


            if (start.isBefore(LocalTime.of(8, 0)) || end.isAfter(LocalTime.of(17, 0))) {
                new Alert(Alert.AlertType.ERROR, "Lesson must be between 08:00 and 17:00").show();
                txtEndTime.clear();
                return;
            }

            txtEndTime.setText(end.toString());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Invalid start time format. Use HH:mm").show();
            e.printStackTrace();
        }
    }

    private void loadInstructorsForCourse(CourseDto course) {
        try {
            cmbInstructor.getItems().clear();
            setupInstructorDisplay();
            List<InstructorDto> instructors = instructorBO.getInstructorsByCourse(course.getId());
            cmbInstructor.getItems().addAll(instructors);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load instructors: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void setupStudentDisplay() {

        cmbStudent.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(StudentDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getId() + " - " + item.getFullName());
            }
        });


        cmbStudent.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(StudentDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getId() + " - " + item.getFullName());
            }
        });
    }

    public void loadStudents() {
        try {
            List<StudentDto> students = studentBO.getAllStudents();
            cmbStudent.getItems().clear();


            setupStudentDisplay();


            cmbStudent.getItems().addAll(students);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load students: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }
    private void setupInstructorDisplay() {
        cmbInstructor.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(InstructorDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });

        cmbInstructor.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(InstructorDto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getFullName());
            }
        });
    }


    public void onStartDateSelected(ActionEvent actionEvent) {
        updateEndDate();
    }

    private void updateEndDate() {
        CourseDto course = cmbCourse.getSelectionModel().getSelectedItem();
        LocalDate startDate = dpStartDate.getValue();

        if (course != null && startDate != null) {
            try {
                int durationInWeeks = Integer.parseInt(course.getDuration());
                dpEndDate.setValue(startDate.plusWeeks(durationInWeeks));
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid course duration format. Please enter a valid number of weeks.").show();
                e.printStackTrace();
            }
        }
    }


}
