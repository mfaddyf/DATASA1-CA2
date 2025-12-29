package org.example;

import java.io.Serializable;

public class Election implements Serializable {

    private String type;
    private String location;
    private String date;
    private int seats;
    private MLinkedList<Candidate> candidates;

    public Election(String type, String location, String date, int seats) {
        this.type = type;
        this.location = location;
        this.date = date;
        this.seats = seats;
        this.candidates = new MLinkedList<>();
    }

    public void addCandidate(Candidate c) {
        candidates.addElement(c);
    }

    public MLinkedList<Candidate> getCandidates() {
        return candidates;
    }

    // Getters and setters...
}
