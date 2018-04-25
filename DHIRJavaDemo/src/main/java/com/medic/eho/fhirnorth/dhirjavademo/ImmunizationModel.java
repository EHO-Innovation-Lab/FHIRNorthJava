package com.medic.eho.fhirnorth.dhirjavademo;

import ca.uhn.fhir.model.dstu2.resource.Immunization;
import javafx.beans.property.SimpleStringProperty;

public class ImmunizationModel {
    private final SimpleStringProperty immunization = new SimpleStringProperty("");
    private final SimpleStringProperty dateGiven = new SimpleStringProperty("");
    
    public ImmunizationModel() {
        this("", "");
    }
    
    public ImmunizationModel(String immunization, String date) {
        setImmunization(immunization);
        setDateGiven(date);
    }
    
    /**
     * Constructor that allows the model to be parsed from a FHIR Immunization object
     * Checks if the desired properties exist in the object and sets the appropriately
     * @param resource 
     */
    public ImmunizationModel(Immunization resource) {
        String immunizationText = "n/a";
        String date = "n/a";
        
        if(resource.getVaccineCode() != null && resource.getVaccineCode().getText() != null){
            immunizationText = resource.getVaccineCode().getText();
        }
        if(resource.getDate() != null)
            date = resource.getDate().toString();
        
        setImmunization(immunizationText);
        setDateGiven(date);
    }
    
    public String getImmunization() {
        return immunization.get();
    }
    
    public void setImmunization(String immunization){
        this.immunization.set(immunization);
    }
    
    public String getDateGiven() {
        return dateGiven.get();
    }
    
    public void setDateGiven(String date) {
        dateGiven.set(date);
    }
}
