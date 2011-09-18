package quickmachine;

class SymbolicResult extends Result {

    public SymbolicResult() {
    }

    @Override
    public Object get() {
        return new SymbolicVar();
    }
}
