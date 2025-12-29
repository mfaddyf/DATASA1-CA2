package org.example;

import java.io.Serializable;

public class Candidate implements Serializable {

    private Politician politician;
    private String partyAtElection;
    private int votes;

    public Candidate(Politician politician, String partyAtElection, int votes) {
        this.politician = politician;
        this.partyAtElection = partyAtElection;
        this.votes = votes;
    }

    // Getters and setters...
}
