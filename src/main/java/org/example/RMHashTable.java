package org.example;

import java.io.Serializable;

public class RMHashTable implements Serializable {

    private static final long serialVersionUID = 1L;

    private static class Node implements Serializable {

        private static final long serialVersionUID = 1L;

        String key; // unique ID e.g. PoliticianID
        Object value; // object stored like politician, election etc
        Node next; // linked to next node in chain

        Node (String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    //Array of buckets (each bucket is the head of a linked list)
    private Node[] table;

    //Number of key-value pairs stored
    private int size;

    public RMHashTable(int capacity) {
        table = new Node[capacity];
        size = 0;
    }

    private int RMHash(String key) {
        int h = 0;
        for (int i = 0; i < key.length(); i++) {
            h = (31 * h + key.charAt(i)) % table.length;
        }
        return Math.abs(h);
    }

    public RMHashTable() {
        this(111);
    }

    public void put(String key, Object value) {

        // convert the key into bucket index
        int index = RMHash(key);

        // start at first node in the bucket
        Node current = table[index];

        // traverse chain to check if key already exists
        while (current != null) {
            if (current.key.equals(key)) {
                //key is found then updates value
                current.value = value;
                return;
            }
            current = current.next;
        }

        // if key is not found then insert new node at the head of the chain
        Node newNode = new Node(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    public Object get(String key) {

        // Compute the bucket index from key
        int index = RMHash(key);

        //Traverse the linked list at this bucket
        Node current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value; //key found
            }
            current = current.next;
        }

        return null; //key not found
    }

    public boolean remove(String key) {
        int index = RMHash(key);
        Node current = table[index];
        Node prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }


    public int size() {
        return size;
    }
}


