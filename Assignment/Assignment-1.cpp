#include <bits/stdc++.h>
using namespace std;

int main() {
    int K, N;
    cin >> K >> N;

    priority_queue<int, vector<int>, greater<int>> pq;

    for(int i = 0; i < N; i++) {
        int x;
        cin >> x;

        if(pq.size() < K) {
            pq.push(x);
        } else if(x > pq.top()) {
            pq.pop();
            pq.push(x);
        }

        if(pq.size() < K) cout << -1 << '\n';
        else cout << pq.top() << '\n';
    }

    return 0;
}
