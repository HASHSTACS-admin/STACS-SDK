# STACS SDK

The STACS Protocol is a STACS Permissioned Blockchain Network built by Hashstacs that exposes REST API endpoints for applications to connect and interact with the underlying distributed ledger. This SDK provides all essential tools for communicating with a node residing in a STACS network including signing of transactions and encryption in transit. 

The STACS Java SDK enables applications to sit on top of a STACS Native Permissioned Network and run business processes related to digital assets and tokenized securities by calling the relevant smart contracts encapsulated in the SDK library. 

Reach out to the Solutions team at Hashstacs for questions! support@stacs.io

## 1. SDK Key Developer Features
1. Invoke Smart Contract Function calls directly to the STACS network 
2. SDK function calls have built-in signatures for sending requests with your private key
3. SDK method calls send transactions to the network by default with encryption in transit
4. Transaction requests that modify the ledger are sent as Asynchronous REST calls and separate queries are provided to verify state changes in the ledger
5. Queries of the state of the ledger are provided as Synchronous REST calls that return data in JSON format
6. SDK provides both the raw returned JSON strings and Java Data Transfer Objects (DTOs) that are mapped to the JSON strings   

## 2. Available Smart Contracts
These are smart contracts that are provided out-of-box with a standard STACS protocol deployment.

This SDK provides both the DTO Object (a request Business Object, `reqBO`) containing parameters required for sending the API request as well as the actual method call to that fires off the REST request to the blockchain node.  

The REST API response is captured as a Response DTO Object, respBO where the raw JSON string is stored while processing is done by the SDK to extract the JSON content into the Response object itself.

The method calls have been divided into chain-related calls and wallet-related calls. 

### 2.1. Chain-related Methods

While sample codes have been provided to indicate the required parameters of the DTO object, examples of the actual execution for each smart contract listed below is also provided in the `ChainSampleUsage()` function in the `SampleUsage.java` file. 

#### 2.1.1 Asynchronous Method Calls to modify the ledger 
These calls sends transaction requests to modify the ledger (such as issuing a token) and each request has a transaction id that can be used in queries to check if the STACS network has accepted the transaction requests. 

##### 2.1.1.1 Issue a Digital Asset (token) to the chain
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`IssueTokenReqBO` | `ChainConnector.issueToken` | `IssueTokenSample.java` 

##### 2.1.1.2 Grant KYC subscription permission to a user wallet account 
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`GrantSubscribePermReqBO` | `ChainConnector.grantSubscriptionPermission` | `GrantSubscriptionPermissionSample.java`

##### 2.1.1.3 Investor with valid KYC profile to subscribe to a digital asset 
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`SubscribeReqBO` |  `ChainConnector.subscribeToToken` | `SubscriptionSample.java`

##### 2.1.1.4 Create a payment record for distributing payments (i.e. coupons, dividends) to a digital asset 
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`GeneratePaymentRecordReqBO`| `ChainConnector.generatePaymentRecord` | N.A.

##### 2.1.1.5 Distribute payments to the right investors 
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`DistributePaymentReqBO` | `ChainConnector.distributePayment` | `DistributePaymentsSample.java`

##### 2.1.1.6 Freeze (and unfreeze) wallet accounts
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`FreezeWalletReqBO` | `ChainConnector.freezeOrUnfreezeWallet` | N.A.
 
#### 2.1.2 Synchronous Method Calls to verify the ledger state (sent by the Asynchronous method calls above)
These calls sends the transaction ids of their respective asynchronous method calls to the blockchain and receives a Response Object containing a JSON string. The Java DTO Response Object provided in this SDK provides both the raw JSON string as well as the extracted data within the same object. 
 
##### 2.1.2.1 Query status of a digital asset (token) issuance
Java DTO Response Object | Method Call 
------------------------ | -----------
`IssueTokenStatusRespBO` | `ChainConnector.getIssueTokenStatus`
 
##### 2.1.2.2 Query status of a KYC subscription permission grant 
Java DTO Response Object | Method Call 
------------------------ | -----------
`GrantSubscribePermStatusRespBO` | `ChainConnector.getGrantSubscriptionPermissionStatus`

##### 2.1.2.3 Query status of a investor subscription 
Java DTO Response Object | Method Call 
------------------------ | -----------
`SubscribeStatusRespBO` | `ChainConnector.getSubscriptionStatus`

##### 2.1.2.4 Query status of a payment record
Java DTO Response Object | Method Call 
------------------------ | -----------
`GeneratePaymentRecordRespBO` | `ChainConnector.getPaymentRecordStatus`

##### 2.1.2.5 Query status of a payment distribution (i.e. coupons, dividends) 
Java DTO Response Object | Method Call 
------------------------ | -----------
`DistributePaymentStatusRespBO` | `ChainConnector.getDistributePaymentStatus`

##### 2.1.2.6 Query status of a freeze and unfreeze action on a wallet account
Java DTO Response Object | Method Call 
------------------------ | -----------
`FreezeOrUnfreezeStatusRespBO` | `ChainConnector.getFreezeOrUnfreezeWalletStatus`
 
