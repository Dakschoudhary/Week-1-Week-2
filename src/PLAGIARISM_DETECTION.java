import java.util.*;

class PlagiarismDetector {

    private int n;
    private Map<String, Set<String>> ngramIndex = new HashMap<>();
    private Map<String, List<String>> documentNgrams = new HashMap<>();

    public PlagiarismDetector(int n) {
        this.n = n;
    }

    private List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    public void addDocument(String docId, String text) {
        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    public void analyzeDocument(String docId) {

        List<String> ngrams = documentNgrams.get(docId);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {
            Set<String> docs = ngramIndex.getOrDefault(gram, new HashSet<>());

            for (String d : docs) {
                if (!d.equals(docId)) {
                    matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                }
            }
        }

        for (String otherDoc : matchCount.keySet()) {
            int matches = matchCount.get(otherDoc);
            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches + " matching n-grams with " + otherDoc);
            System.out.println("Similarity: " + similarity + "%");

            if (similarity > 60) {
                System.out.println("PLAGIARISM DETECTED");
            }
        }
    }
}

public class PLAGIARISM_DETECTION {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        String doc1 = "machine learning is a field of artificial intelligence that focuses on learning from data";
        String doc2 = "machine learning is a field of artificial intelligence used in many applications";
        String doc3 = "sports and fitness are important for a healthy lifestyle";

        detector.addDocument("essay_089.txt", doc1);
        detector.addDocument("essay_092.txt", doc2);
        detector.addDocument("essay_123.txt", doc1);

        detector.analyzeDocument("essay_123.txt");
    }
}