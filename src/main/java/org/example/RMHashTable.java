package org.example;

import java.io.Serializable;

public class RMHashTable implements Serializable {

    private static class Node implements Serializable {

        String key; // unique ID e.g. PoliticianID
        Object value; // object stored like politician, election etc
        Node next; // linked to next node in chain

        Node (String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // array of buckets (each bucket is the head of a linked list)
    private Node[] table;

    // number of key-value pairs stored
    private int size;

    public RMHashTable(int capacity) {
        table = new Node[capacity];
        size = 0;
    }

    private int RMHash(String key) {
        int h = 0; // start hash
        for (int i = 0; i < key.length(); i++) {
            h = (31 * h + key.charAt(i)) % table.length; //polynomial hash
        }
        return Math.abs(h); //ensure hash is positive
    }

    public RMHashTable() {
        this(111); // default size
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
            current = current.next; // move along the chain
        }

        // if key is not found then insert new node at the head of the chain
        Node newNode = new Node(key, value);
        newNode.next = table[index]; // link old head
        table[index] = newNode; // new head
        size++; // increase the count
    }

    public Object get(String key) {

        // Compute the bucket index from key
        int index = RMHash(key);

        //Traverse the linked list at this bucket
        Node current = table[index];
        while (current != null) {
            if (current.key.equals(key)) { //key found
                return current.value;
            }
            current = current.next; // move to the next
        }

        return null; //key not found
    }

    // removing key-value pair
    public boolean remove(String key) {
        int index = RMHash(key); // find bucket
        Node current = table[index]; // start chain
        Node prev = null; // previous node

        while (current != null) {
            if (current.key.equals(key)) { //found key
                if (prev == null) {
                    table[index] = current.next; // remove head
                } else {
                    prev.next = current.next; // unlink node
                }
                size--; // decrease count
                return true; // removed
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // checking if a key exists
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    // return number of entries
    public int size() {
        return size;
    }
}


