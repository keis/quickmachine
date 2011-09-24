package quickmachine;

import java.util.List;
import net.java.quickcheck.generator.support.EnsuredValuesGenerator;

public abstract class StateMachine {
    final protected EnsuredValuesGenerator commands;

    public StateMachine(List<Call> commands) {
        this.commands = new EnsuredValuesGenerator(commands);
    }
    
    public abstract DynamicState initialState();
    
    /**
     * Generates a symbolic call to be included in the command sequence
     */
    Call command(State state) {
        return (Call) this.commands.next();
    }
}
