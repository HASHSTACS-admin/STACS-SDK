# STACS SDK

The STACS Protocol is a STACS Permissioned Blockchain Network that exposes REST API endpoints for applications to connect and interact with the underlying distributed ledger. This SDK provides all essential tools for communicating with a node residing in a STACS network including signing of transactions and encryption in transit. 

The STACS Java SDK enables applications to sit on top of a STACS Native Permissioned Network and run business processes related to digital assets and tokenized securities by calling the relevant smart contracts encapsulated in the SDK library. 

## SDK Key Developer Features
1. Invoke Smart Contract Function calls directly to the STACS network 
2. SDK function calls have built-in signatures for sending requests with your private key
3. SDK method calls send transactions to the network by default with encryption in transit
4. Transaction requests that modify the ledger are sent as Asynchronous REST calls and separate queries are provided to verify state changes in the ledger
5. Queries of the state of the ledger are provided as Synchronous REST calls that return data in JSON format
6. SDK provides both the raw returned JSON strings and Java Data Transfer Objects (DTOs) that are mapped to the JSON strings   

## Available Smart Contracts
These are smart contracts that are provided out-of-box with a standard STACS protocol deployment.

This SDK provides both the DTO Object (a request Business Object, reqBO) containing parameters required for sending the API request as well as the actual method call to that fires off the REST request to the blockchain node.  

The REST API response is captured as a Response DTO Object, respBO where the raw JSON string is stored while processing is done by the SDK to extract the JSON content into the Response object itself.

The method calls have been divided into chain-related calls and wallet-related calls. 

### Chain-related Methods

While sample codes have been provided to indicate the required parameters of the DTO object, examples of the actual execution for each smart contract listed below is also provided in the `ChainSampleUsage()` function in the `SampleUsage.java` file. 

#### Asynchronous Method Calls to modify the ledger 
These calls sends transaction requests to modify the ledger (such as issuing a token) and each request has a transaction id that can be used in queries to check if the STACS network has accepted the transaction requests. 

