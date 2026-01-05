package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ElectionSystemController {

    private ElectionSystemManager system = new ElectionSystemManager();


    // -------- POLITICIAN FIELDS --------
    @FXML
    private TextField polNameField;
    @FXML
    private TextField polDobField;
    @FXML
    private TextField polPartyField;
    @FXML
    private TextField polCountyField;
    @FXML
    private TextField polImageField;
    @FXML
    private ListView<Politician> politicianList;

    private Politician selectedPolitician;


    // -------- ELECTION FIELDS --------
    @FXML
    private ComboBox<String> electionTypeCombo;
    @FXML
    private TextField electionLocationField;
    @FXML
    private TextField electionDateField;
    @FXML
    private Spinner<Integer> electionSeatsSpinner;
    @FXML
    private ListView<Election> electionList;

    private Election selectedElection;

    // -------- CANDIDATE FIELDS --------
    @FXML
    private ListView<Election> candidateElectionList;
    @FXML
    private ListView<Politician> candidatePoliticianList;
    @FXML
    private TextField candidatePartyField;
    @FXML
    private Spinner<Integer> candidateVotesSpinner;
    @FXML
    private ListView<Candidate> candidateList;

    private Candidate selectedCandidate;

    // -------- SEARCH FIELDS --------
    @FXML
    private TextField searchPolNameField;
    @FXML
    private TextField searchPolPartyField;
    @FXML
    private TextField searchPolCountyField;
    @FXML
    private TextField searchElectionTypeField;
    @FXML
    private TextField searchElectionYearField;
    @FXML
    private TextArea searchResultsArea;

    // -------- DROPDOWN SORTING FIELDS --------
    @FXML
    private ComboBox<String> searchSortComboPol;
    @FXML
    private ComboBox<String> searchSortComboEle;

    // ============================================================
    // INITIALIZE
    // ============================================================
    @FXML
    public void initialize() {

        // Election types
        electionTypeCombo.setItems(FXCollections.observableArrayList(
                "General", "Local", "Presidential"
        ));

        // Spinners
        electionSeatsSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1)
        );
        electionSeatsSpinner.setEditable(true);
        electionSeatsSpinner.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                try {
                    electionSeatsSpinner.increment(0); // forces commit
                } catch (Exception ignored) {}
            }
        });

        candidateVotesSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0)
        );
        candidateVotesSpinner.setEditable(true);

        searchSortComboPol.setItems(FXCollections.observableArrayList(
                "Alphabetical (A-Z)", "Party, then Name"
        ));

        searchSortComboEle.setItems(FXCollections.observableArrayList(
                "Type (A-Z)", "Year (Ascending)", "Year (Descending)"
        ));

        electionList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedElection = newVal;
                loadElectionIntoFields(newVal);
                showCandidatesForSelectedElection();
            }
        });

        politicianList.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                    if (newV != null) {
                        selectedPolitician = newV;
                        loadSelectedPoliticianIntoFields(newV);
                    }
                }
        );

        candidateList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> {
                    if (newV != null) {
                        selectedCandidate = newV;
                        loadSelectedCandidateIntoFields(newV);
                    }
                }
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
        if (selectedPolitician == null) return;

        Politician updated = new Politician(
                polNameField.getText(),
                polDobField.getText(),
                polPartyField.getText(),
                polCountyField.getText(),
                polImageField.getText()
        );

        system.updatePolitician(selectedPolitician, updated);
        refreshPoliticianViews();
    }


    @FXML
    public void handleDeletePolitician() {
        Politician selected = politicianList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        system.deletePolitician(selected);
        refreshPoliticianViews();
        candidateList.getItems().clear();
    }

    private void loadSelectedPoliticianIntoFields(Politician p) {
        if (p == null) return;

        polNameField.setText(p.getName());
        polDobField.setText(p.getDob());
        polPartyField.setText(p.getParty());
        polCountyField.setText(p.getCounty());
        polImageField.setText(p.getImageUrl());
    }

    private void refreshPoliticianViews() {
        RMSortingAlgo.sortList(system.getPoliticians(), new RMSortingAlgo.PoliticianNameComp());

        politicianList.getItems().clear();
        candidatePoliticianList.getItems().clear();

        Node<Politician> n = system.getPoliticians().getHead();
        while (n != null) {
            Politician p = n.data;
            politicianList.getItems().add(p);
            candidatePoliticianList.getItems().add(p);
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
        if (selectedElection == null) return;

        Election updated = new Election(
                electionTypeCombo.getValue(),
                electionLocationField.getText(),
                electionDateField.getText(),
                electionSeatsSpinner.getValue()
        );

        system.updateElection(selectedElection, updated);
        refreshElectionViews();
    }



    @FXML
    public void handleDeleteElection() {
        Election selected = electionList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        system.deleteElection(selected);
        refreshElectionViews();
        candidateList.getItems().clear();
    }

    private void loadElectionIntoFields(Election e) {
        electionTypeCombo.setValue(e.getType());
        electionLocationField.setText(e.getLocation());
        electionDateField.setText(e.getDate());
        electionSeatsSpinner.getValueFactory().setValue(e.getSeats());
    }

    private void refreshElectionViews() {
        electionList.getItems().clear();
        candidateElectionList.getItems().clear();

        Node<Election> n = system.getElections().getHead();
        while (n != null) {
            Election e = n.data;
            electionList.getItems().add(e);
            candidateElectionList.getItems().add(e);
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
        Election e = candidateElectionList.getSelectionModel().getSelectedItem();
        Politician p = candidatePoliticianList.getSelectionModel().getSelectedItem();

        if (e == null || p == null) return;

        Candidate c = new Candidate(
                p,
                safe(candidatePartyField),
                candidateVotesSpinner.getValue(),
                e
        );

        system.addCandidateToElection(e, c);
        refreshCandidateList(e);
        clearCandidateInputs();
    }

    @FXML
    public void handleUpdateCandidate() {
        Candidate c = candidateList.getSelectionModel().getSelectedItem();
        if (c == null) return;

        c.setPartyAtElection(candidatePartyField.getText());
        c.setVotes(candidateVotesSpinner.getValue());
        candidateList.refresh();
    }


    @FXML
    public void handleDeleteCandidate() {
        Candidate c = candidateList.getSelectionModel().getSelectedItem();
        if (c == null) return;

        Election e = c.getElection();
        e.removeCandidate(c);

        refreshCandidateList(e);
    }

    private void loadSelectedCandidateIntoFields(Candidate c) {
        candidateElectionList.getSelectionModel().select(c.getElection());
        candidatePoliticianList.getSelectionModel().select(c.getPolitician());
        candidatePartyField.setText(c.getPartyAtElection());
        candidateVotesSpinner.getValueFactory().setValue(c.getVotes());
    }

    private void showCandidatesForSelectedElection() {
        Election e = electionList.getSelectionModel().getSelectedItem();
        refreshCandidateList(e);
    }


    private void refreshCandidateList(Election e) {
        candidateList.getItems().clear();
        if (e == null) return;

        Node<Candidate> n = e.getCandidates().getHead();
        while (n != null) {
            candidateList.getItems().add(n.data);
            n = n.next;
        }
    }

    private void refreshCandidateViews() {

        candidateElectionList.getItems().clear();
        Node<Election> eNode = system.getElections().getHead();
        while (eNode != null) {
            candidateElectionList.getItems().add(eNode.data);
            eNode = eNode.next;
        }

        candidatePoliticianList.getItems().clear();
        Node<Politician> pNode = system.getPoliticians().getHead();
        while (pNode != null) {
            candidatePoliticianList.getItems().add(pNode.data);
            pNode = pNode.next;
        }

        Election selected = electionList.getSelectionModel().getSelectedItem();
        refreshCandidateList(selected);

        clearCandidateInputs();
    }





    private void clearCandidateInputs() {
        candidateElectionList.getSelectionModel().clearSelection();
        candidatePoliticianList.getSelectionModel().clearSelection();
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

        String sortOption = searchSortComboPol.getValue();

        if (sortOption != null) {
            switch (sortOption) {
                case "Alphabetical (A-Z)":
                    RMSortingAlgo.sortList(results, new RMSortingAlgo.PoliticianNameComp());
                    break;

                case "Party, then Name":
                    RMSortingAlgo.sortList(results, new RMSortingAlgo.PoliticianPartyTheNameComp());
                    break;
            }
        }

        String output = "";

        Node<Politician> n = results.getHead();
        while (n != null) {
            Politician p = n.data;

            // Basic info
            output += p.getName() + " | "
                    + p.getDob() + " | "
                    + p.getParty() + " | "
                    + p.getCounty() + " | "
                    + p.getImageUrl() + "\n";

            output += "---------------------------------\n";
            output += "Candidate in the following elections:\n";

            // Elections they ran in
            Node<Election> eNode = system.getElections().getHead();
            while (eNode != null) {
                Election e = eNode.data;

                Node<Candidate> cNode = e.getCandidates().getHead();
                while (cNode != null) {
                    Candidate c = cNode.data;

                    if (c.getPolitician() == p) {
                        output += e.getType() + " | "
                                + e.getLocation() + " | "
                                + e.getDate() + " | Seats: "
                                + e.getSeats() + " | "
                                + c.getPartyAtElection() + "\n";
                    }

                    cNode = cNode.next;
                }

                eNode = eNode.next;
            }

            output += "\n"; // blank line between politicians
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

        String sortOption = searchSortComboEle.getValue();

        if (sortOption != null) {
            switch (sortOption) {
                case "Type (A–Z)":
                    RMSortingAlgo.sortList(results, new RMSortingAlgo.ElectionTypeComp());
                    break;

                case "Year (Ascending)":
                    RMSortingAlgo.sortList(results, new RMSortingAlgo.ElectionYearAscComp());
                    break;

                case "Year (Descending)":
                    RMSortingAlgo.sortList(results, new RMSortingAlgo.ElectionYearDescComp());
                    break;
            }
        }

        String output = "";

        Node<Election> n = results.getHead();
        while (n != null) {
            Election e = n.data;

            output += e.getType() + " | "
                    + e.getLocation() + " | "
                    + e.getDate() + " | "
                    + "Seats: " + e.getSeats() + "\n";

            output += "---------------------------------\n";
            output += "Candidates (sorted by votes):\n";

            MLinkedList<Candidate> sorted = new MLinkedList<>();
            Node<Candidate> cNode = e.getCandidates().getHead();
            while (cNode != null) {
                sorted.addElement(cNode.data);
                cNode = cNode.next;
            }

            RMSortingAlgo.sortList(sorted, new RMSortingAlgo.CandidateVotesDescComp());

            int seats = e.getSeats();
            int index = 0;

            Node<Candidate> sortedNode = sorted.getHead();
            while (sortedNode != null) {
                Candidate c = sortedNode.data;

                String line = c.getPolitician().getName() + " | "
                        + c.getVotes() + " votes | "
                        + c.getPartyAtElection();

                if (index < seats) {
                    line = "* " + line;
                }

                output += line + "\n";

                index++;
                sortedNode = sortedNode.next;
            }

            output += "\n"; // blank line between elections
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
        refreshCandidateViews();

        if (!electionList.getItems().isEmpty()) {
            electionList.getSelectionModel().select(0);
            showCandidatesForSelectedElection();
        }
    }


    @FXML
    public void handleResetSystem() {
        system.reset();
        refreshPoliticianViews();
        refreshElectionViews();
        refreshCandidateViews();
        searchResultsArea.clear();
    }

    // ============================================================
    // MAP BUILDING
    // ============================================================
    @FXML
    public void handleShowSystemMap() {
        Stage mapStage = new Stage();
        mapStage.setTitle("Election Browser");

        Pane mapPane = new Pane();
        mapPane.setPrefSize(600, 600);

        int electionIndex = 0;
        Node<Election> eNode = system.getElections().getHead();

        while (eNode != null) {
            Election e = eNode.data;

            Label electionLabel = new Label(
                    e.getType() + " | " + e.getLocation() + " | " + e.getDate()
            );
            electionLabel.setLayoutX(20);
            electionLabel.setLayoutY(40 + (electionIndex * 40));
            electionLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

            // Clicking an election opens sorted candidate list
            electionLabel.setOnMouseClicked(ev -> showElectionPopup(e));

            mapPane.getChildren().add(electionLabel);

            electionIndex++;
            eNode = eNode.next;
        }

        Scene scene = new Scene(mapPane);
        mapStage.setScene(scene);
        mapStage.show();
    }



    private void showPoliticianPopup(Politician p) {
        Stage stage = new Stage();
        stage.setTitle("Politician Details");

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label name = new Label("Name: " + p.getName());
        Label party = new Label("Party: " + p.getParty());
        Label county = new Label("County: " + p.getCounty());
        Label url = new Label("Photo URL: " + p.getImageUrl());

        box.getChildren().addAll(name, party, county, url);

        stage.setScene(new Scene(box, 300, 200));
        stage.show();
    }


    private void showElectionPopup(Election e) {
        Stage stage = new Stage();
        stage.setTitle("Election Details");

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label title = new Label(e.getType() + " Election");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label info = new Label(
                "Location: " + e.getLocation() + "\n" +
                        "Date: " + e.getDate() + "\n" +
                        "Seats: " + e.getSeats()
        );

        Label candidatesHeader = new Label("Candidates (sorted by votes):");
        candidatesHeader.setStyle("-fx-font-weight: bold;");

        ListView<Label> candidateList = new ListView<>();

        // Copy and sort candidates
        MLinkedList<Candidate> sorted = new MLinkedList<>();
        Node<Candidate> cNode = e.getCandidates().getHead();
        while (cNode != null) {
            sorted.addElement(cNode.data);
            cNode = cNode.next;
        }

        RMSortingAlgo.sortList(sorted, new RMSortingAlgo.CandidateVotesDescComp());

        // Highlight ONLY the first candidate
        boolean first = true;

        Node<Candidate> sortedNode = sorted.getHead();
        while (sortedNode != null) {
            Candidate c = sortedNode.data;

            Label row = new Label(
                    c.getPolitician().getName() + " — " + c.getVotes() + " votes"
            );

            if (first) {
                row.setText(row.getText().toUpperCase());
                row.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                first = false;
            }

            // Clicking candidate → show politician details
            row.setOnMouseClicked(ev -> showPoliticianPopup(c.getPolitician()));

            candidateList.getItems().add(row);

            sortedNode = sortedNode.next;
        }

        box.getChildren().addAll(title, info, candidatesHeader, candidateList);

        stage.setScene(new Scene(box, 350, 450));
        stage.show();
    }

    // ============================================================
    // HELPERS
    // ============================================================

    private String safe(TextField f) {
        return f.getText() == null ? "" : f.getText().trim();
    }
}
