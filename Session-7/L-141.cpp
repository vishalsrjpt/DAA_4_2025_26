/**
 * Definition for singly-linked list.
 * struct ListNode {
 *     int val;
 *     ListNode *next;
 *     ListNode(int x) : val(x), next(NULL) {}
 * };
 */
class Solution {
public:
    bool hasCycle(ListNode *head) {
        ListNode* slw = head;
        ListNode* fst = head;
        while(fst != nullptr && fst->next != nullptr){
            if(fst->next == slw) return true;
            slw = slw->next;
            fst = fst->next->next;
        }
        return false;
    }
};
