package db;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private Database(){
    }
    public static void add(Entity entity) {
        entity.id = entities.size();
        entities.add(entity);
    }
    public static Entity get(int id) throws EntityNotFoundExeption {
        for (Entity entity : entities) {
            if (entity.id == id) {
                return entity;
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
        for (Entity entity1 : entities) {
            if (entity1.id == entity.id){
                entities.remove(entity1);
                entities.add(entity);
                return;
            }
        }
        throw new EntityNotFoundExeption();
    }
}