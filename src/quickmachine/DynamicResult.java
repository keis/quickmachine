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
    public boolean isEqual(Object obj) {
        return this.obj.equals(obj);
    }

    @Override
    public boolean isInstance(Class cls) {
        return cls.isInstance(this.get());
    }
}
