package quickmachine;

import java.util.ArrayList;
import java.util.List;

public class CommandList extends ArrayList<Call> {
    private final StateMachine statem;

    public CommandList(StateMachine statem) {
        this.statem = statem;
    }
    
    String formatHistory(List<Call> history, Call lastCall, DynamicResult lastResult, DynamicState lastState) {
        StringBuilder sb = new StringBuilder();
        int lastnl = 0;
        for(Call call : history) {
            sb.append(call);
            sb.append(", ");
            if(sb.length() - lastnl > 70) {
                sb.append("\n");
                lastnl = sb.length();
            }
        }
        
        sb.append('\n');
        sb.append("Post conditions of ");
        sb.append(lastCall);
        sb.append(" does not hold:");
        sb.append("\nresult: ");
        sb.append(lastResult);
        sb.append("\nstate: ");
        sb.append(lastState);
        return sb.toString();
    }
    
    public State execute() {
        DynamicState state = this.statem.initialState();
        DynamicResult res = null;
        List<Call> history = new ArrayList<>();
        
        for(Call call : this) {
            //System.out.print(call.toString() + " @ " + state.toString());
            
            // Make the call
            try {
                res = new DynamicResult(call.call(state));
            } catch (Exception e) {
                res = new DynamicResult(e);
            }
            
            //System.out.println(res.toString());
                        
            history.add(call);
            
            // Check post conditions (could Assert be used to closer integrate with JUnit?)
            if (!call.postcondition(state, res)) {
                System.out.print(this.formatHistory(history, call, res, state));
                
                throw new RuntimeException("Post conditions of " + call.toString() + " does not hold");
            }

            // Advance to the next state
            state = (DynamicState) call.nextState(state, res);
        }
        
        return state;
    }
}
