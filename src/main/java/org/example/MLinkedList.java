package org.example;

import java.io.Serializable;

public class MLinkedList<L> implements Serializable { // class MLinkedList that can store elements of type L
    public Node<L> head = null; // declares head of the list pointing to first node

    public void addElement(L d) { // add element to head of list
        Node<L> newNode = new Node<>(d);

        // if the list is empty, set the head to the new node
        if (head == null) {
            head = newNode;
        }
        // otherwise, go to the last node and add new node to the end
        else {
            Node<L> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    // returns the head of the list
    public Node<L> getHead() {
        return head;
    }

    // clears the list by removing elements
    public void clear() { // empty list
        head = null;
    }

    // returns number of elements in the list
    public int size() {
        int count = 0;
        Node<L> current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    // retruves element at the given index
    public L get(int index) {
        int count = 0;
        Node<L> current = head;
        while (current != null) {
            if (count == index) {
                return current.data;
            }
            count++;
            current = current.next;
        }
        return null; // index not valid
    }

    // removes first occurrence of the given data
    public boolean remove(L data) {
        if (head == null) return false;

        // if the head contains the data, delete it
        if (head.data.equals(data)) {
            head = head.next;
            return true;
        }

        // if not, loop through and look for data
        Node<L> current = head;
        while (current.next != null) {
            if (current.next.data.equals(data)) {
                // skips the node containing data and cuts it out
                current.next = current.next.next;
                return true;
            }
            current = current.next;
        }
        return false; // item not found
    }
}
