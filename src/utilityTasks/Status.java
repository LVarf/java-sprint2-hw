package utilityTasks;

public enum Status {
    NEW("New"),
    IN_PROGRESS(""),
    DONE("");
    private final String typeOfStatus;

    Status(String status) {
        this.typeOfStatus = status;
    }

    public String getName() {
        return typeOfStatus;
    }
}
