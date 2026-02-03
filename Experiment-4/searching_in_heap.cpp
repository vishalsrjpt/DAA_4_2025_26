//Vishal Kumar Singh : 24BCS10980
#include<bits/stdc++.h>
using namespace std;
#define MAX 100

int heap[MAX];
int heapSize = 0;

bool searchHeap(int val) {
    // Linear search through the heap array
    for(int i = 0; i < heapSize; i++) {
        if(heap[i] == val) {
            return true;
        }
    }
    return false;
}



void displayHeap() {
    if(heapSize == 0) {
        cout << "Heap is empty!" << endl;
        return;
    }
    
    cout << "Current heap: ";
    for(int i = 0; i < heapSize; i++) {
        cout << heap[i] << " ";
    }
    cout << endl;
}

int main() {

    displayHeap();
    
    int searchValue = 3;
    
    cout << "\nSearching for value: " << searchValue << endl;
    
    if(searchHeap(searchValue)) {
        cout << "Value " << searchValue << " found using linear search!" << endl;
    } else {
        cout << "Value " << searchValue << " not found!" << endl;
    }
    
    return 0;
}
