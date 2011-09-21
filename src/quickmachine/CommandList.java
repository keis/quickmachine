package quickmachine;

import java.util.ArrayList;
import java.util.List;

public class CommandList extends ArrayList<Call> {
    private final StateMachine statem;

    public CommandList(StateMachine statem) {
        this.statem = statem;
    }
    
    public State execute() {
        DynamicState state = this.statem.initialState();
        DynamicResult res = null;
        History history = new History();
        
        for(Call call : this) {
            //System.out.print(call.toString() + " @ " + state.toString());
            
            // Make the call
            try {
                res = new DynamicResult(call.call(state));
            } catch (Exception e) {
                res = new DynamicResult(e);
            }
            
            //System.out.println(res.toString());
                        
            history.add(call, state, res);
            
            // Check post conditions (could Assert be used to closer integrate with JUnit?)
            if (!call.postcondition(state, res)) {
                System.out.print(history.format());
                
                throw new RuntimeException("Post conditions of " + call.toString() + " does not hold");
            }

            // Advance to the next state
            state = (DynamicState) call.nextState(state, res);
        }
        
        return state;
    }
}
