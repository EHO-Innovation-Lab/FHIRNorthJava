package com.medic.eho.fhirnorth.dhdrjavademo;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import javafx.beans.property.SimpleStringProperty;

public class MedicationModel {

    private final SimpleStringProperty medicationDisplay = new SimpleStringProperty("");
    private final SimpleStringProperty dateDispensed = new SimpleStringProperty("");
    private final SimpleStringProperty daysSupply = new SimpleStringProperty("");
    private final SimpleStringProperty practitioner = new SimpleStringProperty("");

    public MedicationModel() {
        this("", "", "", "");
    }

    public MedicationModel(String display, String date, String supply, String practitioner) {
        setMedicationDisplay(display);
        setDateDispensed(date);
        setDaysSupply(supply);
        setPractitioner(practitioner);
    }
    
    /**
     * Constructor that allows the model to be parsed from a FHIR MedicationDispense object
     * Iterates through the contained resources to select and retrieve information from the medication resource and practitioner resource
     * Checks if the desired properties exist in the object and sets the appropriately
     * @param resource 
     */
    public MedicationModel(MedicationDispense resource) {
        String display = "n/a";
        String date = "n/a";
        String supply = "n/a";
        String practionerName = "n/a";
        
        if (resource.getDaysSupply() != null)
            supply = resource.getDaysSupply().getValue().toString();
        if (resource.getWhenHandedOver() != null) 
            date = resource.getWhenHandedOver().toString();
        
        if (resource.getContained() != null && resource.getContained().getContainedResources() != null && !resource.getContained().getContainedResources().isEmpty()) {
            for (IResource contained : resource.getContained().getContainedResources()) {
                if (contained instanceof Medication) {
                    Medication med = (Medication) contained;
                    if (med.getCode() != null && med.getCode().getCoding() != null && !med.getCode().getCoding().isEmpty()) {
                        display = med.getCode().getCoding().get(0).getDisplay();
                    }
                }
                if (contained instanceof Practitioner) {
                    Practitioner p = (Practitioner) contained;
                    String firstName = "";
                    String lastName = "";
                    if (p.getName() != null && p.getName().getGivenAsSingleString() != null) {
                        firstName = p.getName().getGivenAsSingleString();
                    }
                    if (p.getName() != null && p.getName().getFamilyAsSingleString() != null) {
                        lastName = p.getName().getFamilyAsSingleString();
                    }
                    practionerName = firstName + " " + lastName;
                }
            }
        }
        setMedicationDisplay(display);
        setDateDispensed(date);
        setDaysSupply(supply);
        setPractitioner(practionerName);
    }

    public String getMedicationDisplay() {
        return medicationDisplay.get();
    }

    public String getDateDispensed() {
        return dateDispensed.get();
    }

    public String getDaysSupply() {
        return daysSupply.get();
    }

    public String getPractitioner() {
        return practitioner.get();
    }

    public void setMedicationDisplay(String display) {
        medicationDisplay.set(display);
    }

    public void setDateDispensed(String date) {
        dateDispensed.set(date);
    }

    public void setDaysSupply(String supply) {
        daysSupply.set(supply);
    }

    public void setPractitioner(String practitioner) {
        this.practitioner.set(practitioner);
    }
}
