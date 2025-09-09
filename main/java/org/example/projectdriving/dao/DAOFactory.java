package org.example.projectdriving.dao;

import org.example.projectdriving.dao.custome.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }
    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }
    public <T extends SuperDAO> T getDAO(DAOTypes daoType) {
        return switch (daoType){
            case STUDENT -> (T) new StudentDAOImpl();
            case LESSON -> (T) new LessonDAOImpl();
            case PAYMENT -> (T) new PaymentDAOImpl();
            case COURSE -> (T) new CourseDAOImpl();
            case ENROLLMENT -> (T) new EnrollmentDAOImpl();
            case INSTRUCTOR ->  (T) new InstructorDAOImpl();
        };
    }
}
