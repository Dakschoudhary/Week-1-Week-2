import java.util.*;
import java.util.concurrent.*;

class TokenBucket {

    private int maxTokens;
    private double refillRate;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(int maxTokens, double refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = maxTokens;
        this.lastRefillTime = System.currentTimeMillis();
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double tokensToAdd = (now - lastRefillTime) / 1000.0 * refillRate;
        tokens = Math.min(maxTokens, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    public synchronized boolean allowRequest() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    public int remainingTokens() {
        return (int) tokens;
    }
}

class RateLimiter {

    private ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private int limit = 1000;
    private double refillRate = 1000.0 / 3600;

    public String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId, new TokenBucket(limit, refillRate));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {
            return "Allowed (" + bucket.remainingTokens() + " requests remaining)";
        }

        return "Denied (0 requests remaining)";
    }

    public String getRateLimitStatus(String clientId) {

        TokenBucket bucket = clients.get(clientId);

        if (bucket == null) {
            return "No usage yet";
        }

        int used = limit - bucket.remainingTokens();

        return "{used: " + used + ", limit: " + limit + "}";
    }
}

public class Main {

    public static void main(String[] args) {

        RateLimiter limiter = new RateLimiter();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}