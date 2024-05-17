package utils.constraints;

public class MaxConstraint implements AbstractConstraint<Long> {
    private final long max;

    public MaxConstraint(long maxValue) {
        this.max = maxValue;
    }

    @Override
    public boolean checkConstraint(Long currentValue) {
        return currentValue <= max;
    }
}
