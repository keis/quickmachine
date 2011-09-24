package quickmachine;

public class ExecutionContext {

    final protected StateMachine statem;
    final protected History history;
    protected DynamicState state;

    public ExecutionContext(StateMachine statem) {
        this.statem = statem;
        this.state = this.statem.initialState();
        this.history = new History();
    }

    public DynamicResult execute(Call call) throws PostConditionError {
        DynamicResult res = null;

        // Make the call and record it in the history
        try {
            res = new DynamicResult(call.call(this.state));
        } catch (Exception e) {
            res = new DynamicResult(e);
        }
        history.add(call, state, res);

        // Check post conditions
        if (!call.postcondition(state, res)) {
            throw new PostConditionError(history);
        }

        // Advance to the next state
        this.state = (DynamicState) call.nextState(this.state, res);

        return res;
    }

    public History execute(CommandList calls) throws PostConditionError {
        for (Call call : calls) {
            this.execute(call);
        }

        return this.history;
    }
}
