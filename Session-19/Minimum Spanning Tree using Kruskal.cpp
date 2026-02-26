//--24BCS10980_VISHAL KUMAR SINGH
//Implementation of Kruskal algorithm.
class Solution {
  public:
    int kruskalsMST(int V, vector<vector<int>> &edges) {
        sort(edges.begin(), edges.end(), 
             [](vector<int>& a, vector<int>& b) {
                 return a[2] < b[2];
             });
        
        vector<int> parent(V);
        vector<int> rank(V, 0);
        for (int i = 0; i < V; i++) {
            parent[i] = i;
        }
        
        int mstWeight = 0;
        int edgesInMST = 0;
        
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];
            
            if (find(parent, u) != find(parent, v)) {
                unionSet(parent, rank, u, v);
                mstWeight += weight;
                edgesInMST++;
                
                if (edgesInMST == V - 1) {
                    break;
                }
            }
        }
        
        return mstWeight;
    }
    
private:
    int find(vector<int>& parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }
    
    void unionSet(vector<int>& parent, vector<int>& rank, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        
        if (rootX != rootY) {
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
};
