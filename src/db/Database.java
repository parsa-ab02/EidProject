package db;

import db.exception.EntityNotFoundExeption;
import db.exception.InvalidEntityException;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static HashMap<Integer, Validator> validators;
    private static ArrayList<Entity> entities = new ArrayList<>();
    private Database(){
    }
    public static void add(Entity entity) {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            try {
                validator.validate(entity);
            }catch (InvalidEntityException e){
                System.out.println("Entity is invalid: " + e.getMessage());
                return;
            }
        }
        entity.id = entities.size();
        entities.add(entity.copy());
    }
    public static Entity get(int id) throws EntityNotFoundExeption {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity.copy();
            }
        }
        throw new EntityNotFoundExeption();
    }
    public static void delete(int id) throws EntityNotFoundExeption {
        for (Entity entity : entities) {
            if (entity.id == id) {
                entities.remove(entity);
                return;
            }
        }
        throw new EntityNotFoundExeption();
    }
    public static void update(Entity entity) throws EntityNotFoundExeption {
        Validator validator = validators.get(entity.getEntityCode());
        if (validator != null) {
            try {
                validator.validate(entity);
            }catch (InvalidEntityException e){
                System.out.println("Entity is invalid: " + e.getMessage());
                return;
            }
        }
        for (Entity entity1 : entities) {
            if (entity1.id == entity.id){
                entities.remove(entity1);
                entities.add(entity.copy());
                return;
            }
        }
        throw new EntityNotFoundExeption();
    }
    public static void registerValidator(int entityCode, Validator validator) {
        if(validators.containsKey(entityCode)){
            throw new IllegalArgumentException("Entity with code " + entityCode + " already exists");
        }else{
            validators.put(entityCode, validator);
        }
    }
}