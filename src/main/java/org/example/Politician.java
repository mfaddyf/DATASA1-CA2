package org.example;

import java.io.Serializable;

public class Politician implements Serializable {

    private String name;
    private String dob;
    private String party;
    private String county;
    private String imageUrl;
    private MLinkedList<Election> electionsStoodIn;

    public Politician(String name, String dob, String party, String county, String imageUrl) {
        this.name = name;
        this.dob = dob;
        this.party = party;
        this.county = county;
        this.imageUrl = imageUrl;
        this.electionsStoodIn = new MLinkedList<>();
    }

    public void addElection(Election e) {
        electionsStoodIn.addElement(e);
    }

    public MLinkedList<Election> getElections() {
        return electionsStoodIn;
    }

    // Getters and setters...
}
