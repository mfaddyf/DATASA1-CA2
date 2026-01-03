package org.example;

import java.io.Serializable;

public class Candidate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Politician politician;
    private String partyAtElection;
    private int votes;
    private Election election;

    public Candidate(Politician politician, String partyAtElection, int votes,  Election election) {
        this.politician = politician;
        this.partyAtElection = partyAtElection;
        this.votes = votes;
        this.election = election;
    }

    // ---
    // GETTERS
    // ---

    public Politician getPolitician() {
        return politician;
    }

    public String getPartyAtElection() {
        return partyAtElection;
    }

    public int getVotes() {
        return votes;
    }

    public Election getElection() {
        return election;
    }

    // ---
    // SETTERS
    // ---

    public void setPolitician(Politician politician) {
        this.politician = politician;
    }

    public void setPartyAtElection(String partyAtElection) {
        this.partyAtElection = partyAtElection;
    }


    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        if (politician == null) return "Invalid Candidate";
        return politician.getName() + " | " + politician.getDob() +
                " | " + partyAtElection + " | Votes: " + votes;
    }
}
