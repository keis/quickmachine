package quickmachine;

class SymbolicResult extends Result {

    public SymbolicResult() {
    }

    @Override
    public Object get() {
        return new SymbolicVar();
    }

    @Override
    public boolean isException() {
        throw new UnsupportedOperationException("Can not check value of symbolic Result");
    }
}
