package com.medic.eho.fhirnorth.dhirjavademo;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
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
    
    private DHIRService service; //service to interact with FHIR endpoint (DHIR)
    
    @FXML
    private TextField oiidTextField;
    @FXML
    private Label oiidErrorLabel;
    @FXML
    private TableView immunizationTableView;
    
    @FXML
    private void sampleButtonAction(ActionEvent event) {
        oiidTextField.setText("40254B02B4");
    }
    
    /**
     * This event performs a call to the DHIRService, which does a query for the entered OIID.
     * @param event 
     */
    @FXML
    private void searchButtonAction(ActionEvent event) {
        service = new DHIRService();
        
        oiidErrorLabel.setText("");
        immunizationTableView.setDisable(false);
        if(!oiidTextField.getText().equals("")){
            immunizationTableView.getItems().clear();
            Bundle results;
            
            try {
                results = service.read(oiidTextField.getText());
                
                if(results.getEntry() == null || results.getEntry().isEmpty()){
                    oiidErrorLabel.setText("No immunization records found.");
                    immunizationTableView.setDisable(true);
                    return;
                }
                
                for(Bundle.Entry entry : results.getEntry()){
                    Immunization resource = (Immunization)entry.getResource();
                    ObservableList<ImmunizationModel> tableData = immunizationTableView.getItems();
                    tableData.add(new ImmunizationModel(resource));
                }
            } 
            
            catch (Exception e) {
                oiidErrorLabel.setText("Error processing request.");
                immunizationTableView.setDisable(true);
                System.out.println(e);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
