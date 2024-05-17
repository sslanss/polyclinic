package utils.constraints;

public class NotNullConstraint<T> implements AbstractConstraint<T> {
    @Override
    public boolean checkConstraint(T currentValue) {
        return currentValue != null;
    }
}
