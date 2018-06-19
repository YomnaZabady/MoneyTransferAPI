# Money Transaction API

## In order to test the transfering of money you need to do the following:


1- Call the api request to create dummy accounts to play with in the system. http://localhost:8080/MoneyTransfer/rest/api/createAccounts

2- Call the api request to transfer money between accounts, specify the fromAccount id , toAccount id and the value to be transferred
Note that this call doesn't process yet the transaction but add into the transactionsBuffer to be then processed upon request

http://localhost:8080/MoneyTransfer/rest/api/transferMoneybetAccs
- fromAcc: 1
- toAcc: 2
- value: 200

3- Check the transaction been created in the system by calling the api request to view the transactions in the system
http://localhost:8080/MoneyTransfer/rest/api/transactionInfo

4- Call the api request to process the transactions in the buffer
http://localhost:8080/MoneyTransfer/rest/api/processTransactions

5- Call the transaction info request once again to check the transaction statuses after exceution
http://localhost:8080/MoneyTransfer/rest/api/transactionInfo

6- Call the accounts info api request to check the accounts after the transactions made
http://localhost:8080/MoneyTransfer/rest/api/accountsInfo

7- You can check a specific acount through the following api call
http://localhost:8080/MoneyTransfer/rest/api/getAccountInfo?accountID=1

-----------------------------------------

## Notes about the Design:

When adding a new transaction it is added to the transaction buffer with a status of INQUEUE
Only when we call processTransactions, the Transaction buffer queue is executed till its emptied
And according to the processing of each transaction, each transaction state will be changed to either SUCCESS or FAILED.

The in memory DB for accounts is in Account Management, it is HashMap called accounts
The in memory DB for transactions is in Transaction Management, It is a Hashmap called transactionsInSystem


