# FHIR North 2018 Java Application
JavaFX application making use of the HL7 FHIR STU3 Library to query DHDR &amp; DHIR

## Libraries
The major library used was [HAPI FHIR](http://hapifhir.io/)

## Code Samples
### Adding Headers to the Client
The following code block shows how to add headers to the FHIR client:
```AdditionalRequestHeadersInterceptor header = new AdditionalRequestHeadersInterceptor();```
```headers.addHeaderValue("ClientTxID", UUID.randomUUID().toString());```
```client.registerInterceptor(headers);```

### Searching
The following code show how to build and execute a search using the FHIR client:
``` 
Bundle results = client.search()
  .forResource(Immunization.class)
  .where(Immunization.PATIENT.hasChainedProperty(Patient.IDENTIFIER.exactly().identifier(searchString)))
  .returnBundle(Bundle.class)
  .execute();
```

## Things to Note
### Chaining Properties
In order to query properties of referenced resources, use the .hasChainedProperty method:
```Immunization.PATIENT.hasChainedProperty(Patient.IDENTIFIER.exactly().identifier(searchString)```

### Disable Metadata Validation
The HAPI FHIR library sends an initial metadata query for validation any time a client preforms a query.
In order to query DHIR/DHDR, this must be disabled on the client:
```ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER)```

## Author
Dawne Pierce - Ideaworks MEDIC
