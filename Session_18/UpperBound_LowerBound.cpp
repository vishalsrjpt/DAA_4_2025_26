//24BCS10980-Vishal Kumar Singh
#include<bits/stdc++.h>
using namespace std;
int upperBound(int low, int high, int key){
      
    while(low<high){
        int mid=low+(high-low)/2;

        if(arr[mid]<=key){
            low=mid+1;
        }else{
            high=mid;
        }
    }
    
    return low;
}

int lowerBound(int low, int high, int key){
    
    
    while(low<high){
        int mid=low+(high-low)/2;

        if(arr[mid]<key){
            low=mid+1;
        }else{
            high=mid;
        }
    }
    
    return low;
}


int main(){
  vector<int> ary;
  int key;
  cin>>key
  sort(ary);
  upperBound(0,ary.size(),key);
  lowerBound(0,ary.size(),key)
  return 0;
}
