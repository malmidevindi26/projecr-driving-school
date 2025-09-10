package org.example.projectdriving.bo;

import org.example.projectdriving.bo.Custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {

    }
    public static BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public <T extends SuperBO> T getBO(BOTypes boType) {
        return switch (boType){
            case INSTRUCTOR -> (T) new InstructorBOImpl();
            case LESSON -> (T) new LessonBOImpl();
            case PAYMENT ->  (T) new PaymentBOImpl();
            case COURSE -> (T) new CourseBOImpl();
            case STUDENT -> (T) new StudentBOImpl();
        };
    }
}
