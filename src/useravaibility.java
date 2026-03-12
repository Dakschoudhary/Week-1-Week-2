import java.util.*;

class UsernameChecker {


    private HashMap<String, Integer> usernameToId = new HashMap<>();


    private HashMap<String, Integer> attemptFrequency = new HashMap<>();

    private int nextUserId = 1;


    public void registerUser(String username) {
        usernameToId.put(username, nextUserId++);
    }


    public boolean checkAvailability(String username) {


        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        return !usernameToId.containsKey(username);
    }


    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();


        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!usernameToId.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }


        String alt = username.replace('_', '.');

        if (!usernameToId.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }


    public String getMostAttempted() {

        String result = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {

            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                result = entry.getKey();
            }
        }

        return result + " (" + maxCount + " attempts)";
    }
}

public class useravaibility {

    public static void main(String[] args) {

        UsernameChecker system = new UsernameChecker();

        system.registerUser("john_doe");
        system.registerUser("admin");

        System.out.println(system.checkAvailability("john_doe"));   // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        System.out.println(system.suggestAlternatives("john_doe"));

        System.out.println("Most Attempted: " + system.getMostAttempted());
    }
}