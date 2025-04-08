package todo.service;

import db.Database;
import db.exception.EntityNotFoundExeption;
import todo.entity.Task;
import todo.entity.Step;
import java.util.ArrayList;

public class TaskService {
    public static void setAsCompleted(int taskId) {
        try {
            Task task = (Task) Database.get(taskId);
            task.status = Task.Status.Completed;
            Database.update(task);
            ArrayList<db.Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            for (db.Entity entity : steps) {
                Step step = (Step) entity;
                if (step.taskRef == taskId) {
                    step.status = Step.Status.Completed;
                    Database.update(step);
                }
            }
        } catch (EntityNotFoundExeption e) {
            System.out.println("Cannot set task as completed. Error: " + e.getMessage());
        }
    }
}