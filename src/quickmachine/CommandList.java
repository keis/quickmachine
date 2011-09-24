package quickmachine;

import java.util.ArrayList;
import java.util.List;

public class CommandList extends ArrayList<Call> {

    private final StateMachine statem;

    public CommandList(StateMachine statem) {
        this.statem = statem;
    }
}
