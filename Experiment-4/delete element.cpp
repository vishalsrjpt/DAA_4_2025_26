//Vishal Kumar Singh: 24BCS10980
#include<bits/stdc++.h>
using namespace std;
#define MAX 100

int heap[MAX];
int heapSize = 0;

int searchHeap(int val) {
    for(int i = 0; i < heapSize; i++) {
        if(heap[i] == val) {
            return i; 
        }
    }
    return -1;  
}

bool deleteElement(int val) {
    if(heapSize == 0) {
        cout << "Heap is empty!" << endl;
        return false;
    }
    
    int index = searchHeap(val);
    
    if(index == -1) {
        cout << "Element " << val << " not found in heap!" << endl;
        return false;
    }
    
    cout << "Deleting element " << val << " at index " << index << endl;
    
    heap[index] = heap[heapSize - 1];
    heapSize--;
    
    if(index > 0 && heap[index] < heap[(index - 1) / 2]) {
        while(index > 0 && heap[index] < heap[(index - 1) / 2]) {
            swap(heap[index], heap[(index - 1) / 2]);
            index = (index - 1) / 2;
        }
    } else {
        heapifyDown(index);
    }
    
    return true;
}

void heapifyDown(int i) {
    int smallest = i;
    int left = 2 * i + 1;
    int right = 2 * i + 2;

    if(left < heapSize && heap[smallest] > heap[left]) smallest = left;
    if(right < heapSize && heap[smallest] > heap[right]) smallest = right;

    if(smallest != i) {
        swap(heap[i], heap[smallest]);
        heapifyDown(smallest);
    }
}

void heapifyUp(int i) {
    while(i > 0 && heap[(i-1)/2] > heap[i]) {
        swap(heap[(i-1)/2], heap[i]);
        i = (i-1)/2;
    }
}

void insert(int val) {
    if(heapSize == MAX) {
        cout << "Heap overflow!" << endl;
        return;
    }
    
    heap[heapSize] = val;
    heapSize++;
    heapifyUp(heapSize - 1);
}

void deleteRoot() {
    if(heapSize == 0) {
        cout << "Heap is empty!" << endl;
        return;
    }
    
    cout << "Deleting root element: " << heap[0] << endl;
    heap[0] = heap[heapSize - 1];
    heapSize--;
    heapifyDown(0);
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

bool deleteAtIndex(int index) {
    if(heapSize == 0) {
        cout << "Heap is empty!" << endl;
        return false;
    }
    
    if(index < 0 || index >= heapSize) {
        cout << "Invalid index!" << endl;
        return false;
    }
    
    cout << "Deleting element " << heap[index] << " at index " << index << endl;
    
    heap[index] = heap[heapSize - 1];
    heapSize--;
    
    if(index > 0 && heap[index] < heap[(index - 1) / 2]) {
        while(index > 0 && heap[index] < heap[(index - 1) / 2]) {
            swap(heap[index], heap[(index - 1) / 2]);
            index = (index - 1) / 2;
        }
    } else {
        heapifyDown(index);
    }
    
    return true;
}

int main() {
    cout << "Inserting elements: 5, 3, 8, 1, 6, 2, 7" << endl;
    insert(5);
    insert(3);
    insert(8);
    insert(1);
    insert(6);
    insert(2);
    insert(7);
    
    displayHeap();
    
    
    deleteElement(3);
    displayHeap();
    
    deleteElement(1);
    displayHeap();
    
    deleteElement(10);
    displayHeap();
    
    deleteElement(6);
    displayHeap();
    
    insert(4);
    insert(9);
    displayHeap();
    
    deleteAtIndex(0);  
    displayHeap();
    
    deleteRoot();
    displayHeap();
    
    return 0;
}
