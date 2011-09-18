package quickmachine.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import quickmachine.Call;
import quickmachine.CommandList;
import quickmachine.CommandListGenerator;
import quickmachine.DynamicState;
import quickmachine.Result;
import quickmachine.State;
import quickmachine.StateMachine;
import quickmachine.SymbolicState;

public class TestAuth {

    private final StateMachine statem;

    public TestAuth() {
        Map<String, String> users = new HashMap<>();
        Map<String, List<String>> permissions = new HashMap<>();
        final Auth auth = new Auth(users, permissions);

        users.put("Alice", "passw0rd");
        users.put("Bob", "p@ssword");

        permissions.put("spam", Arrays.asList("Alice", "Bob"));
        permissions.put("egg", Arrays.asList("Alice"));

        this.statem = new StateMachine(Arrays.asList(
                new Login(auth, "Alice", "passw0rd"),
                new Login(auth, "Bob", "p@ssword"),
                new Call("Invalid Login") {

                    @Override
                    public boolean precondition(SymbolicState state) {
                        return !state.has("cookie");
                    }

                    @Override
                    public boolean postcondition(DynamicState prior, Result res) {
                        return res.isException();
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        return auth.login("Eve", "Evil");
                    }
                },
                new Call("Logout") {

                    @Override
                    public State nextState(State state, Result res) {
                        return state.next().unset("cookie").unset("user");
                    }

                    @Override
                    public boolean precondition(SymbolicState state) {
                        return state.has("cookie");
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        auth.logout((String) state.get("cookie"));
                        return null;
                    }
                },
                new Call("Get Egg token") {

                    @Override
                    public State nextState(State state, Result res) {
                        // Should be a NOP for all users except Alice
                        if (state.has("cookie") && state.get("user").equals("Alice")) {
                            return state.next().set("egg_token", res.get());
                        }
                        return state.next();
                    }

                    @Override
                    public boolean postcondition(DynamicState prior, Result res) {
                        if (!prior.has("cookie")) {
                            return res.isException();
                        }

                        String user = (String) prior.get("user");
                        switch (user) {
                            case "Alice":
                                return String.class.isInstance(res.get());
                            case "Bob":
                                return res.isException();
                            default:
                                throw new RuntimeException("Unexpected state");
                        }
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        return auth.createToken((String) state.get("cookie"), "egg");
                    }
                },
                new Call("Access Egg") {

                    @Override
                    public boolean postcondition(DynamicState prior, Result res) {
                        if (!prior.has("egg_token")) {
                            return res.isException();
                        }

                        if (!prior.has("user") || !prior.get("user").equals("Alice")) {
                            return res.isException();
                        }

                        return String.class.isInstance(res.get());
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        return auth.access("egg", (String) state.get("egg_token"));
                    }
                },
                new Call("Get Spam token") {

                    @Override
                    public State nextState(State state, Result res) {
                        if (state.has("cookie")) {
                            return state.next().set("spam_token", res.get());
                        }
                        return state.next();
                    }

                    @Override
                    public boolean postcondition(DynamicState prior, Result res) {
                        if (!prior.has("cookie")) {
                            return res.isException();
                        }
                        return String.class.isInstance(res.get());
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        return auth.createToken((String) state.get("cookie"), "spam");
                    }
                },
                new Call("Access Spam") {

                    @Override
                    public boolean postcondition(DynamicState prior, Result res) {
                        if (!prior.has("spam_token")) {
                            return res.isException();
                        }

                        if (!prior.has("user")) {
                            return res.isException();
                        }

                        return String.class.isInstance(res.get());
                    }

                    @Override
                    public Object call(DynamicState state) throws Exception {
                        return auth.access("spam", (String) state.get("spam_token"));
                    }
                })) {

            @Override
            public DynamicState initialState() {
                return new DynamicState();
            }
        };
    }

    private void run() {
        CommandListGenerator clg = new CommandListGenerator(this.statem);
        CommandList cl = clg.next();
        State s = cl.execute();
        System.out.println(s.toString());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestAuth sm = new TestAuth();
        sm.run();
    }

    static class Login extends Call {

        private final Auth auth;
        private final String user;
        private final String password;

        public Login(Auth auth, String user, String password) {
            super("Login as " + user);
            this.auth = auth;
            this.user = user;
            this.password = password;
        }

        @Override
        public State nextState(State state, Result res) {
            return state.next().set("user", this.user).set("cookie", res.get());
        }

        @Override
        public boolean precondition(SymbolicState state) {
            return !state.has("cookie");
        }

        @Override
        public Object call(DynamicState state) throws Exception {
            return auth.login(this.user, this.password);
        }
    }
}
