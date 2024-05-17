package utils.validation.validators;

import java.util.HashSet;
import java.util.Set;
import utils.constraints.AbstractConstraint;

public class FieldValidator<T> {

    private final Set<AbstractConstraint<T>> constraints;


    public FieldValidator() {
        constraints = new HashSet<>();
    }

    public FieldValidator<T> add(AbstractConstraint<T> constraint) {
        constraints.add(constraint);
        return this;
    }

    public boolean validate(T value) {
        for (var constraint : constraints) {
            if (!constraint.checkConstraint(value)) {
                return false;
            }
        }
        return true;
    }
}
