package org.example.projectdriving.config;

import org.example.projectdriving.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private SessionFactory sessionFactory;

    private FactoryConfiguration(){

        Configuration configuration = new Configuration().configure();

        configuration.addAnnotatedClass(CourseEntity.class);
        configuration.addAnnotatedClass(UserEntity.class);
        configuration.addAnnotatedClass(StudentEntity.class);
        configuration.addAnnotatedClass(LessonEntity.class);
        configuration.addAnnotatedClass(PaymentEntity.class);
        configuration.addAnnotatedClass(EnrollmentEntity.class);
        configuration.addAnnotatedClass(InstructorEntity.class);

        sessionFactory = configuration.buildSessionFactory();

    }
    public static FactoryConfiguration getInstance(){
        if(factoryConfiguration == null){
            factoryConfiguration = new FactoryConfiguration();
        }
        return factoryConfiguration;
    }
    public Session getSession(){
        return sessionFactory.openSession();
    }
}
