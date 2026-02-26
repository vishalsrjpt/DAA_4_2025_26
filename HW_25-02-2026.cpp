//--24BCS10980--VISHAL KUMAR SINGH

//1. Multiply 2 Matrices GFG.
class Solution {
  public:
    vector<vector<int>> multiply(vector<vector<int>>& mat1, vector<vector<int>>& mat2) {
        int n=mat1.size();

        vector<vector<int>> res(n, vector<int>(n, 0));
        
        
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                for(int k=0;k<n;k++){
                    res[i][j]+=mat1[i][k]*mat2[k][j];
                }
            }
        }
        
        return res;
    }
};

//3. Multiply 2 matrices using Recurrence Relation with recurrence relation T(n) = 8T(n/2) + O(n²))
class Solution {
public:
    vector<vector<int>> multiply(vector<vector<int>>& mat1, vector<vector<int>>& mat2) {
        int n = mat1.size();
        return multiplyRecursive(mat1, mat2, 0, 0, 0, 0, n);
    }
    
private:
    vector<vector<int>> multiplyRecursive(vector<vector<int>>& A, vector<vector<int>>& B, 
                                          int rowA, int colA, int rowB, int colB, int n) {
        vector<vector<int>> C(n, vector<int>(n, 0));
        
        if (n == 1) {
            C[0][0] = A[rowA][colA] * B[rowB][colB];
            return C;
        }
        
        int half = n / 2;
        
        vector<vector<int>> P1 = multiplyRecursive(A, B, rowA, colA, rowB, colB, half);
        vector<vector<int>> P2 = multiplyRecursive(A, B, rowA, colA + half, rowB + half, colB, half);
        
        vector<vector<int>> P3 = multiplyRecursive(A, B, rowA, colA, rowB, colB + half, half);
        vector<vector<int>> P4 = multiplyRecursive(A, B, rowA, colA + half, rowB + half, colB + half, half);
        
        vector<vector<int>> P5 = multiplyRecursive(A, B, rowA + half, colA, rowB, colB, half);
        vector<vector<int>> P6 = multiplyRecursive(A, B, rowA + half, colA + half, rowB + half, colB, half);
        
        vector<vector<int>> P7 = multiplyRecursive(A, B, rowA + half, colA, rowB, colB + half, half);
        vector<vector<int>> P8 = multiplyRecursive(A, B, rowA + half, colA + half, rowB + half, colB + half, half);
        
        int newN = n;
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                C[i][j] = P1[i][j] + P2[i][j];
            }
        }
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                C[i][j + half] = P3[i][j] + P4[i][j];
            }
        }
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                C[i + half][j] = P5[i][j] + P6[i][j];
            }
        }
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                C[i + half][j + half] = P7[i][j] + P8[i][j];
            }
        }
        
        return C;
    }
};


//3. Multiply 2 matrices using Recurrence Relation with recurrence relation (Strassen's Algorithm) T(n) = 7T(n/2) + O(n²))
class Solution {
public:
    vector<vector<int>> multiply(vector<vector<int>>& mat1, vector<vector<int>>& mat2) {
        int n = mat1.size();
        return strassenMultiply(mat1, mat2, 0, 0, 0, 0, n);
    }
    
private:
    vector<vector<int>> strassenMultiply(vector<vector<int>>& A, vector<vector<int>>& B,
                                         int rowA, int colA, int rowB, int colB, int n) {
        vector<vector<int>> C(n, vector<int>(n, 0));
        
        if (n == 1) {
            C[0][0] = A[rowA][colA] * B[rowB][colB];
            return C;
        }
        
        int half = n / 2;
        
        auto P1 = strassenMultiply(A, B, rowA, colA, rowB, colB, half); 
        auto P2 = strassenMultiply(A, B, rowA, colA + half, rowB + half, colB, half); 
        auto P3 = strassenMultiply(A, B, rowA, colA, rowB, colB + half, half); 
        auto P4 = strassenMultiply(A, B, rowA, colA + half, rowB + half, colB + half, half); 
        auto P5 = strassenMultiply(A, B, rowA + half, colA, rowB, colB, half); 
        auto P6 = strassenMultiply(A, B, rowA + half, colA + half, rowB + half, colB, half); 
        auto P7 = strassenMultiply(A, B, rowA + half, colA, rowB, colB + half, half); 
        auto P8 = strassenMultiply(A, B, rowA + half, colA + half, rowB + half, colB + half, half); 
        
        
        
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < half; j++) {
                C[i][j] = P1[i][j] + P4[i][j] - P5[i][j] + P7[i][j];
                C[i][j + half] = P3[i][j] + P5[i][j];
                C[i + half][j] = P2[i][j] + P4[i][j];
                C[i + half][j + half] = P1[i][j] + P3[i][j] - P2[i][j] + P6[i][j];
            }
        }
        
        return C;
    }
};
