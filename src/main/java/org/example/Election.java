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

    public Candidate findCandidateByDisplay(String display) {
        Node<Candidate> n = candidates.getHead();
        while (n != null) {
            if (n.data.toString().equals(display)) {
                return n.data;
            }
            n = n.next;
        }
        return null;
    }

    public void updateCandidate(Candidate updated) {
        Node<Candidate> n = candidates.getHead();
        while (n != null) {
            Candidate c = n.data;
            if (c.getPolitician().equals(updated.getPolitician())) {
                n.data = updated; return;
            }
            n = n.next;
        }
    }

    public void removeCandidateByPolitician(Politician p) {
        Node<Candidate> current = candidates.getHead();
        Node<Candidate> prev = null;

        while (current != null) {
            if (current.data.getPolitician().equals(p)) {

                if (prev == null) {
                    candidates.head = current.next;
                }
                else {
                    prev.next = current.next;
                }
                current = current.next;
            }
            else {
                prev = current;
                current = current.next;
            }
        }
    }


    public boolean matches(Election other) {
        return this.type.equalsIgnoreCase(other.type) &&
                this.location.equalsIgnoreCase(other.location) &&
                this.date.equalsIgnoreCase(other.date);
    }

    @Override
    public String toString() {
        return "Election Type: " + type + ", Location: " + location + ", Date: " + date;
    }
}
