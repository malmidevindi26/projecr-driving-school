module org.example.projectdriving {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.projectdriving to javafx.fxml;
    exports org.example.projectdriving;
}