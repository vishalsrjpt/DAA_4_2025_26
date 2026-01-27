#include <bits/stdc++.h>
//24BCS10980 Vishal Kumar singh
using namespace std;
int cnt=0;

void complexRec(int n) {


   if (n <= 2) {
       cnt++;
       return;
   }


   int p = n;
   while (p > 0) {
       vector<int> temp(n);
       for (int i = 0; i < n; i++) {
           temp[i] = i ^ p;
           cnt++;
       }
       p >>= 1;
   }


   vector<int> small(n);
   for (int i = 0; i < n; i++) {
       small[i] = i * i;
       cnt++;
   }
   


   if (n % 3 == 0) {
       reverse(small.begin(), small.end());
       cnt++;
   } else {
       reverse(small.begin(), small.end());
       cnt++;
   }


   complexRec(n / 2);
   complexRec(n / 2);
   complexRec(n / 2);
   
}

int main(){
    //recurrence relation: T(n)=3T(n/2) + n + logn
    //Time Complexity: O(N Log 3 base2 )
    //depth: logn
    complexRec(8);
    cout<<"number of operations: "<<cnt;
    return 0;
}
