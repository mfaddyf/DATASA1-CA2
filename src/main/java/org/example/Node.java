package org.example;

import java.io.Serializable;

public class Node<E> implements Serializable {

    // ref to the next node, null if it is the final
    public Node<E> next = null;
    public E data;

    public Node(E data) {
        this.data = data; // assigns given data
        this.next = null; // at the beginning, no next node
    }
}
