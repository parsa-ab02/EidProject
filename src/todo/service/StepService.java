package todo.service;

import db.Database;
import db.exception.InvalidEntityException;
import todo.entity.Step;

public class StepService {
    public static void saveStep(int taskRef, String title) throws InvalidEntityException {
        Step newStep = new Step(title, taskRef);
        Database.add(newStep);
    }
}
