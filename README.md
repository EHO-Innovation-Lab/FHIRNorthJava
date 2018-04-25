# FHIR North 2018 Java Applications
JavaFX applications making use of the HL7 FHIR STU3 Library to query DHDR &amp; DHIR
## Getting Started
### Before You Begin
Make sure you have the following installed:
Java Version 1.8.0
### Downloading the Applications
The code can be found at https://www.innovation-lab.ca in the [Code Sharing Repository](https://www.innovation-lab.ca/repository)
### Running the Applications
For successful querying to the DHDR/DHIR services, the applications require your Unique Sender Identifier to run.

1. Locate your Unique Sender Identifier here: https://www.innovation-lab.ca/Test-Portal
2. Add your Unique Sender Identifier to the indicated spot in the DHDRService.java/DHIRService.java files.
### Troubleshooting the Applications
A Q&A thread for the applications can be found at the [Innovation Lab Forums](https://www.innovation-lab.ca/discussions/topic/q-a-for-fhir-in-java/)
## Libraries
These applications make use of the [HAPI FHIR](http://hapifhir.io/) library.

## Things to Note
### Disable Metadata Validation
The HAPI FHIR library sends an initial metadata query for validation any time a client preforms a query.
In order to query DHIR/DHDR, this must be disabled on the client:
```ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER)```

## Author
Dawne Pierce - Ideaworks MEDIC