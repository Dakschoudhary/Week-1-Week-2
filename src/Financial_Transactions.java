import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    String account;
    long time;

    Transaction(int id, int amount, String merchant, String account, long time) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}

class TransactionAnalyzer {

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<int[]> findTwoSum(int target) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public List<int[]> findTwoSumWithWindow(int target, long windowMillis) {

        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction prev = map.get(complement);

                if (Math.abs(t.time - prev.time) <= windowMillis) {
                    result.add(new int[]{prev.id, t.id});
                }
            }

            map.put(t.amount, t);
        }

        return result;
    }

    public List<List<Integer>> findKSum(int k, int target) {

        List<List<Integer>> result = new ArrayList<>();
        backtrack(0, k, target, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int start, int k, int target, List<Integer> path, List<List<Integer>> result) {

        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(path));
            return;
        }

        if (k <= 0) return;

        for (int i = start; i < transactions.size(); i++) {

            Transaction t = transactions.get(i);

            path.add(t.id);
            backtrack(i + 1, k - 1, target - t.amount, path, result);
            path.remove(path.size() - 1);
        }
    }

    public Map<String, List<String>> detectDuplicates() {

        Map<String, List<String>> duplicates = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            duplicates.putIfAbsent(key, new ArrayList<>());
            duplicates.get(key).add(t.account);
        }

        return duplicates;
    }
}

public class Financial_Transactions {

    public static void main(String[] args) {

        TransactionAnalyzer analyzer = new TransactionAnalyzer();

        analyzer.addTransaction(new Transaction(1, 500, "Store A", "acc1", System.currentTimeMillis()));
        analyzer.addTransaction(new Transaction(2, 300, "Store B", "acc2", System.currentTimeMillis()));
        analyzer.addTransaction(new Transaction(3, 200, "Store C", "acc3", System.currentTimeMillis()));

        List<int[]> pairs = analyzer.findTwoSum(500);

        for (int[] p : pairs) {
            System.out.println("TwoSum Pair: " + p[0] + ", " + p[1]);
        }

        List<List<Integer>> ksum = analyzer.findKSum(3, 1000);

        for (List<Integer> list : ksum) {
            System.out.println("KSum: " + list);
        }

        System.out.println(analyzer.detectDuplicates());
    }
}