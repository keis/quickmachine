package quickmachine;

abstract public class Result {
    abstract public Object get();

    public String toString() {
        Object v = this.get();
        return "Result{" + (v == null ? "null" : v.toString()) + '}';
    }
}
