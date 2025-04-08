package todo.validator;

import db.Entity;
import db.Validator;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import db.Database;
import db.exception.EntityNotFoundExeption;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        if (!(entity instanceof Step)) {
            throw new IllegalArgumentException("Provided entity is not a Step.");
        }
        Step step = (Step) entity;
        if (step.title == null || step.title.trim().isEmpty()) {
            throw new InvalidEntityException("Step title cant be empty.");
        }
        try {
            Entity taskEntity = Database.get(step.taskRef);
            if (!(taskEntity instanceof todo.entity.Task)) {
                throw new InvalidEntityException("Entity with ID=" + step.taskRef + " is not a valid Task.");
            }
        } catch (EntityNotFoundExeption e) {
            throw new InvalidEntityException("Cant find task with ID=" + step.taskRef);
        }
    }
}
