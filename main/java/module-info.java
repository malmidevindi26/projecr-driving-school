module org.example.projectdriving {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires static lombok;
    requires jbcrypt;


    opens org.example.projectdriving.entity to org.hibernate.orm.core;
    opens org.example.projectdriving.config to jakarta.persistence;
    opens org.example.projectdriving.controller to javafx.fxml;
    opens org.example.projectdriving.dto.tm to javafx.base;

    exports org.example.projectdriving;
}