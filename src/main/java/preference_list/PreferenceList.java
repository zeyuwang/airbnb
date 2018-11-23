package preference_list;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;

public class PreferenceList {
    /*
        Preference List
        AirBnB Interview Question
     */


    //    27 Preference List
    //    Given a list of everyone's preferred city list, output the city list following the order of everyone's preference order.
    //
    //    For example, input is [[3, 5, 7, 9], [2, 3, 8], [5, 8]]. One of possible output is [2, 3, 5, 8, 7, 9].

    // - Design of Algorithms:
    // We can use topology sort for this question.
    // We can regard the preference list as a dependency list. The city in the beginning of the list has dependency on the city on the end of the lost.
    // We can use topology sort to get an dependency linked list, and the list will be the result of this issue.
    //
    // - Implementation details:
    // In the topology sort, we need to use DFS to search through the list of cities.
    // The cities finished being visited first, will be put in to a list, since there will be no cities has an lower preference score than it.
    // After we add all the result to the list, we need to reverse the list, to make the cities in the list sorted in descending order of preference score.
    //
    // - Concepts:
    // Vertex - The city in the list is the vertex
    // Directional edge - the two consecutive cities in the preference list. C1->C2 is an edge.
    //
    // - Data Structure:
    // A HashMap as a adjacency link list for the preference graph.
    // Set<Integer> to track the cities that has been visited
    // Set<Integer> to track the cities in visiting, if we revisit a city, then it means there is an cycle in the graph and we can't have a correct list.
    // List<Integer> the result to return.

    public class Solution {
        public List<Integer> getPreference(List<List<Integer>> preferences) {
            // This uses Kahn’s algorithm for Topological Sorting https://www.geeksforgeeks.org/topological-sorting-indegree-based-solution/
            Map<Integer, Integer> inDegree = new HashMap<>();
            Map<Integer, Set<Integer>> nodes = new HashMap<>();
            for (List<Integer> l : preferences) {
                for (int i = 0; i < l.size() - 1; i++) {
                    int from = l.get(i);
                    int to = l.get(i + 1);
                    if (!nodes.containsKey(from)) {
                        inDegree.put(from, 0);
                        nodes.put(from, new HashSet<>());
                    }
                    if (!nodes.containsKey(to)) {
                        inDegree.put(to, 0);
                        nodes.put(to, new HashSet<>());
                    }
                    if (!nodes.get(from).contains(to)) {
                        Set<Integer> s = nodes.get(from);
                        s.add(to);
                        nodes.put(from, s);
                    }
                    inDegree.put(to, inDegree.getOrDefault(to, 0) + 1);
                }
            }
            Queue<Integer> q = new LinkedList<>();
            for (int k : inDegree.keySet()) {
                if (inDegree.get(k) == 0) {
                    q.offer(k);
                }
            }
            List<Integer> res = new ArrayList<>();
            while (!q.isEmpty()) {
                int id = q.poll();
                res.add(id);
                Set<Integer> neighbors = nodes.get(id);
                for (int next : neighbors) {
                    int degree = inDegree.get(next) - 1;
                    inDegree.put(next, degree);
                    if (degree == 0) q.offer(next);
                }
            }
            return res;
        }

        // my solution
        public List<Integer> getPreferenceWZY(List<List<Integer>> preferences) {
            List<Integer> rst = new ArrayList<>();
            Set<Integer> visited = new HashSet<>();
            HashMap<Integer, List<Integer>> preferenceGraph = new HashMap<>(); // adjacency list of the graph

            for(List<Integer> edges : preferences) {
                for(int i = 0; i < edges.size(); i++) {
                    int currentVertex = edges.get(i);
                    if (i < edges.size()-1) {
                        int nextVertex = edges.get(i+1);
                        System.out.println("Add edge " + currentVertex + "->" + nextVertex + " to adjacent list");
                        List<Integer> nextVertexes = preferenceGraph.get(currentVertex);
                        if (nextVertexes == null) {
                            nextVertexes = new ArrayList<>();
                            preferenceGraph.put(currentVertex, nextVertexes);
                        }
                        nextVertexes.add(nextVertex);
                    }
                }
            }

            for(Integer vertex : preferenceGraph.keySet()) {
                Set<Integer> visiting = new HashSet<>();
                if (!visited.contains(vertex)) { // check if visited before visit
                    dfs(vertex, preferenceGraph, visiting, visited, rst);
                }
            }

            Collections.reverse(rst);

            return rst;
        }

        public void dfs(int vertex, HashMap<Integer, List<Integer>> adjacentLists, Set<Integer> visiting, Set<Integer> visited, List<Integer> list) {
            System.out.println("Start visiting vertex " + vertex);
            if (visiting.contains(vertex)) {
                throw new RuntimeException("vertex " + vertex + " is already in visiting. There is a preference cycle in the input." );
            }

            visiting.add(vertex);

            if (adjacentLists.containsKey(vertex)) {
                for( int nextVertex : adjacentLists.get(vertex)) {
                    if( !visited.contains(nextVertex)) { // check if visited before visit
                        dfs(nextVertex, adjacentLists, visiting, visited, list);
                    }
                }
            }

            list.add(vertex);
            visited.add(vertex);
            System.out.println("Finish visiting " + vertex);
        }
    }

    public static class UnitTest {
        @Test
        public void test1() {
            Solution sol = new PreferenceList().new Solution();
            List<List<Integer>> preferences = new ArrayList<>();
            // 2 -> 3 -> 5
            List<Integer> p1 = new ArrayList<Integer>() {{
                add(2);
                add(3);
                add(5);
            }};
            // 4 -> 2 -> 1
            List<Integer> p2 = new ArrayList<Integer>() {{
                add(4);
                add(2);
                add(1);
            }};
            // 4 -> 1 -> 5 -> 6
            List<Integer> p3 = new ArrayList<Integer>() {{
                add(4);
                add(1);
                add(5);
                add(6);
            }};
            // 4 -> 7
            List<Integer> p4 = new ArrayList<Integer>() {{
                add(4);
                add(7);
            }};

            preferences.add(p1);
            preferences.add(p2);
            preferences.add(p3);
            preferences.add(p4);
            List<Integer> res = sol.getPreferenceWZY(preferences);
            // System.out.println(res);

//            assertEquals(7, res.size());

//            4-> 2 -> 7 -> 1 -> 3 -> 5 -> 6
//            assertEquals(4, (int) res.get(0));
//            assertEquals(2, (int) res.get(1));
//            assertEquals(7, (int) res.get(2));
//            assertEquals(1, (int) res.get(3));
//            assertEquals(3, (int) res.get(4));
//            assertEquals(5, (int) res.get(5));
//            assertEquals(6, (int) res.get(6));

            p1 = new ArrayList<Integer>() {{
                add(3);
                add(5);
                add(7);
                add(9);
            }};
            p2 = new ArrayList<Integer>() {{
                add(2);
                add(3);
                add(8);
            }};
            p3 = new ArrayList<Integer>() {{
                add(5);
                add(8);
            }};
            preferences = new ArrayList<>();
            preferences.add(p1);
            preferences.add(p2);
            preferences.add(p3);
            res = sol.getPreferenceWZY(preferences);
            // System.out.println(res);
            assertEquals(6, res.size());
        }
    }
}

