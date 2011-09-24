package quickmachine;

abstract public class Call {
    private final String description;

    public Call() {
        this.description = "Anonymous call";
    }
    
    public Call(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

    public String toString() {
        return "Call{" + description + '}';
    }
    
    /**
     * Checks the precondition that should hold so that Call can be
     * included in the command sequence
     * 
     * Default implementation always returns true
     * 
     * @param state the current state
     * @return true if the Call can be performed in the current state
     */
    public boolean precondition(State state) {
        return true;
    }

    /**
     * Checks the postcondition that should hold about the result of
     * performing Call, given the state of the abstract state machine
     * prior to command execution
     * 
     * Default implementation evaluates to false if the result is an Exception
     * 
     * @param prior The state prior to the call
     * @param res The result of the call
     * @return true if the postconditions holds
     */
    public boolean postcondition(DynamicState prior, Result res) {
        return !Exception.class.isInstance(res.get());
    }

    /**
     * Specifies the next state of the abstract state machine. The value of res
     * should not be used in conditionals as it may be Symbolic in this call.
     * 
     * Default implementation yields an identical state
     * 
     * @param state the current state
     * @param res the result of the call, can be either symbolic or dynamic
     * @return the new state
     * @throws Exception
     */
    public State nextState(State state, Result res) {
        return state.next();
    }
    
    /**
     * Performs the call
     * @param state the current state, not to be modified
     * @return the result of the call or null
     * @throws Exception 
     */
    public abstract Object call(DynamicState state) throws Exception;
}
