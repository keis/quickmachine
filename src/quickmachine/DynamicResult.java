package quickmachine;

class DynamicResult extends Result {
    private final Object obj;

    public DynamicResult(Object obj) {
        this.obj = obj;
    }
    
    @Override
    public Object get() {
        return obj;
    }

    @Override
    public boolean isException() {
        return Exception.class.isInstance(this.get());
    }
}
