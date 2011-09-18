package quickmachine.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Auth {
    final Map<String, String> users;
    final Map<String, String> cookie;
    final Map<String, List<String>> permission;
    final Map<String, String> tokens;

    public Auth(Map<String, String> users, Map<String, List<String>> permission) {
        this.users = users;
        this.permission = permission;
        this.cookie = new HashMap<>();
        this.tokens = new HashMap<>();
    }
    
    public String login(String username, String password) throws Exception {
        if (password.equals(this.users.get(username))) {
            String session = UUID.randomUUID().toString();
            this.cookie.put(session, username);
            return session;
        }
        throw new Exception("bad username / password");
    }
    
    public void logout(String session) throws Exception {
        if (!this.cookie.containsKey(session)) {
            throw new Exception("no session");
        }
        this.cookie.remove(session);
    }
    
    public String createToken(String session, String domain) throws Exception {
        if (!this.cookie.containsKey(session)) {
            throw new Exception("no session");
        }
        
        if (!this.permission.containsKey(domain)) {
            throw new Exception("bad domain");
        }
        
        String user = this.cookie.get(session);
        List<String> allowed = this.permission.get(domain);
        
        if (!allowed.contains(user)) {
            throw new Exception("access denied");
        }
        
        String token = UUID.randomUUID().toString();
        this.tokens.put(token, domain);
        return token;
    }
    
    public void revokeToken(String session, String token) throws Exception {
        if (!this.tokens.containsKey(token)) {
            throw new Exception("bad token");
        }
        this.tokens.remove(token);
    }
    
    public String access(String domain, String token) throws Exception {
        if (!this.tokens.containsKey(token)) {
            throw new Exception("no token");
        }
        if (!this.tokens.get(token).equals(domain)) {
            throw new Exception("bad token");
        }
        
        return domain;
    }
}
