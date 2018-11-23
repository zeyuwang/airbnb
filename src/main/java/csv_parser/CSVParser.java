package csv_parser;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public class CSVParser {
    /*
        CSV Parser
        AirBnB Interview Question
        http://creativyst.com/Doc/Articles/CSV/CSV01.htm#EmbedBRs
     */
    public class Solution {
        public String parseCSV(String str) {
            List<String> res = new ArrayList<>(); // list of phrase for current csv line
            boolean inQuote = false;
            StringBuilder sb = new StringBuilder(); // sb for current phrase
            for (int i = 0; i < str.length(); i++) {
                if (inQuote) { // 1) when in quote
                    if (str.charAt(i) == '\"') { // 1.1) if encounter a quote
                        if (i < str.length() - 1 && str.charAt(i + 1) == '\"') { // 1.1.1) if it is double quote, put quote in current word
                            sb.append("\""); // the only time we put quote in to result
                            i++; // since we have count the next character, we need to skip it.
                        } else { // 1.1.2) if it is not double quote, exit current quote
                            inQuote = false;
                        }
                    } else { // 1.2) if not quote, we just append result to the current phrase, even it is a comma it is fine.
                        sb.append(str.charAt(i));
                    }
                } else { // 2) when not in quote:
                    if (str.charAt(i) == '\"') { // 2.1) if encounter a quote, then we are in quote
                        inQuote = true;
                    } else if (str.charAt(i) == ',') { // 2.2) if get a comma, add the current phrase to list and rest sb
                        res.add(sb.toString());
                        sb.setLength(0);
                    } else if (str.charAt(i) == ' ' && (sb.length() == 0)) { // avoid heading space
                         continue;
                    } else { // 2.3) if not quote nor a comma, append current char to current phrase
                        sb.append(str.charAt(i));
                    }
                }
            }

            for (int i = sb.length()-1; i > 0; i--) { // remove trailing space
                if (sb.charAt(i) == ' ' && sb.length() == i+1 ) {
                    sb.deleteCharAt(i);
                }
            }

            if (sb.length() > 0) {
                res.add(sb.toString());
            }
            return String.join("|", res); // do we really need to use a | as delimiter?
        }
    }

    /*
        CSV Parser
        AirBnB Interview Question
        http://creativyst.com/Doc/Articles/CSV/CSV01.htm#EmbedBRs
     */
    public class Solution_2 {
        public String parseCSV(String str) {
            if (str == null || str.isEmpty()) return null;
            List<String> res = new ArrayList<>();
            StringBuilder curr = new StringBuilder();
            boolean inQuote = false;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (inQuote) {
                    if (c == '\"') {
                        if (i < str.length() - 1 && str.charAt(i + 1) == '\"') {
                            curr.append("\"");
                            i++;
                        } else {
                            inQuote = false;
                        }
                    } else {
                        curr.append(c);
                    }
                } else {
                    if (c == '\"') {
                        inQuote = true;
                    } else if (c == ',') {
                        res.add(curr.toString());
                        curr.setLength(0);
                    } else {
                        curr.append(c);
                    }
                }
            }

            if (curr.length() > 0)
                res.add(curr.toString());

            return String.join("|", res);
        }
    }

    public static class UnitTest {
        @Test
        public void test1() {
            Solution sol = new CSVParser().new Solution();
            String test = "   John,Smith,john.smith@gmail.com,Los Angeles,1";
            String expected = "John|Smith|john.smith@gmail.com|Los Angeles|1";
            assertEquals(expected, sol.parseCSV(test));

            test = "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0";
            expected = "Jane|Roberts|janer@msn.com|San Francisco, CA|0";
            assertEquals(expected, sol.parseCSV(test));

            test = "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1";
            expected = "Alexandra \"Alex\"|Menendez|alex.menendez@gmail.com|Miami|1";
            assertEquals(expected, sol.parseCSV(test));

            test = "\"\"\"Alexandra Alex\"\"\"";
            expected = "\"Alexandra Alex\"";
            assertEquals(expected, sol.parseCSV(test));
        }

        @Test
        public void test2() {
            Solution_2 sol = new CSVParser().new Solution_2();
            String test = "John,Smith,john.smith@gmail.com,Los Angeles,1";
            String expected = "John|Smith|john.smith@gmail.com|Los Angeles|1";
            assertEquals(expected, sol.parseCSV(test));

            test = "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0";
            expected = "Jane|Roberts|janer@msn.com|San Francisco, CA|0";
            assertEquals(expected, sol.parseCSV(test));

            test = "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1";
            expected = "Alexandra \"Alex\"|Menendez|alex.menendez@gmail.com|Miami|1";
            assertEquals(expected, sol.parseCSV(test));

            test = "\"\"\"Alexandra Alex\"\"\"";
            expected = "\"Alexandra Alex\"";
            assertEquals(expected, sol.parseCSV(test));
        }
    }
}

