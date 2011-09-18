package quickmachine;

class SymbolicResult extends Result {
    private final SymbolicVar var;

    public SymbolicResult() {
        var = new SymbolicVar();
    }

    @Override
    public Object get() {
        return this.var;
    }

    @Override
    public boolean isInstance(Class cls) {
        throw new UnsupportedOperationException("Can not check value of symbolic Result");
    }

    @Override
    public boolean isEqual(Object obj) {
        throw new UnsupportedOperationException("Can not check value of symbolic Result");
    }
}
