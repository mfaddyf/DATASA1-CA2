package org.example;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

public class ElectionSystemManagerTest {

    private ElectionSystemManager manager;

    @BeforeEach
    void setup() {
        manager = new ElectionSystemManager();
    }

    // ------------------------------------------------------------
    // ADD TESTS
    // ------------------------------------------------------------

    @Test
    void testAddPolitician() {
        Politician p = new Politician("Alice", "1990", "Green", "Waterford", "img");
        manager.addPolitician(p);

        assertEquals(1, manager.getPoliticians().size());
    }

    @Test
    void testAddElection() {
        Election e = new Election("General", "Waterford", "2024", 5);
        manager.addElection(e);

        assertEquals(1, manager.getElections().size());
    }

    @Test
    void testAddCandidateToElection() {
        Election e = new Election("General", "Waterford", "2024", 5);
        Politician p = new Politician("Alice", "1990", "Green", "Waterford", "img");
        Candidate c = new Candidate(p, "Green", 1000, e);

        manager.addElection(e);
        manager.addCandidateToElection(e, c);

        assertEquals(1, e.getCandidates().size());
    }

    // ------------------------------------------------------------
    // UPDATE TESTS
    // ------------------------------------------------------------

    @Test
    void testUpdatePolitician() {
        Politician p = new Politician("Alice", "1990", "Green", "Waterford", "img");
        manager.addPolitician(p);

        Politician updated = new Politician("Alice B", "1990", "Green", "Cork", "newimg");
        manager.updatePolitician(p, updated);

        assertEquals("Alice B", p.getName());
        assertEquals("Cork", p.getCounty());
        assertEquals("newimg", p.getImageUrl());
    }

    @Test
    void testUpdateElection() {
        Election e = new Election("General", "Waterford", "2024", 5);
        manager.addElection(e);

        Election updated = new Election("Local", "Cork", "2025", 10);
        manager.updateElection(e, updated);

        assertEquals("Local", e.getType());
        assertEquals("Cork", e.getLocation());
        assertEquals(10, e.getSeats());
    }

    // ------------------------------------------------------------
    // DELETE TESTS
    // ------------------------------------------------------------

    @Test
    void testDeletePolitician() {
        Politician p = new Politician("Alice", "1990", "Green", "Waterford", "img");
        manager.addPolitician(p);

        boolean removed = manager.deletePolitician(p);

        assertTrue(removed);
        assertEquals(0, manager.getPoliticians().size());
    }

    @Test
    void testDeleteElection() {
        Election e = new Election("General", "Waterford", "2024", 5);
        manager.addElection(e);

        boolean removed = manager.deleteElection(e);

        assertTrue(removed);
        assertEquals(0, manager.getElections().size());
    }

    // ------------------------------------------------------------
    // SEARCH TESTS
    // ------------------------------------------------------------

    @Test
    void testSearchPoliticians() {
        manager.addPolitician(new Politician("Alice", "1990", "Green", "Waterford", "img"));
        manager.addPolitician(new Politician("Bob", "1980", "Blue", "Cork", "img"));

        var results = manager.searchPoliticians("Ali", "", "");

        assertEquals(1, results.size());
        assertEquals("Alice", results.getHead().data.getName());
    }

    @Test
    void testSearchElections() {
        manager.addElection(new Election("General", "Waterford", "2024", 5));
        manager.addElection(new Election("Local", "Cork", "2025", 3));

        var results = manager.searchElections("Gen", "");

        assertEquals(1, results.size());
        assertEquals("General", results.getHead().data.getType());
    }

    // ------------------------------------------------------------
    // RESET TEST
    // ------------------------------------------------------------

    @Test
    void testReset() {
        manager.addPolitician(new Politician("Alice", "1990", "Green", "Waterford", "img"));
        manager.addElection(new Election("General", "Waterford", "2024", 5));

        manager.reset();

        assertEquals(0, manager.getPoliticians().size());
        assertEquals(0, manager.getElections().size());
    }

    // ------------------------------------------------------------
    // SAVE / LOAD TEST
    // ------------------------------------------------------------

    @Test
    void testSaveAndLoad() {
        String filename = "testdata.dat";

        manager.addPolitician(new Politician("Alice", "1990", "Green", "Waterford", "img"));
        manager.saveToFile(filename);

        ElectionSystemManager loaded = ElectionSystemManager.loadFromFile(filename);

        assertEquals(1, loaded.getPoliticians().size());

        new File(filename).delete();
    }
}
