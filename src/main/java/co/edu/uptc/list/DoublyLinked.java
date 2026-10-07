package co.edu.uptc.list;

import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class DoublyLinked<T> {
    private ListNode<T> head;
    private ListNode<T> tail;
    private int size;

    public void addLast(T value) {
        ListNode<T> node = new ListNode<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.setNext(node);
            node.setPrevious(tail);
            tail = node;
        }
        size++;
    }

    public T removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        T value = head.getData();
        head = head.getNext();
        if (head == null) {
            tail = null;
        } else {
            head.setPrevious(null);
        }
        size--;
        return value;
    }

    public void showLinkedList(){
        ListNode<T> current = head;
        while(current != null){
            System.out.println(current.getData());
            current = current.getNext();
        }
    }

    public ListNode<T> getNode(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }

        ListNode<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current;
    }

    public T removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("La lista está vacía.");
        }

        T value = tail.getData();

        tail = tail.getPrevious();

        if (tail == null) {
            head = null;
        } else {
            tail.setNext(null);
        }

        size--;

        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
