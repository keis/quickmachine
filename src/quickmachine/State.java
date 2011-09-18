package quickmachine;

import java.util.HashMap;
import java.util.Map;

public abstract class State {
    private final Map<String, Object> data;
    protected final State prior;

    protected State() {
        this.prior = null;
        this.data = new HashMap<>();
    }
    
    protected State(State prior) {
        this.prior = prior;
        this.data = new HashMap<>(prior.data);
    }
    
    protected void copy(State state) {
        this.data.putAll(state.data);
    }
    
    public Object get(String key) {
        return this.data.get(key);
    }
        
    public boolean has(String key) {
        return this.data.containsKey(key);
    }
    
    public State set(String key, Object o) {
        this.data.put(key, o);
        return this;
    }
    
    public State unset(String key) {
        this.data.remove(key);
        return this;
    }

    public String toString() {
        return "State{" + "data=" + data + '}';
    }
    

    public abstract State next();
}
