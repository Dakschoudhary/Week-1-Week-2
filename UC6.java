import java.util.*;

public class UC6{
    public static void main(String[] args) {
        String str = "madam";

        Queue<Character> queue = new LinkedList<>();
        Stack<Character> stack = new Stack<>();

        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            queue.add(c);
            stack.push(c);
        }

        boolean isPalindrome = true;

        while(!queue.isEmpty()) {
            if(queue.remove() != stack.pop()) {
                isPalindrome = false;
                break;
            }
        }

        if(isPalindrome) {
            System.out.println("Palindrome");
        } else {
            System.out.println("Not Palindrome");
        }
    }
}