Smart Contract Description | Java DTO Request Object | Method Call | Sample Code  
------------------------   | ----------------------  | ----------- | -----------  
Issue a digital asset (token) to the chain | `IssueTokenReqBO` | `ChainConnector.issueToken` | `IssueTokenSample.java` 
Grant KYC subscription permission to a user wallet account | `GrantSubscribePermReqBO` | `ChainConnector.grantSubscriptionPermission` | GrantSubscriptionPermissionSample.java`
Investor with valid KYC profile to subscribe to a digital asset | `SubscribeReqBO` |  `ChainConnector.subscribeToToken` | `SubscriptionSample.java`
Create a payment record for distributing payments (i.e. coupons, dividends) to a digital asset | `GeneratePaymentRecordReqBO`| `ChainConnector.generatePaymentRecord` | N.A.
Distribute payments to the right investors | `DistributePaymentReqBO` | `ChainConnector.distributePayment` | `DistributePaymentsSample.java`
Freeze (and unfreeze) wallet accounts | `FreezeWalletReqBO` | `ChainConnector.freezeOrUnfreezeWallet` | N.A.
 
#### Synchronous Method Calls to verify the ledger state (sent by the Asynchronous method calls above)
These calls sends the transaction ids of their respective asynchronous method calls to the blockchain and receives a Response Object containing a JSON string. The Java DTO Response Object provided in this SDK provides both the raw JSON string as well as the extracted data within the same object. 
 
Query Description | Java DTO Response Object | Method Call 
----------------- | ------------------------ | -----------
Query status of a digital asset (token) issuance | `IssueTokenStatusRespBO` | `ChainConnector.getIssueTokenStatus`
Query status of a KYC subscription permission grant | `GrantSubscribePermStatusRespBO` | `ChainConnector.getGrantSubscriptionPermissionStatus`
Query status of a investor subscription | `SubscribeStatusRespBO` | `ChainConnector.getSubscriptionStatus`
Query status of a payment record | `GeneratePaymentRecordRespBO` | `ChainConnector.getPaymentRecordStatus`
Query status of a payment distribution (i.e. coupons, dividends) | `DistributePaymentStatusRespBO` | `ChainConnector.getDistributePaymentStatus`
Query status of a freeze and unfreeze action on a wallet account | `FreezeOrUnfreezeStatusRespBO` | `ChainConnector.getFreezeOrUnfreezeWalletStatus`
 
#### Synchronous Method Calls to query the ledger
These calls does a simple check of the ledger state with input parameters wrapped in a DTO Request object and returns a DTO response object.

Query Description | Java DTO Request Object | Java DTO Response Object | Method Call 
Get latest block height | N.A. | `LatestBlockRespBO` | `ChainConnector.getLatestBlockHeight` 
Retrieve all transactions for a specified length of blocks | `TxHistoryBetweenBlocksReqBO` | `TxHistoryRespBO` | `ChainConnector.getTxsBetweenBlocks` | N.A.
 
### Wallet-related Methods 

While sample codes have been provided to indicate the required parameters of the DTO object, examples of the actual execution for each smart contract listed below is also provided in the `WalletSampleUsage()` function in the `SampleUsage.java` file. 

#### Asynchronous Method Calls to modify the ledger  
These calls sends transaction requests to modify the ledger (such as transferring a token) and each request has a transaction id that can be used in queries to check if the STACS network has accepted the transaction requests. 

Smart Contract Description | Java DTO Request Object | Method Call | Sample Code  
------------------------   | ----------------------  | ----------- | -----------  
Transfer tokens from 1 wallet to another | `TransferTokenReqBO` | `WalletConnector.transferToken` | `TransferTokenSample.java` 
 
#### Synchronous Method Calls to verify the ledger state (sent by the Asynchronous method calls above)
These calls sends the transaction ids of their respective asynchronous method calls to the blockchain and receives a Response Object containing a JSON string. The Java DTO Response Object provided in this SDK provides both the raw JSON string as well as the extracted data within the same object. 

Query Description | Java DTO Response Object | Method Call 
----------------- | ------------------------ | -----------
Query status of a token transfer | `TransferTokenStatusRespBO` | `WalletConnector.getTransferTokenStatus`

#### Synchronous Method Calls to query the ledger
These calls does a simple check of the ledger state with input parameters wrapped in a DTO Request object and returns a DTO response object.  

Query Description | Java DTO Request Object | Java DTO Response Object | Method Call 
Query wallet balance | `WalletBalanceReqBO` | `WalletBalanceRespBO` | `WalletConnector.getWalletBalance` 
Query the full transaction history of a token or wallet | `TxHistoryReqBO` |` TxHistoryRespBO` | `WalletConnector.getTxHistoryForWalletAndOrCurrency` 

### Not covered in the SDK 
Key management is not part of the SDK. For production use, it is encouraged that you use a KMS to manage the private keys used for signing and encrypting. 
This does not include deployment of nodes for a STACS network.
This SDK version also does not cover the deployment of new or updated smart contracts to a STACS network.  

### Pre-requisites

Required minimum versions:
..* JDK 8
..* Apache Maven 3.6


Request from the STACS node owner the following information:
1. Node Public Key
2. Node Merchant Id
3. Node AES Key
4. Node Gateway URL 
You will also have to generate a keypair and will have to submit the public key to the STACS node owner.
The private key generated will have to be entered as the `node_prikey` field in the configuration file.

To generate a keypair, run the `StacsUtil.createWallet()` call. See `SampleUsage.java` for this function. 

These parameters will be required to be entered into the configuration file, `config.properties`.

### Getting Started

Detailed examples for each smart contract function is provided in the `SampleUsage.java` main file and can be run once the `config.properties` configuration file has been updated with the pre-requisite information. 

To begin, ensure you have Maven installed and run the Maven command:
```
mvn clean compile install
```

#### Including this SDK into your own Java projects
Run the following Maven command to generate a jar file that can be used 
```
mvn clean compile package install
```

The Junit tests require a live connection to an existing STACS network. To skip the JUnit tests, run the following instead:
```
mvn clean compile package install -DskipTests
```

Once the jar file above has been created add the following dependencies to your Java project Maven `pom.xml` file. 
```
<dependencies>
      <dependency>
    	<groupId>stacs-sdk</groupId>
  		<artifactId>stacs-sdk</artifactId>
  		<version>1.0.5</version>
    </dependency>
    <dependency>
  		<groupId>com.hashstacs</groupId>
  		<artifactId>stacs-base</artifactId>
  		<version>1.0.0</version>
  	</dependency> 
  	...
</dependencies>
```

To install the generated jar placed in your new project's `/lib` folder, add the following to your `pom.xml`:
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-install-plugin</artifactId>
    <version>2.5.2</version>
    <executions>
        <execution>
            <id>install-external-base</id>
            <phase>clean</phase>
            <configuration>
                <file>${basedir}/lib/stacs-sdk-1.0.5.jar</file>
                <repositoryLayout>default</repositoryLayout>
                <groupId>stacs-sdk</groupId>
                <artifactId>stacs-sdk</artifactId>
                <version>1.0.5</version>
                <packaging>jar</packaging>
                <generatePom>true</generatePom>
            </configuration>
            <goals>
                <goal>install-file</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
