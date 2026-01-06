package org.example;

import java.io.*;

public class ElectionSystemManager implements Serializable {

    // two hash tables
    private RMHashTable politicianTable;
    private RMHashTable electionTable;

    private MLinkedList<Politician> politicians;
    private MLinkedList<Election> elections;

    // declaring and creating the hashtables / linkedlists
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

    /**
     * Adds the politician to the system.
     * If the politician's key isn't already in the hashtable, the politician is also given a key
     * which is appended to the linked list.
     *
     * @param p the Politician to add
     */
    public void addPolitician(Politician p) {
        String key = buildPoliticianKey(p);

        // checks to see if the key exists
        if (!politicianTable.containsKey(key)) {
            politicians.addElement(p);
        }
        // inserts / updates key
        politicianTable.put(key, p);
    }

    /**
     * Adds the election to the system.
     * If the election's key isn't already in the hashtable, the election is also given a key
     * which is appended to the linked list.
     *
     * @param e the Election to add
     */
    public void addElection(Election e) {
        String key = buildElectionKey(e);

        // checks to see if the key exists
        if (!electionTable.containsKey(key)) {
            elections.addElement(e);
        }
        // inserts / updates key
        electionTable.put(key, e);
    }

    /**
     * Adds a candidate to a specific election
     *
     * @param election the Election the candidate is running in
     * @param candidate the Candidate that is to be added to the election
     */
    public void addCandidateToElection(Election election, Candidate candidate) {
        if (election != null && candidate != null) {
            election.addCandidate(candidate);
        }
    }

    // ============================================================
    // UPDATE METHODS
    // ============================================================

    /**
     * Upadtes an already existing politician by removing the old key from the hash table, modifying the
     * original object, and inserting the info under a new key.
     *
     * @param original the existing politician to update
     * @param updated the updated politician with all the new fields
     */
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

    /**
     * Upadtes an already existing election by removing the old key from the hash table, modifying the
     * original object, and inserting the info under a new key.
     *
     * @param original the existing election to update
     * @param updated the updated election with all the new fields
     */
    public void updateElection(Election original, Election updated) {
        String oldKey = buildElectionKey(original);

        // remove old key
        electionTable.remove(oldKey);

        // updates fields
        original.setType(updated.getType());
        original.setLocation(updated.getLocation());
        original.setDate(updated.getDate());
        original.setSeats(updated.getSeats());

        // inserts new key
        String newKey = buildElectionKey(original);
        electionTable.put(newKey, original);
    }

    // ============================================================
    // DELETE METHODS
    // ============================================================

    /**
     * Deletes a politician from both the hash table and the linked list.
     *
     * @param p the politician to delete
     * @return true if removed, false otherwise
     */
    public boolean deletePolitician(Politician p) {
        // if empty then return false
        if (p == null) return false;

        String key = buildPoliticianKey(p);
        // if built key is removed, removed = true
        boolean removed = politicianTable.remove(key);

        // if removed = true, remove same politician from linked list
        if (removed) {
            politicians.remove(p);
        }
        return removed;
    }

    /**
     * Deletes an election from both the hash table and the linked list.
     *
     * @param e the election to delete
     * @return true if removed, false otherwise
     */
    public boolean deleteElection(Election e) {
        // if empty then return false
        if (e == null) return false;

        String key = buildElectionKey(e);
        // if built key is removed, removed = true
        boolean removed = electionTable.remove(key);

        // if removed = true, remove same election from linked list
        if (removed) {
            elections.remove(e);
        }
        return removed;
    }

    // ============================================================
    // FIND METHODS
    // ============================================================

    public MLinkedList<Politician> getPoliticians() {
        return politicians;
    }

    public MLinkedList<Election> getElections() {
        return elections;
    }

    // ============================================================
    // SEARCH METHODS
    // ============================================================

    /**
     * Searches all politicians using optional filters for name, party, county.
     * Matching is case-insensitive.
     *
     * @param namePart partial or full name to match
     * @param party partial or full party name to match
     * @param county partial or full county name to match
     * @return a linked list of matching Politicians
     */
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

    /**
     * Searches all elections using optional filters for type, year.
     * Matching is case-insensitive.
     *
     * @param typePart partial or full election type to match
     * @param yearPart partial or full year to match
     * @return a linked list of matching Elections
     */
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
     * Saves the entire system state to a file using java serialisation.
     *
     * @param filename the file to write to / create
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
     * Loads saved election system manager, if the loading fails, a new manager is created
     *
     * @param filename the file to load from
     * @return loaded manager or a new manager
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
     * Resets the system by clearing all hashtables and linked lists
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
     * Ensures that any string used for searching, filtering, obj creation is never null
     *
     * @param s the input string
     * @return a non-null, trimmed string, returns an empty string if null String s is null
     */
    private String safe(String s) {
        return (s == null) ? "" : s.trim();
    }
}
