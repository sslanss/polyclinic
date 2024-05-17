package utils.constraints;

public class NotEmptyConstraint implements AbstractConstraint<String> {
    @Override
    public boolean checkConstraint(String currentValue) {
        return !(currentValue.isEmpty() || currentValue.isBlank());
    }
}
