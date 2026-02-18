//24BCS10980_VISHAL KUMAR SINGH
class Solution {
  public:
    int minTime(vector<int>& arr, int k) {
        long long low = *max_element(arr.begin(), arr.end());   
        long long high = accumulate(arr.begin(), arr.end(), 0LL);
        long long result = high;
        
        while (low <= high) {
            long long mid = low + (high - low) / 2;
            
            if (isFeasible(arr, k, mid)) {
                result = mid;       
                high = mid - 1;
            } else {
                low = mid + 1;     
            }
        }
        return (int)result;
    }
    
  private:
    bool isFeasible(vector<int>& arr, int k, long long maxTime) {
        int painters = 1;
        long long currentSum = 0;
        
        for (int length : arr) {
            if (length > maxTime) return false;
            
            if (currentSum + length <= maxTime) {
                currentSum += length;
            } else {
                painters++;
                currentSum = length;
                
                if (painters > k) return false;
            }
        }
        return true;
    }
};
