package water_land;

import java.util.*;

import org.junit.*;

import static org.junit.Assert.*;



public class WaterLand {
/*
    Water Land / Water Drop / Pour Water
    AirBnB Interview Question
 */

//    We are given an elevation map, heights[i] representing the height of the terrain at that index.
//    The width at each index is 1. After V units of water fall at index K, how much water is at each index?
//
//    Water first drops at index K and rests on top of the highest terrain or water at that index. Then, it flows according to the following rules:
//    If the droplet would eventually fall by moving left, then move left.
//            Otherwise, if the droplet would eventually fall by moving right, then move right.
//    Otherwise, rise at it's current position.
//    Here, "eventually fall" means that the droplet will eventually be at a lower level if it moves in that direction.
//    Also, "level" means the height of the terrain plus any water in that column.
//
//    We can assume there's infinitely high terrain on the two sides out of bounds of the array.
//    Also, there could not be partial water being spread out evenly on more than 1 grid block - each unit of water has to be in exactly one block.
//
//    Example 1:
//
//    Input: heights = [2,1,1,2,1,2,2], V = 4, K = 3
//    Output: [2,2,2,3,2,2,2]
//    Explanation:
//            #       #
//            #       #
//            ##  # ###
//            #########
//             0123456    <- index
//
//    The first drop of water lands at index K = 3:
//
//            #       #
//            #   w   #
//            ##  # ###
//            #########
//             0123456
//
//    When moving left or right, the water can only move to the same level or a lower level.
//            (By level, we mean the total height of the terrain plus any water in that column.)
//    Since moving left will eventually make it fall, it moves left.
//            (A droplet "made to fall" means go to a lower height than it was at previously.)
//
//            #       #
//            #       #
//            ## w# ###
//            #########
//             0123456
//
//    Since moving left will not make it fall, it stays in place.  The next droplet falls:
//
//            #       #
//            #   w   #
//            ## w# ###
//            #########
//             0123456
//
//    Since the new droplet moving left will eventually make it fall, it moves left.
//    Notice that the droplet still preferred to move left,
//    even though it could move right (and moving right makes it fall quicker.)
//
//            #       #
//            #  w    #
//            ## w# ###
//            #########
//             0123456
//
//            #       #
//            #       #
//            ##ww# ###
//            #########
//             0123456
//
//    After those steps, the third droplet falls.
//    Since moving left would not eventually make it fall, it tries to move right.
//    Since moving right would eventually make it fall, it moves right.
//
//            #       #
//            #   w   #
//            ##ww# ###
//            #########
//             0123456
//
//            #       #
//            #       #
//            ##ww#w###
//            #########
//             0123456
//
//    Finally, the fourth droplet falls.
//    Since moving left would not eventually make it fall, it tries to move right.
//    Since moving right would not eventually make it fall, it stays in place:
//
//            #       #
//            #   w   #
//            ##ww#w###
//            #########
//             0123456
//
//    The final answer is [2,2,2,3,2,2,2]:
//
//            #
//            #######
//            #######
//            0123456
//
//    Example 2:
//
//    Input: heights = [1,2,3,4], V = 2, K = 2
//    Output: [2,3,3,4]
//    Explanation:
//    The last droplet settles at index 1, since moving further left would not cause it to eventually fall to a lower height.
//
//            Example 3:
//
//    Input: heights = [3,1,3], V = 5, K = 1
//    Output: [4,4,4]
//
//    Note:
//
//    heights will have length in [1, 100] and contain integers in [0, 99].
//    V will be in range [0, 2000].
//    K will be in range [0, heights.length - 1].

