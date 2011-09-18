package quickmachine;

abstract public class Result {

    abstract public Object get();

    public String toString() {
        Object v = this.get();
        return "Result{" + (v == null ? "null" : v.toString()) + '}';
    }

    /**
     * Check if result is an exception.
     * Should not be used on symbolic results
     *
     * @return true if the result is an exception
     */
    public boolean isException() {
        return this.isInstance(Exception.class);
    }

    /**
     * Check if the value of the result is equal to obj
     * Should not be used on symbolic results
     *
     * @param obj
     * @return true if the value is equal to obj
     */
    public abstract boolean isEqual(Object obj);

    /**
     * Check if result is an instance of cls
     * Should not be used on symbolic results
     *
     * @param cls class to check if result is an instance of
     * @return true if the value is an instance of cls
     */
    public abstract boolean isInstance(Class cls);
}
