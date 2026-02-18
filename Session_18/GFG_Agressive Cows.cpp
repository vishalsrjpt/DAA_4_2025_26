//24BCS10980_VISHAL KUMAR SINGH
class Solution {
  public:
    bool canPlace(vector<int>& stalls, int k, int dist) {
        int count = 1;               
        int lastPos = stalls[0];      
        
        for (int i = 1; i < stalls.size(); i++) {
            if (stalls[i] - lastPos >= dist) {
                count++;               
                lastPos = stalls[i];
            }
            if (count >= k) return true; 
        }
        return count >= k;
    }

    int aggressiveCows(vector<int>& stalls, int k) {
        sort(stalls.begin(), stalls.end());  
        int n = stalls.size();
        
        int low = 1;                          
        int high = stalls[n-1] - stalls[0];    
        int ans = 0;
        
        while (low <= high) {
            int mid = low + (high - low) / 2;   
            
            if (canPlace(stalls, k, mid)) {
                ans = mid;                      
                low = mid + 1;
            } else {
                high = mid - 1;                  
            }
        }
        return ans;
    }
};
