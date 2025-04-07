package example;

import db.Entity;
import db.exception.InvalidEntityException;
import db.Validator;

public class HumanValidator implements Validator {
    @Override
    public void validate(Entity entity) throws InvalidEntityException {
        // Code for human validation
    }
}