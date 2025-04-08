import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import db.Database;
import db.Entity;
import db.exception.EntityNotFoundExeption;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.entity.Task.Status;
import todo.entity.Step;
import todo.service.TaskService;
import todo.service.StepService;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        while(true) {
            System.out.println("Enter command:");
            String command = scanner.nextLine().trim();
            if(command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program.");
                break;
            }
            try {
                if(command.equalsIgnoreCase("add task")) {
                    handleAddTask();
                } else if (command.equalsIgnoreCase("add step")) {
                    handleAddStep();
                } else if (command.equalsIgnoreCase("delete")) {
                    handleDelete();
                } else if (command.equalsIgnoreCase("update task")) {
                    handleUpdateTask();
                } else if (command.equalsIgnoreCase("update step")) {
                    handleUpdateStep();
                } else if (command.equalsIgnoreCase("get task-by-id")) {
                    handleGetTaskById();
                } else if (command.equalsIgnoreCase("get all-tasks")) {
                    handleGetAllTasks();
                } else if (command.equalsIgnoreCase("get incomplete-tasks")) {
                    handleGetIncompleteTasks();
                } else {
                    System.out.println("Unknown command.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
    private static void handleAddTask() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        System.out.print("Due date (yyyy-MM-dd): ");
        String dueDateStr = scanner.nextLine().trim();
        Date dueDate = null;
        try {
            dueDate = dateFormat.parse(dueDateStr);
        } catch(ParseException e) {
            System.out.println("Invalid date format.");
            return;
        }
        Task task = new Task(title, description, dueDate);
        try {
            Database.add(task);
            System.out.println("Task saved successfully.");
            System.out.println("ID: " + task.id);
        } catch (InvalidEntityException e) {
            System.out.println("Cannot save task.");
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void handleAddStep() throws InvalidEntityException {
        System.out.print("TaskID: ");
        int taskId = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        StepService.saveStep(taskId, title);
    }
    private static void handleDelete() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        try {
            Entity entity = Database.get(id);
            Database.delete(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
            if(entity instanceof Task) {
                ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
                for (Entity e : steps) {
                    Step step = (Step) e;
                    if(step.taskRef == id) {
                        Database.delete(step.id);
                    }
                }
            }
        } catch (EntityNotFoundExeption e) {
            System.out.println("Cannot delete entity with ID=" + id);
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void handleUpdateTask() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Field: ");
        String field = scanner.nextLine().trim();
        System.out.print("New Value: ");
        String newValue = scanner.nextLine().trim();
        try {
            Entity entity = Database.get(id);
            if (!(entity instanceof Task)) {
                System.out.println("Entity with ID=" + id + " is not a Task.");
                return;
            }
            Task task = (Task) entity;
            String oldValue = "";
            if(field.equalsIgnoreCase("title")) {
                oldValue = task.title;
                task.title = newValue;
            } else if (field.equalsIgnoreCase("description")) {
                oldValue = task.description;
                task.description = newValue;
            } else if (field.equalsIgnoreCase("dueDate")) {
                try {
                    Date newDueDate = dateFormat.parse(newValue);
                    oldValue = task.dueDate != null ? dateFormat.format(task.dueDate) : "null";
                    task.dueDate = newDueDate;
                } catch (ParseException e) {
                    System.out.println("Invalid date format.");
                    return;
                }
            } else if (field.equalsIgnoreCase("status")) {
                oldValue = task.status.toString();
                if(newValue.equalsIgnoreCase("Completed")) {
                    task.status = Task.Status.Completed;
                    ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
                    for(Entity e : steps) {
                        Step step = (Step) e;
                        if(step.taskRef == task.id)
                            step.status = Step.Status.Completed;
                    }
                }else if(newValue.equalsIgnoreCase("NotStarted")) {
                    task.status = Task.Status.NotStarted;
                } else if(newValue.equalsIgnoreCase("InProgress")) {
                    task.status = Task.Status.InProgress;
                } else {
                    System.out.println("Invalid status value.");
                    return;
                }
            } else {
                System.out.println("Invalid field name.");
                return;
            }
            Database.update(task);
            System.out.println("Successfully updated the task.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date().toString());
        } catch (EntityNotFoundExeption e) {
            System.out.println("Cannot update task with ID=" + id);
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void handleUpdateStep() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Field: ");
        String field = scanner.nextLine().trim();
        System.out.print("New Value: ");
        String newValue = scanner.nextLine().trim();
        try {
            Entity entity = Database.get(id);
            if (!(entity instanceof Step)) {
                System.out.println("Entity with ID=" + id + " is not a Step.");
                return;
            }
            Step step = (Step) entity;
            String oldValue = "";
            if(field.equalsIgnoreCase("title")) {
                oldValue = step.title;
                step.title = newValue;
            } else if (field.equalsIgnoreCase("status")) {
                oldValue = step.status.toString();
                if(newValue.equalsIgnoreCase("Completed")) {
                    step.status = Step.Status.Completed;
                } else if(newValue.equalsIgnoreCase("NotStarted")) {
                    step.status = Step.Status.NotStarted;
                } else {
                    System.out.println("Invalid status value for step.");
                    return;
                }
            } else {
                System.out.println("Invalid field name for step.");
                return;
            }
            Database.update(step);
            System.out.println("Successfully updated the step.");
            System.out.println("Field: " + field);
            System.out.println("Old Value: " + oldValue);
            System.out.println("New Value: " + newValue);
            System.out.println("Modification Date: " + new Date().toString());
        } catch (EntityNotFoundExeption e) {
            System.out.println("Cannot update step with ID=" + id);
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void handleGetTaskById() {
        System.out.print("ID: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        try {
            Entity entity = Database.get(id);
            if (!(entity instanceof Task)) {
                System.out.println("Entity with ID=" + id + " is not a Task.");
                return;
            }
            Task task = (Task) entity;
            System.out.println("ID: " + task.id);
            System.out.println("Title: " + task.title);
            System.out.println("Due Date: " + (task.dueDate != null ? dateFormat.format(task.dueDate) : "N/A"));
            System.out.println("Status: " + task.status);
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            if(steps.isEmpty()) {
                System.out.println("Steps: None");
            } else {
                System.out.println("Steps:");
                for(Entity e : steps) {
                    Step step = (Step) e;
                    if(step.taskRef == task.id) {
                        System.out.println("    + " + step.title + ":");
                        System.out.println("        ID: " + step.id);
                        System.out.println("        Status: " + step.status);
                    }
                }
            }
        } catch (EntityNotFoundExeption e) {
            System.out.println("Cannot find task with ID=" + id);
        }
    }
    private static void handleGetAllTasks() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_CODE);
        tasks.sort((a, b) -> {
            Task t1 = (Task) a;
            Task t2 = (Task) b;
            if(t1.dueDate == null || t2.dueDate == null)
                return 0;
            return t1.dueDate.compareTo(t2.dueDate);
        });
        for (Entity entity : tasks) {
            Task task = (Task) entity;
            System.out.println("ID: " + task.id);
            System.out.println("Title: " + task.title);
            System.out.println("Due Date: " + (task.dueDate != null ? dateFormat.format(task.dueDate) : "N/A"));
            System.out.println("Status: " + task.status);
            // نمایش قدم‌های مرتبط
            ArrayList<Entity> steps = Database.getAll(Step.STEP_ENTITY_CODE);
            boolean firstStep = true;
            for(Entity e : steps) {
                Step step = (Step) e;
                if(step.taskRef == task.id) {
                    if(firstStep) {
                        System.out.println("Steps:");
                        firstStep = false;
                    }
                    System.out.println("    + " + step.title + ":");
                    System.out.println("        ID: " + step.id);
                    System.out.println("        Status: " + step.status);
                }
            }
            System.out.println();
        }
    }
    private static void handleGetIncompleteTasks() {
        ArrayList<Entity> tasks = Database.getAll(Task.TASK_ENTITY_CODE);
        boolean found = false;
        for (Entity entity : tasks) {
            Task task = (Task) entity;
            if(task.status != Task.Status.Completed) {
                found = true;
                System.out.println("ID: " + task.id);
                System.out.println("Title: " + task.title);
                System.out.println("Due Date: " + (task.dueDate != null ? dateFormat.format(task.dueDate) : "N/A"));
                System.out.println("Status: " + task.status);
                System.out.println();
            }
        }
        if(!found) {
            System.out.println("No incomplete tasks found.");
        }
    }
}