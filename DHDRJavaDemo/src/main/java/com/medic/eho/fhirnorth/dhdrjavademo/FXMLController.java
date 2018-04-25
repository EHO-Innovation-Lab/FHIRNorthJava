package com.medic.eho.fhirnorth.dhdrjavademo;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FXMLController implements Initializable {

    private DHDRService service; //service to interact with FHIR endpoint (DHDR)

    @FXML
    private TextField hcnTextField;
    @FXML
    private Label hcnErrorLabel;
    @FXML
    private TableView medicationTableView;
    @FXML
    private TableView patientTableView;

    @FXML
    private void sampleButtonAction(ActionEvent event) {
        hcnTextField.setText("6948425589");
    }
    
    /**
     * This event performs a call to the DHDRService, which does a query for the entered HCN.
     * If the query returns a bundle with entries, the first entry is used to retrieve the master patient
     * @param event 
     */
    @FXML
    private void searchButtonAction(ActionEvent event) {
        service = new DHDRService();

        hcnErrorLabel.setText("");
        medicationTableView.setDisable(false);
        patientTableView.setDisable(false);
        if (!hcnTextField.getText().equals("")) {
            medicationTableView.getItems().clear();
            patientTableView.getItems().clear();
            Bundle results;

            try {
                results = service.read(hcnTextField.getText());

                if (results.getEntry() == null || results.getEntry().isEmpty()) {
                    hcnErrorLabel.setText("No medication dispense records found.");
                    medicationTableView.setDisable(true);
                    return;
                }
                
                //grabs master patient information out of the first medication dispense entry in the bundle
                MedicationDispense firstResource = (MedicationDispense) results.getEntryFirstRep().getResource();
                ObservableList<PatientModel> patientData = patientTableView.getItems();
                patientData.add(new PatientModel(firstResource));
                
                //grabs medication information from every medication dispense entry
                for (Bundle.Entry entry : results.getEntry()) {
                    MedicationDispense resource = (MedicationDispense) entry.getResource();
                    
                    ObservableList<MedicationModel> medicationData = medicationTableView.getItems();
                    medicationData.add(new MedicationModel(resource));
                }

            } catch (Exception e) {
                hcnErrorLabel.setText("Error processing request.");
                medicationTableView.setDisable(true);
                patientTableView.setDisable(true);
                System.out.println(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
