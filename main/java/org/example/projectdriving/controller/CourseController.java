package org.example.projectdriving.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.projectdriving.bo.BOFactory;
import org.example.projectdriving.bo.BOTypes;
import org.example.projectdriving.bo.Custom.CourseBO;
import org.example.projectdriving.bo.exception.DuplicateException;
import org.example.projectdriving.bo.exception.InUseException;
import org.example.projectdriving.dto.CourseDto;
import org.example.projectdriving.dto.tm.CourseTM;
import org.example.projectdriving.entity.CourseEntity;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    private final CourseBO courseBO = BOFactory.getInstance().getBO(BOTypes.COURSE);

    public Label lblCourseId;
    public TextField txtName;
    public TextField txtDuration;
    public TextField txtFee;
    public TextArea txtDescription;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;
    public TableView<CourseTM> tblCourse;
    public TableColumn<CourseTM,String> colId;
    public TableColumn <CourseTM,String>colName;
    public TableColumn <CourseTM,String>colDuration;
    public TableColumn <CourseTM, BigDecimal>colFee;
    public TableColumn <CourseTM,String>colDescription;
    public ComboBox <String>comCourse;

    private final String numberPattern = "^(0|[1-9][0-9]*)(\\.[0-9]+)?$";
    private final Map<String, CourseDto> courseMap = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        try {
            resetPage();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
            e.printStackTrace();
        }
    }

    private void resetPage() throws SQLException {
        loadNextId();
        loadTableData();

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);

        txtDuration.setText("");
        txtFee.setText("");
        txtDescription.setText("");
        txtName.setText("");

    }

    private void loadTableData() throws SQLException {
        tblCourse.setItems(FXCollections.observableArrayList(
                courseBO.getAllCourses().stream().map(courseDto ->
                        new CourseTM(
                                courseDto.getId(),
                                courseDto.getName(),
                                courseDto.getDuration(),
                                courseDto.getFee(),
                                courseDto.getDescription()
                        )).toList()
        ));
    }

    private void loadNextId() throws SQLException {
        String nextId = courseBO.getNextId();
        lblCourseId.setText(nextId);
    }



    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException {
        String name = comCourse.getValue();
        String duration = txtDuration.getText();
        String feeText = txtFee.getText();
        String description = txtDescription.getText();
        String courseId = lblCourseId.getText();

        boolean isValidFee = feeText.matches(numberPattern);

        txtFee.setStyle(txtFee.getStyle() + ";-fx-border-color: #BB25B9;");

        if (!isValidFee) {
            txtFee.setStyle(txtFee.getStyle() + ";-fx-border-color: red;");
        }

        BigDecimal fee = new BigDecimal(feeText);
        CourseDto courseDto = new CourseDto(
                courseId,
                name,
                duration,
                fee,
                description
        );
      if(isValidFee){
          try {
              courseBO.saveCourse(courseDto);

              resetPage();
              new Alert(
                      Alert.AlertType.INFORMATION, "Course saved successfully..!"
              ).show();
          }catch (DuplicateException e) {
              System.out.println(e.getMessage());

              new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
          } catch (Exception e) {
              e.printStackTrace();
              new Alert(
                      Alert.AlertType.ERROR, "Fail to save Course..!"
              ).show();
          }
      }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String name = comCourse.getValue();
        String duration = txtDuration.getText();
        String feeText = txtFee.getText();
        String description = txtDescription.getText();
        String courseId = lblCourseId.getText();

        boolean isValidFee = feeText.matches(numberPattern);

        txtFee.setStyle(txtFee.getStyle() + ";-fx-border-color: #BB25B9;");

        if (!isValidFee) {
            txtFee.setStyle(txtFee.getStyle() + ";-fx-border-color: red;");
        }

        BigDecimal fee = new BigDecimal(feeText);
        CourseDto courseDto = new CourseDto(
                courseId,
                name,
                duration,
                fee,
                description
        );
        if(isValidFee){
            try {
                courseBO.updateCourse(courseDto);

                resetPage();
                new Alert(
                        Alert.AlertType.INFORMATION, "Course Updated successfully..!"
                ).show();
            }catch (DuplicateException e) {
                System.out.println(e.getMessage());

                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(
                        Alert.AlertType.ERROR, "Fail to Update Course..!"
                ).show();
            }
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
               String courseId = lblCourseId.getText();
               boolean isDelete = courseBO.deleteCourse(courseId);

               if (isDelete){
                   resetPage();
                   new Alert(
                           Alert.AlertType.INFORMATION, "Course deleted successfully."
                   ).show();
               }else {
                   new Alert(Alert.AlertType.ERROR, "Fail to delete Course.").show();

               }
           }catch (InUseException e) {
               new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
           } catch (Exception e) {
               e.printStackTrace();
               new Alert(
                       Alert.AlertType.ERROR, "Fail to delete customer..!"
               ).show();
           }
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) throws SQLException {
        loadNextId();
        resetPage();
        loadTableData();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CourseTM selectedItem = tblCourse.getSelectionModel().getSelectedItem();

        if(selectedItem != null){
            lblCourseId.setText(selectedItem.getId());
            txtName.setText(selectedItem.getName());
            txtDuration.setText(selectedItem.getDuration());
            txtFee.setText(String.valueOf(selectedItem.getFee()));
            txtDescription.setText(selectedItem.getDescription());

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }


    }

}
