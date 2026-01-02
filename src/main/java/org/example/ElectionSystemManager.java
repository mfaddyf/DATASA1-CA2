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

    public void updatePolitician(Politician updated) {
        String key = buildPoliticianKey(updated);
        politicianTable.put(key, updated);

        Node<Politician> n = politicians.getHead();
        while (n != null) {
            Politician p = n.data;
            if (p.getName().equalsIgnoreCase(updated.getName()) &&
                    p.getDob().equals(updated.getDob())) {
                n.data = updated;
                return;
            }
            n = n.next;
        }
    }

    public void updateElection(Election updated) {
        String key = buildElectionKey(updated);
        electionTable.put(key, updated);

        Node<Election> n = elections.getHead();
        while (n != null) {
            Election e = n.data;
            if (e.matches(updated)) {
                n.data = updated;
                return;
            }
            n = n.next;
        }
    }

    // ============================================================
    // DELETE METHODS
    // ============================================================

    public boolean deletePolitician(String name, String dob) {
        String key = name.toLowerCase() + "|" + dob;

        Politician p = findPoliticianByNameAndDob(name, dob);
        if (p == null) return false;

        politicianTable.remove(key);
        politicians.remove(p);

        // Remove from all elections
        Node<Election> e = elections.getHead();
        while (e != null) {
            e.data.removeCandidateByPolitician(p);
            e = e.next;
        }

        return true;
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
        } catch (Exception ignored) {}
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
