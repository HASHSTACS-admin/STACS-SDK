# STACS SDK

The STACS Java SDK supports integration of existing business applications into a STACS Native Network as well as the
creation of new web applications on top of a STACS Native Network.

### Pre-requisites
Request from the STACS node owner the following information:
1. Node Public Key
2. Node Merchant Id
3. Node AES Key
4. Node Gateway URL 
You will have to generate a keypair and will have to submit the public key to the STACS node owner.
These parameters will be recorded to config.properties.

### Usage

Ensure you have Maven installed and run the following:
```
mvn compile install 
```