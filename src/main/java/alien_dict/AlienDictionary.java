package alien_dict;
// There is a new alien language which uses the latin alphabet. However, the order among letters are unknown to you. You receive a list of non-empty words from the dictionary, where words are sorted lexicographically by the rules of this new language. Derive the order of letters in this language.

// Example 1:

// Input:
// [
//   "wrt",
//   "wrf",
//   "er",
//   "ett",
//   "rftt"
// ]

// Output: "wertf"

// Example 2:

// Input:
// [
//   "z",
//   "x"
// ]

// Output: "zx"

// Example 3:

// Input:
// [
//   "z",
//   "x",
//   "z"
// ] 

// Output: "" 

// Explanation: The order is invalid, so return "".

// Note:

//     You may assume all letters are in lowercase.
//     You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
//     If the order is invalid, return an empty string.
//     There may be multiple valid order of letters, return any one of them is fine.


import java.util.*;

class AlienDictionary {

    // my solution
    public String alienOrder(String[] words) {
        StringBuilder rstSb = new StringBuilder();
        Set<Character> visited = new HashSet<>();
        HashMap<Character, List<Character>> preferenceGraph = new HashMap<>(); // adjacency list of the graph

        for(int i = 0; i < words.length; i++) {
            String currentVertex = words[i];
            if (i < words.length-1) {
                String nextVertex = words[i+1];
                char[] edgeCharArr = getFirstDiffCharPiar(currentVertex, nextVertex);
                // System.out.println("Add edge " + currentVertex + "->" + nextVertex + " to adjacent list");
                if (edgeCharArr != null) {
                    List<Character> nextVertexes = preferenceGraph.get(edgeCharArr[0]);
                    if (nextVertexes == null) {
                        nextVertexes = new ArrayList<>();
                        preferenceGraph.put(edgeCharArr[0], nextVertexes);
                    }
                    nextVertexes.add(edgeCharArr[1]);
                }
            }
            // add all the letters as well
            for(int j = 0; j < currentVertex.length(); j++) {
                List<Character> nextVertexes = preferenceGraph.get(currentVertex.charAt(j));
                if (nextVertexes == null) {
                    nextVertexes = new ArrayList<>();
                    preferenceGraph.put(currentVertex.charAt(j), nextVertexes);
                }
            }
        }

        try{
            for(char vertex : preferenceGraph.keySet()) {
                Set<Character> visiting = new HashSet<>();
                if (!visited.contains(vertex)) { // check if visited before visit
                    dfs(vertex, preferenceGraph, visiting, visited, rstSb);
                }
            }
        } catch (Exception e) {
            return "";
        }

        rstSb.reverse();
        return rstSb.toString();
    }

    // case 1
    // a b c 
    // a b d

    // case 2
    // a b c 
    // a b c d 
    public char[] getFirstDiffCharPiar(String str1, String str2) {
        char[] rst = null;
        int l = Math.min(str1.length(), str2.length());
        for(int i = 0; i <l; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                rst = new char[2];
                rst[0] = str1.charAt(i);
                rst[1] = str2.charAt(i);
                break;
            }
        }
        return rst;
    }

    public void dfs(char vertex, HashMap<Character, List<Character>> adjacentLists, Set<Character> visiting, Set<Character> visited, StringBuilder list) {
        // System.out.println("Start visiting vertex " + vertex);
        if (visiting.contains(vertex)) {
            throw new RuntimeException("vertex " + vertex + " is already in visiting. There is a preference cycle in the input." );
        }

        visiting.add(vertex);

        if (adjacentLists.containsKey(vertex)) {
            for( char nextVertex : adjacentLists.get(vertex)) {
                if( !visited.contains(nextVertex)) { // check if visited before visit
                    dfs(nextVertex, adjacentLists, visiting, visited, list);
                }
            }
        }

        list.append(vertex);
        visited.add(vertex);
        // System.out.println("Finish visiting " + vertex);
    }
}