package utils.constraints;

public interface AbstractConstraint<T> {
    boolean checkConstraint(T currentValue);
}