#### 2.1.3 Synchronous Method Calls to query the ledger
These calls does a simple check of the ledger state with input parameters wrapped in a DTO Request object and returns a DTO response object.

##### 2.1.3.1 Get latest block height
Java DTO Request Object | Java DTO Response Object | Method Call 
----------------------- | ------------------------ | ------------
N.A. | `LatestBlockRespBO` | `ChainConnector.getLatestBlockHeight` 

##### 2.1.3.2 Retrieve all transactions for a specified length of blocks 
Java DTO Request Object | Java DTO Response Object | Method Call 
----------------------- | ------------------------ | ------------
`TxHistoryBetweenBlocksReqBO` | `TxHistoryRespBO`  | `ChainConnector.getTxsBetweenBlocks`
 
### 2.2 Wallet-related Methods 

While sample codes have been provided to indicate the required parameters of the DTO object, examples of the actual execution for each smart contract listed below is also provided in the `WalletSampleUsage()` function in the `SampleUsage.java` file. 

#### 2.2.1 Asynchronous Method Calls to modify the ledger  
These calls sends transaction requests to modify the ledger (such as transferring a token) and each request has a transaction id that can be used in queries to check if the STACS network has accepted the transaction requests. 

##### 2.2.1.1 Transfer tokens from 1 wallet to another
Java DTO Request Object | Method Call | Sample Code  
----------------------  | ----------- | -----------  
`TransferTokenReqBO` | `WalletConnector.transferToken` | `TransferTokenSample.java` 
 
#### 2.2.2 Synchronous Method Calls to verify the ledger state (sent by the Asynchronous method calls above)
These calls sends the transaction ids of their respective asynchronous method calls to the blockchain and receives a Response Object containing a JSON string. The Java DTO Response Object provided in this SDK provides both the raw JSON string as well as the extracted data within the same object. 

##### 2.2.2.1 Query status of a token transfer
Java DTO Response Object | Method Call 
------------------------ | -----------
`TransferTokenStatusRespBO` | `WalletConnector.getTransferTokenStatus`

#### 2.2.3 Synchronous Method Calls to query the ledger
These calls does a simple check of the ledger state with input parameters wrapped in a DTO Request object and returns a DTO response object.  

##### 2.2.3.1 Query wallet balance
Java DTO Request Object | Java DTO Response Object | Method Call 
----------------------- | ------------------------ | -----------
`WalletBalanceReqBO` | `WalletBalanceRespBO` | `WalletConnector.getWalletBalance` 

##### 2.2.3.2 Query the full transaction history of a token or wallet 
Java DTO Request Object | Java DTO Response Object | Method Call 
----------------------- | ------------------------ | -----------
`TxHistoryReqBO` |` TxHistoryRespBO` | `WalletConnector.getTxHistoryForWalletAndOrCurrency` 

### Not covered in the SDK 
Keys are managed independently in this SDK and the encryption used is ECDSA. 
This SDK does not include deployment of nodes for a STACS network.
This SDK version also does not cover the deployment of new or updated smart contracts to a STACS network.  

## 3. Pre-requisites

Required minimum versions:
* JDK 8
* Apache Maven 3.6


Request from the STACS node owner the following information:
1. Node Public Key
2. Node Merchant Id
3. Node AES Key
4. Node Gateway URL 
You will also have to generate a keypair and will have to submit the public key to the STACS node owner.
The private key generated will have to be entered as the `node_prikey` field in the configuration file.

To generate a keypair, run the `StacsUtil.createWallet()` call. See `SampleUsage.java` for this function. 

These parameters will be required to be entered into the configuration file, `config.properties`.

### 3.1 Getting Started

Detailed examples for each smart contract function is provided in the `SampleUsage.java` main file and can be run once the `config.properties` configuration file has been updated with the pre-requisite information. 

To begin, ensure you have Maven installed and run the Maven command:
```
mvn clean compile install
```

#### 3.2 Simple Example - Issuing a token on the STACS network 

A simple example for issuing a token can be found in the `ChainSampleUsage()` method in the `SampleUsage.java` main file.

Comments are provided for each sample use that explain how the process is completed. The sample code for issuing a token is as follows below:

```
/**
 *Issuing a token on a STACS Native network with the request object @IssueTokenReqBO and 
 *receive the asynchronous response object @AsyncRespBO 
 *
 *Verify the request status with the @transaction_id as part of the request object and
 *receive the response object @IssueTokenStatusRespBO
 */
IssueTokenSample issueTokenInfo = new IssueTokenSample(_sponsorWalletAddress,_tokenCustodyAddress,SAMPLE_TOKEN);
IssueTokenReqBO issueTokenRequest = issueTokenInfo.getIssueRequest();
AsyncRespBO issueTokenResponse = _chainConn.issueToken(issueTokenRequest, _sponsorSignKey);
log.debug("Issued Token Request Transaction Id: " + issueTokenResponse.get_txId());
Thread.sleep(StacsUtil.POLL_WAIT_TIME_IN_MS);
IssueTokenStatusRespBO issueTokenStatus = _chainConn.getIssueTokenStatus(issueTokenResponse.get_txId());

```

### 3.2 Including this SDK into your own Java projects
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
