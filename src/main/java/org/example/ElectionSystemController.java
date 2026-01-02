package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

public class ElectionSystemController {

    private ElectionSystemManager system = new ElectionSystemManager();


    // -------- POLITICIAN FIELDS --------
    @FXML private TextField polNameField;
    @FXML private TextField polDobField;
    @FXML private TextField polPartyField;
    @FXML private TextField polCountyField;
    @FXML private TextField polImageField;
    @FXML private ListView<String> politicianList;

    // -------- ELECTION FIELDS --------
    @FXML private ComboBox<String> electionTypeCombo;
    @FXML private TextField electionLocationField;
    @FXML private TextField electionDateField;
    @FXML private Spinner<Integer> electionSeatsSpinner;
    @FXML private ListView<String> electionList;

    // -------- CANDIDATE FIELDS --------
    @FXML private ComboBox<String> candidateElectionCombo;
    @FXML private ComboBox<String> candidatePoliticianCombo;
    @FXML private TextField candidatePartyField;
    @FXML private Spinner<Integer> candidateVotesSpinner;
    @FXML private ListView<String> candidateList;

    // -------- SEARCH FIELDS --------
    @FXML private TextField searchPolNameField;
    @FXML private TextField searchPolPartyField;
    @FXML private TextField searchPolCountyField;
    @FXML private TextField searchElectionTypeField;
    @FXML private TextField searchElectionYearField;
    @FXML private TextArea searchResultsArea;

