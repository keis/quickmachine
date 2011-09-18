package quickmachine;

public class DynamicState extends State {

    public DynamicState() {
        super();
    }
    
    public DynamicState(DynamicState prior) {
        super(prior);
    }
    
    @Override
    public State next() {
        return new DynamicState(this);
    }
}
