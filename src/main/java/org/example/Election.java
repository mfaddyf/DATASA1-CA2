package org.example;

import java.io.Serializable;

public class Election implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public void addCandidate(Candidate c) {
        candidates.addElement(c);
    }

    public MLinkedList<Candidate> getCandidates() {
        return candidates;
    }

    public void removeCandidate(Candidate c) {
        candidates.remove(c);   // remove by reference
    }

    @Override
    public String toString() {
        return type + " | " + location + " | " + date + " | Seats: " + seats;
    }
}
