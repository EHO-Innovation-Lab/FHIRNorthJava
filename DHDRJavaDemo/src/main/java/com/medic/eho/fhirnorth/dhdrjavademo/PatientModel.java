package com.medic.eho.fhirnorth.dhdrjavademo;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import javafx.beans.property.SimpleStringProperty;

public class PatientModel {
    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private final SimpleStringProperty lastName = new SimpleStringProperty("");
    private final SimpleStringProperty gender = new SimpleStringProperty("");
    private final SimpleStringProperty birthDate = new SimpleStringProperty("");
    
    public PatientModel(){
        this("", "", "", "");
    }
    
    public PatientModel(String firstName, String lastName, String gender, String birthDate) {
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setBirthDate(birthDate);
    }
    
    /**
     * Constructor that allows the model to be parsed from a FHIR Immunization object
     * Iterates through the contained resources to select and retrieve information from the master patient resource
     * NOTE: MedicationDispense contains information for both the Master Patient and Dispense Patient, a check on the Patient Id is required to distinguish them
     * 
     * Checks if the desired properties exist in the object and sets the appropriately
     * @param resource 
     */
    public PatientModel(MedicationDispense resource) {
        String fName = "n/a";
        String lName = "n/a";
        String patientGender = "n/a";
        String bDate = "n/a";
        
        if (resource.getContained() != null && resource.getContained().getContainedResources() != null && !resource.getContained().getContainedResources().isEmpty()) {
            for (IResource contained : resource.getContained().getContainedResources()) {
                if (contained instanceof Patient && ((Patient)contained).getId().toString().contains("PatMaster")) {
                    Patient patient = (Patient) contained;
                    if (patient.getNameFirstRep() != null) {
                        if (patient.getNameFirstRep().getGivenAsSingleString() != null)
                            fName = patient.getNameFirstRep().getGivenAsSingleString();
                        if (patient.getNameFirstRep().getGivenAsSingleString() != null)
                            lName = patient.getNameFirstRep().getFamilyAsSingleString();
                    }
                    if (patient.getBirthDate() != null)
                        bDate = patient.getBirthDate().toString();
                    if (patient.getGender() != null)
                        patientGender = patient.getGender();
                }
            }
        }
        
        setFirstName(fName);
        setLastName(lName);
        setGender(patientGender);
        setBirthDate(bDate);
    }
    
    public String getFirstName(){
        return firstName.get();
    }
    
    public String getLastName(){
        return lastName.get();
    }
    
    public String getGender(){
        return gender.get();
    }
    
    public String getBirthDate(){
        return birthDate.get();
    }
    
    public void setFirstName(String firstName){
        this.firstName.set(firstName);
    }
    
    public void setLastName(String lastName){
        this.lastName.set(lastName);
    }
    
    public void setGender(String gender){
        this.gender.set(gender);
    }
    
    public void setBirthDate(String birthDate){
        this.birthDate.set(birthDate);
    }
}