    // ============================================================
    // INITIALIZE
    // ============================================================
    @FXML
    public void initialize() {

        // Election types
        electionTypeCombo.setItems(FXCollections.observableArrayList(
                "General", "Local", "European", "Presidential"
        ));

        // Spinners
        electionSeatsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1)
        );
        electionSeatsSpinner.setEditable(true);

        candidateVotesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0)
        );
        candidateVotesSpinner.setEditable(true);

        // LIST SELECTION LISTENERS
        politicianList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> loadSelectedPoliticianIntoFields(newV)
        );

        electionList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> {
                    loadSelectedElectionIntoFields(newV);
                    showCandidatesForSelectedElection();
                }
        );

        candidateList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> loadSelectedCandidateIntoFields(newV)
        );
    }

    // ============================================================
    // POLITICIANS
    // ============================================================

    @FXML
    public void handleAddPolitician() {
        Politician p = new Politician(
                safe(polNameField),
                safe(polDobField),
                safe(polPartyField),
                safe(polCountyField),
                safe(polImageField)
        );

        if (p.getName().isEmpty() || p.getDob().isEmpty()) return;

        system.addPolitician(p);
        refreshPoliticianViews();
        clearPoliticianInputs();
    }

    @FXML
    public void handleUpdatePolitician() {
        String selected = politicianList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Politician updated = new Politician(
                safe(polNameField),
                safe(polDobField),
                safe(polPartyField),
                safe(polCountyField),
                safe(polImageField)
        );

        system.updatePolitician(updated);
        refreshPoliticianViews();
    }

    @FXML
    public void handleDeletePolitician() {
        String selected = politicianList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String[] parts = selected.split("\\|");
        String name = parts[0].trim();
        String dob = parts[1].trim();

        system.deletePolitician(name, dob);
        refreshPoliticianViews();
    }

    private void loadSelectedPoliticianIntoFields(String display) {
        if (display == null) return;

        String[] parts = display.split("\\|");
        if (parts.length < 2) return;

        String name = parts[0].trim();
        String dob = parts[1].trim();

        Politician p = system.findPoliticianByNameAndDob(name, dob);
        if (p == null) return;

        polNameField.setText(p.getName());
        polDobField.setText(p.getDob());
        polPartyField.setText(p.getParty());
        polCountyField.setText(p.getCounty());
        polImageField.setText(p.getImageUrl());
    }

    private void refreshPoliticianViews() {
        politicianList.getItems().clear();
        candidatePoliticianCombo.getItems().clear();

        Node<Politician> n = system.getPoliticians().getHead();
        while (n != null) {
            Politician p = n.data;
            String display = p.getName() + " | " + p.getDob();
            politicianList.getItems().add(display);
            candidatePoliticianCombo.getItems().add(display);
            n = n.next;
        }
    }

    private void clearPoliticianInputs() {
        polNameField.clear();
        polDobField.clear();
        polPartyField.clear();
        polCountyField.clear();
        polImageField.clear();
    }

    // ============================================================
    // ELECTIONS
    // ============================================================

    @FXML
    public void handleAddElection() {
        Election e = new Election(
                electionTypeCombo.getValue(),
                safe(electionLocationField),
                safe(electionDateField),
                electionSeatsSpinner.getValue()
        );

        if (e.getType() == null || e.getLocation().isEmpty() || e.getDate().isEmpty()) return;

        system.addElection(e);
        refreshElectionViews();
        clearElectionInputs();
    }

    @FXML
    public void handleUpdateElection() {
        String selected = electionList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Election updated = new Election(
                electionTypeCombo.getValue(),
                safe(electionLocationField),
                safe(electionDateField),
                electionSeatsSpinner.getValue()
        );

        system.updateElection(updated);
        refreshElectionViews();
    }

    @FXML
    public void handleDeleteElection() {
        String selected = electionList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Election e = findElectionByDisplay(selected);
        if (e != null) {
            system.deleteElection(e);
            refreshElectionViews();
        }
    }

    private void loadSelectedElectionIntoFields(String display) {
        if (display == null) return;

        Election e = findElectionByDisplay(display);
        if (e == null) return;

        electionTypeCombo.setValue(e.getType());
        electionLocationField.setText(e.getLocation());
        electionDateField.setText(e.getDate());
        electionSeatsSpinner.getValueFactory().setValue(e.getSeats());
    }

    private void refreshElectionViews() {
        electionList.getItems().clear();
        candidateElectionCombo.getItems().clear();

        Node<Election> n = system.getElections().getHead();
        while (n != null) {
            Election e = n.data;
            String display = e.toString();
            electionList.getItems().add(display);
            candidateElectionCombo.getItems().add(display);
            n = n.next;
        }
    }

    private void clearElectionInputs() {
        electionTypeCombo.getSelectionModel().clearSelection();
        electionLocationField.clear();
        electionDateField.clear();
        electionSeatsSpinner.getValueFactory().setValue(1);
    }

    private Election findElectionByDisplay(String display) {
        Node<Election> n = system.getElections().getHead();
        while (n != null) {
            if (n.data.toString().equals(display)) return n.data;
            n = n.next;
        }
        return null;
    }

    // ============================================================
    // CANDIDATES
    // ============================================================

    @FXML
    public void handleAddCandidate() {
        String electionDisplay = candidateElectionCombo.getValue();
        String politicianDisplay = candidatePoliticianCombo.getValue();
        if (electionDisplay == null || politicianDisplay == null) return;

        Election e = findElectionByDisplay(electionDisplay);

        String[] parts = politicianDisplay.split("\\|");
        String name = parts[0].trim();
        String dob = parts[1].trim();

        Politician p = system.findPoliticianByNameAndDob(name, dob);

        Candidate c = new Candidate(
                p,
                safe(candidatePartyField),
                candidateVotesSpinner.getValue()
        );

        system.addCandidateToElection(e, c);
        refreshCandidateList(e);
        clearCandidateInputs();
    }

    @FXML
    public void handleUpdateCandidate() {
        String electionDisplay = candidateElectionCombo.getValue();
        String candidateDisplay = candidateList.getSelectionModel().getSelectedItem();
        if (electionDisplay == null || candidateDisplay == null) return;

        Election e = findElectionByDisplay(electionDisplay);
        Candidate old = e.findCandidateByDisplay(candidateDisplay);

        Candidate updated = new Candidate(
                old.getPolitician(),
                safe(candidatePartyField),
                candidateVotesSpinner.getValue()
        );

        e.updateCandidate(updated);
        refreshCandidateList(e);
    }

    @FXML
    public void handleDeleteCandidate() {
        String electionDisplay = candidateElectionCombo.getValue();
        String candidateDisplay = candidateList.getSelectionModel().getSelectedItem();
        if (electionDisplay == null || candidateDisplay == null) return;

        Election e = findElectionByDisplay(electionDisplay);
        Candidate c = e.findCandidateByDisplay(candidateDisplay);

        e.removeCandidateByPolitician(c.getPolitician());
        refreshCandidateList(e);
    }

    private void loadSelectedCandidateIntoFields(String display) {
        if (display == null) return;

        Election e = findElectionByDisplay(
                electionList.getSelectionModel().getSelectedItem()
        );
        if (e == null) return;

        Candidate c = e.findCandidateByDisplay(display);
        if (c == null) return;

        candidatePoliticianCombo.setValue(
                c.getPolitician().getName() + " | " + c.getPolitician().getDob()
        );
        candidatePartyField.setText(c.getPartyAtElection());
        candidateVotesSpinner.getValueFactory().setValue(c.getVotes());
    }

    private void showCandidatesForSelectedElection() {
        String selected = electionList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            candidateList.getItems().clear();
            return;
        }
        Election e = findElectionByDisplay(selected);
        refreshCandidateList(e);
    }

    private void refreshCandidateList(Election e) {
        candidateList.getItems().clear();
        if (e == null) return;

        Node<Candidate> n = e.getCandidates().getHead();
        while (n != null) {
            candidateList.getItems().add(n.data.toString());
            n = n.next;
        }
    }

    private void clearCandidateInputs() {
        candidateElectionCombo.getSelectionModel().clearSelection();
        candidatePoliticianCombo.getSelectionModel().clearSelection();
        candidatePartyField.clear();
        candidateVotesSpinner.getValueFactory().setValue(0);
    }

    // ============================================================
    // SEARCH
    // ============================================================

    @FXML
    public void handleSearchPoliticians() {
        MLinkedList<Politician> results = system.searchPoliticians(
                safe(searchPolNameField),
                safe(searchPolPartyField),
                safe(searchPolCountyField)
        );
        String output = "";
        Node<Politician> n = results.getHead();
        while (n != null) {
            output += n.data.toString() + "\n";
            n = n.next;
        }
        searchResultsArea.setText(output);
    }


    @FXML
    public void handleSearchElections() {
        MLinkedList<Election> results = system.searchElections(
                safe(searchElectionTypeField),
                safe(searchElectionYearField)
        );
        String output = "";
        Node<Election> n = results.getHead();
        while (n != null) {
            output += n.data.toString() + "\n";
            n = n.next;
        }
        searchResultsArea.setText(output);
    }


    // ============================================================
    // FILE OPERATIONS
    // ============================================================

    @FXML
    public void handleSaveData() {
        system.saveToFile("elections.dat");
    }

    @FXML
    public void handleLoadData() {
        system = ElectionSystemManager.loadFromFile("elections.dat");
        refreshPoliticianViews();
        refreshElectionViews();
        candidateList.getItems().clear();
    }

    @FXML
    public void handleResetSystem() {
        system.reset();
        refreshPoliticianViews();
        refreshElectionViews();
        candidateList.getItems().clear();
        searchResultsArea.clear();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private String safe(TextField f) {
        return f.getText() == null ? "" : f.getText().trim();
    }
}
