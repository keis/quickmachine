package quickmachine;

public class PostConditionError extends RuntimeException {

    protected final History history;

    public PostConditionError(History history) {
        super("Post conditions of " + history.last().call.toString() + " does not hold"
                + "\n" + history.format());
        this.history = history;
    }

    public PostConditionError(History history, String message) {
        super(message
                + "\n" + history.format());
        this.history = history;
    }

    public History getHistory() {
        return history;
    }
}
