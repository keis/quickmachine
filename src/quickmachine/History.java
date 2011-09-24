package quickmachine;

import java.util.ArrayList;
import java.util.List;

public class History {

    class Item {

        public Call call;
        public State state;
        public Result result;

        Item(Call call, State state, Result result) {
            this.call = call;
            this.state = state;
            this.result = result;
        }
    }
    List<Item> history;

    public History() {
        this.history = new ArrayList<>();
    }

    public boolean add(Call call, State state, Result result) {
        return this.history.add(new Item(call, state, result));
    }

    Item last() throws IndexOutOfBoundsException {
        return this.history.get(this.history.size() - 1);
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        int lastnl = 0;
        for (Item item : this.history) {
            sb.append(item.call);
            sb.append(", ");
            if (sb.length() - lastnl > 70) {
                sb.append("\n");
                lastnl = sb.length();
            }
        }
        Item last = this.last();

        sb.append('\n');
        sb.append("Post conditions of ").append(last.call).append(" does not hold\n");
        sb.append("result: ").append(last.result).append("\n");
        sb.append("state: ").append(last.state).append("\n");

        return sb.toString();
    }

    public String toString() {
        Item last = this.last();
        return "History{"
                + this.history.size() + "call(s), "
                + last.call.toString() + ", "
                + last.result.toString() + "}";
    }
}
