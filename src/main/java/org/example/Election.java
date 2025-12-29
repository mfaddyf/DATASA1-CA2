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

    // ---
    // GETTERS
    // ---

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getSeats() {
        return seats;
    }

    // ---
    // SETTERS
    // ---

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setCandidates(MLinkedList<Candidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public String toString() {
        return "Election Type: " + type + ", Location: " + location + ", Date: " + date;
    }
}
