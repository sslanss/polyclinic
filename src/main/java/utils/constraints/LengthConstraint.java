package utils.constraints;

public class LengthConstraint implements AbstractConstraint<String> {

    private final long length;

    public LengthConstraint(long lengthValue) {
        this.length = lengthValue;
    }

    @Override
    public boolean checkConstraint(String currentString) {
        return currentString.length() == length;
    }
}
