package taskException;

public class TimeTaskConflictException extends IllegalArgumentException {
    public TimeTaskConflictException(String massage) {
        super(massage);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
