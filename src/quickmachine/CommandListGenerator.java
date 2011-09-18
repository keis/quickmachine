package quickmachine;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.support.IntegerGenerator;

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
        this.targetSize = new IntegerGenerator(this.minSize, this.maxSize);
    }

    @Override
    public CommandList next() {
        CommandList list = new CommandList(this.statem);
        SymbolicState state = new SymbolicState(this.statem.initialState());
        Call nextCommand = null;
        int size = this.targetSize.next();
        
        while (list.size() < size) {
            try {
                nextCommand = this.yieldCommand(state);
            } catch (Exception e) {
                if (list.size() > this.minSize) {
                    return list;
                } else {
                    throw new UnsupportedOperationException("Failed to create commandlist");
                }
            }
            
            list.add(nextCommand);
            state = (SymbolicState) nextCommand.nextState(state, new SymbolicResult());
        }
        return list;
    }

    private Call yieldCommand(SymbolicState state) throws Exception {
        int run = 0;
        Call call = null;
        do {
            call = this.statem.command(state);
            if(++run > maxTries) {
                throw new Exception("Could not get suitable command");
            }
        } while (!call.precondition(state));
        return call;
    }
}