    // - Design of algorithms:
    // Each time there is a water dropped, we can check if the height of the current-1(left) position is lowr than the current location recursivedly until we are sure that moving left furture will not cause the current water to fall eventually.
    // If it doesn't fall, then we can do the same thing to right direction.
    // If it dpesn't fall eventually, the water will be stuck as current potiion.
    public int[] pourWater(int[] heights, int V, int K) {
        int[] rst = Arrays.copyOf(heights, heights.length);
        // System.out.printf("length of heights is %d.\n", heights.length);
        while(V > 0) {
            // System.out.printf("V is %d, K is %d.\n", V, K);
            // Try to fall on the left
            int currentIdx = K;
            int lastFallPosition = K;
            boolean findFall = false;
            while(currentIdx > 0) {
                if (rst[currentIdx] == rst[currentIdx-1]) {
                    currentIdx--;
                } else if (rst[currentIdx] > rst[currentIdx-1]) {
                    lastFallPosition = --currentIdx;
                    findFall = true;
                }else {
                    break;
                }
            }

            if (findFall == true) {
                // System.out.printf("Find fall on the left, currentId is %d.\n", currentIdx);
                rst[lastFallPosition]++;
                V--;
                continue;
            }

            // If not fall on the left direction, then try to fall on the right direction
            currentIdx = K;
            while(currentIdx < rst.length-1) {
                if(rst[currentIdx] == rst[currentIdx+1]) {
                    currentIdx++;
                } else if (rst[currentIdx] > rst[currentIdx+1]) {
                    lastFallPosition = ++currentIdx;
                    findFall = true;
                } else {
                    break;
                }
            }

            if (findFall == true) {
                // System.out.printf("Find fall on the right, currentId is %d.\n", currentIdx);
                rst[lastFallPosition]++;
                V--;
                continue;
            }

            // If not fail on neither direction, then keep at the current location
            rst[K]++;
            V--;
            // System.out.printf("Don't find fall on neighther direction, K is %d.\n", K);

        }

        return rst;
    }

    public class Solution {
        public void pourWater(int[] heights, int location, int water) {
            int[] waters = new int[heights.length];
            int pourLocation;

            // Principal: mimic single drop of water flow.
            while (water > 0) {
                // flow left
                int left = location - 1;
                while (left >= 0) {
                    if (heights[left] + waters[left] > heights[left + 1] + waters[left + 1]) {
                        break;
                    }
                    left--;
                }
                if (heights[left + 1] + waters[left + 1] < heights[location] + waters[location]) {
                    pourLocation = left + 1;
                    waters[pourLocation]++;
                    water--;
                    continue; // next drop
                }

                // flow right
                int right = location + 1;
                while (right < heights.length) {
                    if (heights[right] + waters[right] > heights[right - 1] + waters[right - 1]) {
                        break;
                    }
                    right++;
                }
                if (heights[right - 1] + waters[right - 1] < heights[location] + waters[location]) {
                    pourLocation = right - 1;
                    waters[pourLocation]++;
                    water--;
                    continue; // next drop
                }

                // if this drop has no where to go, fill to the pouring location.
                pourLocation = location;
                waters[pourLocation]++;
                water--;
            }

            print(heights, waters);
        }

        private void print(int[] heights, int[] waters) {
            int n = heights.length;

            int maxHeight = 0;
            for (int i = 0; i < n; i++) {
                maxHeight = Math.max(maxHeight, heights[i] + waters[i]);
            }

            for (int height = maxHeight; height >= 0; height--) {
                for (int i = 0; i < n; i++) {
                    if (height <= heights[i]) {
                        System.out.print("+");
                    } else if (height > heights[i] && height <= heights[i] + waters[i]) {
                        System.out.print("W");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static class UnitTest {
        @Test
        public void test1() {
            Solution sol = new WaterLand().new Solution();
            int[] waterLand = new int[]{5, 4, 2, 1, 2, 3, 2, 1, 0, 1, 2, 4};
            sol.pourWater(waterLand, 5, 1);
            sol.pourWater(waterLand, 5, 5);
            sol.pourWater(waterLand, 5, 10);
            sol.pourWater(waterLand, 5, 20);
            sol.pourWater(waterLand, 5, 30);
            sol.pourWater(waterLand, 5, 50);
            sol.pourWater(waterLand, 5, 100);

            waterLand = new int[]{5, 4, 2, 1, 3, 2, 2, 1, 0, 1, 4, 3};
            sol.pourWater(waterLand, 4, 1);
            sol.pourWater(waterLand, 4, 5);
            sol.pourWater(waterLand, 4, 10);
            sol.pourWater(waterLand, 4, 20);
            sol.pourWater(waterLand, 4, 30);
            sol.pourWater(waterLand, 4, 50);
            sol.pourWater(waterLand, 4, 100);
        }
    }
}

