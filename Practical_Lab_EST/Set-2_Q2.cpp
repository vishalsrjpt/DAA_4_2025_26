#include <bits/stdc++.h>
using namespace std;
template <typename T>
class Queue {
private:
    struct Node {
        T data;
        Node* next;
        Node(const T& value) : data(value), next(nullptr) {}
    };
    Node* front;   
    Node* rear;    
    int count;     
public:
    Queue() : front(nullptr), rear(nullptr), count(0) {}
    ~Queue() {
        while (!empty()) {
            pop();
        }
    }
    void push(const T& value) {
        Node* newNode = new Node(value);
        if (empty()) {
            front = rear = newNode;
        } else {
            rear->next = newNode;
            rear = newNode;
        }
        count++;
    }
    void pop() {
        if (empty()) {
            throw std::out_of_range("Queue is empty, cannot pop.");
        }
        Node* temp = front;
        front = front->next;
        if (front == nullptr) {  
            rear = nullptr;
        }
        delete temp;
        count--;
    }
    T peek() const {
        if (empty()) {
            throw std::out_of_range("Queue is empty, cannot peek.");
        }
        return front->data;
    }
    bool empty() const {
        return front == nullptr;
    }
    int size() const {
        return count;
    }
};
int main() {
    Queue<int> q;
    cout << "Empty: " << q.empty() << "\n";   
    cout << "Size : " << q.size() << "\n";   
    q.push(10);
    q.push(20);
    q.push(30);
    cout << "Peek : " << q.peek() << "\n";    
    cout << "Size : " << q.size() << "\n";    
    q.pop();
    cout << "After pop, peek: " << q.peek() << "\n";  
    cout << "Size : " << q.size() << "\n";             
    q.pop();
    q.pop();   
    cout << "Empty: " << q.empty() << "\n";   
    return 0;
}  
