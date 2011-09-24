package quickmachine;

public class SymbolicState extends State {

    public SymbolicState() {
        super();
    }
    
    public SymbolicState(State prior) {
        super(prior);
    }
    
    public SymbolicState(DynamicState reference) {
        super();
        this.copy(reference);
    }

    @Override
    public State next() {
        return new SymbolicState(this);
    }
}
