//--24BCS10980_VISHAL KUMAR SINGH
//Prim's algorithm using a priority queue.
class Solution {
public:
    int spanningTree(int V, vector<vector<int>>& edges) {
        vector<vector<pair<int, int>>> adj(V);
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            adj[u].push_back({v, w});
            adj[v].push_back({u, w});
        }
        
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        vector<bool> inMST(V, false);
        vector<int> key(V, INT_MAX);
        
        key[0] = 0;
        pq.push({0, 0});
        
        int mstWeight = 0;
        
        while (!pq.empty()) {
            int u = pq.top().second;
            int w = pq.top().first;
            pq.pop();
            
            if (inMST[u]) continue;
            
            inMST[u] = true;
            mstWeight += w;
            
            for (auto& neighbor : adj[u]) {
                int v = neighbor.first;
                int weight = neighbor.second;
                
                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    pq.push({key[v], v});
                }
            }
        }
        
        return mstWeight;
    }
};
