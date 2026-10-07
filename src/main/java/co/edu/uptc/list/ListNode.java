package co.edu.uptc.list;

import lombok.*;

@Getter
@Setter
public class ListNode<T> {
    private T data;
    private ListNode<T> previous;
    private ListNode<T> next;

    public ListNode(T data){
        next = null;
        previous = null;
        this.data = data;
    }
}
