package quickmachine;

abstract public class Result {

    abstract public Object get();

    public String toString() {
        Object v = this.get();
        return "Result{" + (v == null ? "null" : v.toString()) + '}';
    }

    /**
     * Check if result is an exception.
     *
     * @return true if the result is an exception
     */
    public abstract boolean isException();
}
