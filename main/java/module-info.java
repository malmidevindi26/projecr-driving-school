module org.example.projectdriving {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires static lombok;


    opens org.example.projectdriving to jakarta.persistence, org.hibernate.orm.core, javafx.fxml, javafx.base;

    exports org.example.projectdriving;
}