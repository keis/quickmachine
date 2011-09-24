package quickmachine;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

public class CommandListGenerator implements Generator<CommandList> {

    private final StateMachine statem;
    private final int maxTries;
    private final int minSize;
    private final int maxSize;
    private final Generator<Integer> targetSize;

    public CommandListGenerator(StateMachine statem) {
        this.statem = statem;
        this.maxTries = 10;
        this.minSize = 5;
        this.maxSize = 512;
        this.targetSize = PrimitiveGenerators.integers(this.minSize, this.maxSize);
    }

    @Override
    public CommandList next() {
        CommandList list = new CommandList(this.statem);
        State state = new SymbolicState(this.statem.initialState());
        Call command = null;
        int size = this.targetSize.next();

        while (list.size() < size) {
            try {
                command = this.yieldCommand(state);
            } catch (Exception e) {
                if (list.size() > this.minSize) {
                    return list;
                } else {
                    throw new UnsupportedOperationException("Failed to create commandlist");
                }
            }

            list.add(command);
            state = (SymbolicState) command.nextState(state,
                    new SymbolicResult());
        }
        return list;
    }

    private Call yieldCommand(State state) throws Exception {
        int run = 0;
        Call call = null;
        do {
            call = this.statem.command(state);
            if (++run > maxTries) {
                throw new Exception("Could not get suitable command");
            }
        } while (!call.precondition(state));
        return call;
    }
}
