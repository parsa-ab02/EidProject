package todo.entity;

import db.Entity;

public class Step extends Entity {
    public static final int STEP_ENTITY_CODE = 2;
    public String title;
    public Status status;
    public int taskRef;

    public enum Status {
        NotStarted,
        Completed
    }
    public Step(String title, int taskRef) {
        this.title = title;
        this.taskRef = taskRef;
        this.status = Status.NotStarted;
    }
    @Override
    public Entity copy() {
        Step copyStep = new Step(this.title, this.taskRef);
        copyStep.id = this.id;
        copyStep.status = this.status;
        return copyStep;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }
}