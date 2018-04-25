package com.medic.eho.fhirnorth.dhirjavademo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;

public class DHIRService {

    private final String serverBase = "http://lite.innovation-lab.ca:9443/dhir-lite";
    private final FhirContext ctx = FhirContext.forDstu2();
    private IGenericClient client;

    /**
     * Initializes FHIR client
     * Generates pin for Immunization_Context, sets required HTTP headers and registers headers to client
     * ****SENDER ID MUST BE REPLACED WITH YOUR UNIQUE SENDER ID, FOUND AT https://www.innovation-lab.ca/Test-Portal****
     */
    public DHIRService(){
        client = ctx.newRestfulGenericClient(serverBase);
        
        //required to prevent the client from sending a metadata request not supported by DHIR
        ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
        
        String pin = "";
        try {
            pin = generatePin();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        String senderId = "INSERT YOUR SENDER ID HERE";
        
        AdditionalRequestHeadersInterceptor headers = new AdditionalRequestHeadersInterceptor();
        headers.addHeaderValue("Immunizations_Context", pin);
        headers.addHeaderValue("X-Sender-Id", senderId);
        headers.addHeaderValue("X-License-Text", "I hereby accept the service agreement here: https://innovation-lab.ca/media/1147/innovation-lab-terms-of-use.pdf");
        headers.addHeaderValue("ClientTxID", UUID.randomUUID().toString());
        
        client.registerInterceptor(headers);
    }
    
    /**
     * Performs a basic GET operation, querying the DHIR server by a single OIID
     * NOTE: To query by the properties of the referenced patient, use .hasChainedProperty method to access patient
     *       ( e.g. Immunization.PATIENT.hasChainedProperty(Patient.FAMILY.matches(lastName)) )
     * 
     * @param oiid Ontario Immunization ID
     * @return returns the query results
     */
    public Bundle read(String oiid) {
        Bundle results = client.search()
            .forResource(Immunization.class)
            .where(Immunization.PATIENT.hasChainedProperty(Patient.IDENTIFIER.exactly().identifier("https://ehealthontario.ca/API/FHIR/NamingSystem/ca-on-panorama-immunization-id|" + oiid)))
            .returnBundle(Bundle.class)
            .execute();
        return results;
    }
    
    /**
     * Example of how to convert a FHIR resource into a raw JSON string
     * @param immunization resource to parse
     * @return raw JSON representation of Immunization object
     */
    public String parse(Immunization immunization){
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(immunization);
    }
    
    /**
     * Example of how to convert a raw JSON string into a FHIR object
     * @param jsonResource resource to parse
     * @return FHIR object parsed from raw JSON
     */
    public Immunization parse(String jsonResource){
        return ctx.newJsonParser().parseResource(Immunization.class, jsonResource);
    }
    
    /**
     * Performs proper pin formatting according to guidelines found here: https://innovation-lab.ca/preparing-immunization-pin/
     * First hashes pin using SHA-256, then encodes the hashed value using Base64
     * NOTE: It is necessary to convert the hashed value into a HEX string before encoding with Base64 for proper results
     * 
     * @return the encoded pin
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException 
     */
    private String generatePin() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String base = "123123"; //example pin
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(base.getBytes());
        byte[] hash = md.digest();
        
        String hashedString = DatatypeConverter.printHexBinary(hash).toLowerCase();
        
        byte[] formattedHash = 
            ("{\n" +
            "\"pin\":\"" + hashedString + "\"\n" +
            "}")
            .getBytes("UTF-8");
        
        return Base64.getEncoder().encodeToString(formattedHash);
    }
}
