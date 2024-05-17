package utils.constraints;

public class MinConstraint implements AbstractConstraint<Long> {

    private final long min;

    public MinConstraint(long minValue) {
        this.min = minValue;
    }

    @Override
    public boolean checkConstraint(Long currentValue) {
        return currentValue >= min;
    }
}
