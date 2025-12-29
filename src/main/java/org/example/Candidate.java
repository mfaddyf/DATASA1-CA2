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
        return politician.getName() + " (" + partyAtElection + ") - " + votes + " votes";
    }
}
