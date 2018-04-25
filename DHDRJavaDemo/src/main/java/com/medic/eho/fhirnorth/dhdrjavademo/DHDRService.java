package com.medic.eho.fhirnorth.dhdrjavademo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import java.util.UUID;

public class DHDRService {

    private final String serverBase = "http://lite.innovation-lab.ca:9443/dispense-service";
    private final FhirContext ctx = FhirContext.forDstu2();
    private IGenericClient client;

    /**
     * Initializes FHIR client
     * Generates pin for Immunization_Context, sets required HTTP headers and registers headers to client
     * ****SENDER ID MUST BE REPLACED WITH YOUR UNIQUE SENDER ID, FOUND AT https://www.innovation-lab.ca/Test-Portal****
     */
    public DHDRService(){
        client = ctx.newRestfulGenericClient(serverBase);
        
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        
        String senderId = "INSERT YOUR SENDER ID HERE";
        
        AdditionalRequestHeadersInterceptor headers = new AdditionalRequestHeadersInterceptor();
        headers.addHeaderValue("X-Sender-Id", senderId);
        headers.addHeaderValue("X-License-Text", "I hereby accept the service agreement here: https://innovation-lab.ca/media/1147/innovation-lab-terms-of-use.pdf");
        headers.addHeaderValue("ClientTxID", UUID.randomUUID().toString());
        
        client.registerInterceptor(headers);
    }

    /**
     * Performs a basic GET operation, querying the DHDR server by a single HCN
     * 
     * @param oiid Ontario Immunization ID
     * @return returns the query results
     */
    public Bundle read(String hcn) {
        Bundle results = client.search()
            .byUrl("MedicationDispense?patient:patient.identifier=https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-patient-hcn|" + hcn)
            .returnBundle(Bundle.class)
            .execute();
        return results;
    }
    
    /**
     * Example of how to convert a FHIR resource into a raw JSON string
     * @param resource resource to parse
     * @return raw JSON representation of Immunization object
     */
    public String parse(MedicationDispense resource){
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource);
    }
    
    /**
     * Example of how to convert a raw JSON string into a FHIR object
     * @param jsonResource resource to parse
     * @return FHIR object parsed from raw JSON
     */
    public MedicationDispense parse(String jsonResource){
        return ctx.newJsonParser().parseResource(MedicationDispense.class, jsonResource);
    }
}

