import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    Map<String, Integer> queries = new HashMap<>();
    boolean isEnd = false;
}

class AutocompleteSystem {

    private TrieNode root = new TrieNode();
    private Map<String, Integer> frequency = new HashMap<>();

    public void addQuery(String query) {

        frequency.put(query, frequency.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
            node.queries.put(query, frequency.get(query));
        }

        node.isEnd = true;
    }

    public List<String> search(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : node.queries.entrySet()) {
            pq.offer(entry);
            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> result = new ArrayList<>();

        while (!pq.isEmpty()) {
            result.add(0, pq.poll().getKey() + " (" + pq.peek() + ")");
        }

        return result;
    }
}

public class Autocomplete {

    public static void main(String[] args) {

        AutocompleteSystem system = new AutocompleteSystem();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");

        List<String> suggestions = system.search("jav");

        for (String s : suggestions) {
            System.out.println(s);
        }

        system.addQuery("java 21 features");
        system.addQuery("java 21 features");
    }
}