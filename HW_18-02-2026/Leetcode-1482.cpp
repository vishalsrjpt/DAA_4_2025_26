//24BCS10980_VISHAL KUMAR SINGH

class Solution {
public:
    int minDays(vector<int>& bloomDay, int m, int k) {
        long long totalFlowersNeeded = (long long)m * k;
        if (bloomDay.size() < totalFlowersNeeded) return -1; 

        int low = 1, high = *max_element(bloomDay.begin(), bloomDay.end());
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (canMakeBouquets(bloomDay, m, k, mid)) {
                ans = mid;
                high = mid - 1; 
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }

private:
    bool canMakeBouquets(vector<int>& bloomDay, int m, int k, int day) {
        int bouquets = 0;
        int consecutive = 0;

        for (int i = 0; i < bloomDay.size(); i++) {
            if (bloomDay[i] <= day) {
                consecutive++;
                if (consecutive == k) {
                    bouquets++;
                    consecutive = 0; 
                }
            } else {
                consecutive = 0; 
            }
            if (bouquets >= m) return true;
        }
        return bouquets >= m;
    }
};
