package db;
public class EntityNotFoundExeption extends RuntimeException {
    public EntityNotFoundExeption() {
        super("Cannot find entity");
    }
    public EntityNotFoundExeption(String message) {
        super(message);
    }
    public EntityNotFoundExeption(int id){
        super("Cannot find entity with id=" + id);
    }
}