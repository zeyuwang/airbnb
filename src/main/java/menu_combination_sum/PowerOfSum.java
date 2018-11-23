package menu_combination_sum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

// Similar to the Menu Combination Sum problem
// Find the number of ways X to break down to a number N to a sum a integer rise to power of M.
// For example, when N = 5, M = 2, then there are 2 ways to break down number N:
// 5 = 1^2 + 2^2
// 5 = 1^2 + 1^2 + 1^2 + 1^2 + 1^2
// N > 0
// M > 0
// result < Integer.MAX_VALUE
public class PowerOfSum {

    public class Solution {
        public int getWays(int N, int M) {
            long target = N;
            List<Long> candidates = new ArrayList<>();
            for (int i = 1; i <= target; i++) {
                long candidate = (long) Math.pow(i, M);
                if(candidate <= target) {
//                    System.out.printf("%d is one candidate.\n", candidate);
                    candidates.add(candidate);
                }
            }
            return backTrace(target, candidates, 0);
        }

        private int backTrace(Long target, List<Long> candidates, int start) {
            int rst = 0;
            if (target == 0) {
                return ++rst;
            }
            for(int i = start; i < candidates.size(); i++) {
                long nextCandidates = candidates.get(i);
                if(target < nextCandidates) {
                    continue;
                }
//                System.out.printf("target is %d,start is %d, i is %d, nextCandidates id %d， target - nextCandidates is %d.\n", target, start, i, nextCandidates, target - nextCandidates);
                rst += backTrace(target - nextCandidates, candidates, i);
            }
            return rst;
        }
    }

    public static class UnitTest {
        @Test
        public void test1() {
            int N = 5;
            int M = 2;
            Solution solution = new PowerOfSum().new Solution();
            assertEquals(solution.getWays(N, M), 2);
        }
    }

}
