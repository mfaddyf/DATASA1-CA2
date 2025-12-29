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

    // ---
    // GETTERS
    // ---

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getParty() {
        return party;
    }

    public String getCounty() {
        return county;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MLinkedList<Election> getElectionsStoodIn() {
        return electionsStoodIn;
    }

    // ---
    // SETTERS
    // ---

    public void setName(String name) {
        this.name = name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setElectionsStoodIn(MLinkedList<Election> electionsStoodIn) {
        this.electionsStoodIn = electionsStoodIn;
    }

    public String toString() {
        return name + " (" + party + ", " + county + ")";
    }

}
