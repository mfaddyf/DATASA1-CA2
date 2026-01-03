package org.example;

import java.io.*;

public class ElectionSystemManager implements Serializable {

    private RMHashTable politicianTable;
    private RMHashTable electionTable;

    private MLinkedList<Politician> politicians;
    private MLinkedList<Election> elections;

    public ElectionSystemManager() {
        politicianTable = new RMHashTable();
        electionTable = new RMHashTable();
        politicians = new MLinkedList<>();
        elections = new MLinkedList<>();
    }

    // ============================================================
    // KEY HELPERS
    // ============================================================

    private String buildPoliticianKey(Politician p) {
        return p.getName().toLowerCase() + "|" + p.getDob();
    }

    private String buildElectionKey(Election e) {
        return e.getType().toLowerCase() + "|" +
                e.getLocation().toLowerCase() + "|" +
                e.getDate();
    }

    // ============================================================
    // ADD METHODS
    // ============================================================

    public void addPolitician(Politician p) {
        String key = buildPoliticianKey(p);

        if (!politicianTable.containsKey(key)) {
            politicians.addElement(p);
        }
        politicianTable.put(key, p);
    }

    public void addElection(Election e) {
        String key = buildElectionKey(e);

        if (!electionTable.containsKey(key)) {
            elections.addElement(e);
        }
        electionTable.put(key, e);
    }

    public void addCandidateToElection(Election election, Candidate candidate) {
        if (election != null && candidate != null) {
            election.addCandidate(candidate);
        }
    }

    // ============================================================
    // UPDATE METHODS
    // ============================================================

    public void updatePolitician(Politician original, Politician updated) {
        String oldKey = buildPoliticianKey(original);

        // remove old key
        politicianTable.remove(oldKey);

        // update fields
        original.setName(updated.getName());
        original.setDob(updated.getDob());
        original.setParty(updated.getParty());
        original.setCounty(updated.getCounty());
        original.setImageUrl(updated.getImageUrl());

        // insert new key
        String newKey = buildPoliticianKey(original);
        politicianTable.put(newKey, original);
    }


    public void updateElection(Election original, Election updated) {
        String oldKey = buildElectionKey(original);
        electionTable.remove(oldKey);

        original.setType(updated.getType());
        original.setLocation(updated.getLocation());
        original.setDate(updated.getDate());
        original.setSeats(updated.getSeats());

        String newKey = buildElectionKey(original);
        electionTable.put(newKey, original);
    }



    // ============================================================
    // DELETE METHODS
    // ============================================================

    public boolean deletePolitician(Politician p) {
        if (p == null) return false;

        String key = buildPoliticianKey(p);
        boolean removed = politicianTable.remove(key);

        if (removed) {
            politicians.remove(p);
        }

        return removed;
    }


    public boolean deleteElection(Election e) {
        String key = buildElectionKey(e);

        boolean removed = electionTable.remove(key);
        if (removed) {
            elections.remove(e);
        }
        return removed;
    }

    // ============================================================
    // FIND METHODS
    // ============================================================

    public Politician findPoliticianByNameAndDob(String name, String dob) {
        String key = name.toLowerCase() + "|" + dob;
        Object o = politicianTable.get(key);
        if (o instanceof Politician) return (Politician) o;
        return null;
    }

    public Election findElection(String type, String location, String date) {
        String key = type.toLowerCase() + "|" + location.toLowerCase() + "|" + date;
        Object o = electionTable.get(key);
        if (o instanceof Election) return (Election) o;
        return null;
    }

    public MLinkedList<Politician> getPoliticians() {
        return politicians;
    }

    public MLinkedList<Election> getElections() {
        return elections;
    }

    // ============================================================
    // SEARCH METHODS
    // ============================================================

    public MLinkedList<Politician> searchPoliticians(String namePart, String party, String county) {

        MLinkedList<Politician> results = new MLinkedList<>();
        Node<Politician> n = politicians.getHead();

        String nameF = safe(namePart).toLowerCase();
        String partyF = safe(party).toLowerCase();
        String countyF = safe(county).toLowerCase();

        while (n != null) {
            Politician p = n.data;
            boolean match = true;

            if (!nameF.isEmpty() && !p.getName().toLowerCase().contains(nameF))
                match = false;

            if (!partyF.isEmpty() && !p.getParty().toLowerCase().contains(partyF))
                match = false;

            if (!countyF.isEmpty() && !p.getCounty().toLowerCase().contains(countyF))
                match = false;

            if (match) results.addElement(p);

            n = n.next;
        }

        return results;
    }

    public MLinkedList<Election> searchElections(String typePart, String yearPart) {

        MLinkedList<Election> results = new MLinkedList<>();
        Node<Election> n = elections.getHead();

        String typeF = safe(typePart).toLowerCase();
        String yearF = safe(yearPart).toLowerCase();

        while (n != null) {
            Election e = n.data;
            boolean match = true;

            if (!typeF.isEmpty() && !e.getType().toLowerCase().contains(typeF))
                match = false;

            if (!yearF.isEmpty() && !e.getDate().toLowerCase().contains(yearF))
                match = false;

            if (match) results.addElement(e);

            n = n.next;
        }

        return results;
    }

    // ============================================================
    // PERSISTENCE
    // ============================================================

    /**
     *
     * @param filename
     */
    public void saveToFile(String filename) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filename
     * @return
     */
    public static ElectionSystemManager loadFromFile(String filename) {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filename))) {
            Object o = ois.readObject();
            if (o instanceof ElectionSystemManager) {
                return (ElectionSystemManager) o;
            }
        } catch (Exception ignored) {
        }
        return new ElectionSystemManager();
    }

    /**
     *
     */
    public void reset() {
        politicianTable = new RMHashTable();
        electionTable = new RMHashTable();
        politicians.clear();
        elections.clear();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    /**
     *
     * @param s
     * @return
     */
    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
