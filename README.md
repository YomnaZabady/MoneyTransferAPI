#Money Transaction API

- In order to test the transfering of money you need to do the following:

1- call the api request to create dummy accounts to play with in the system

2- call the api request to transfer money between accounts, specify the fromAccount id , toAccount id and the value to be transferred
Note that this call doesn't process yet the transaction but add into the transactionsBuffer to be then processed upon request

3- check the transaction been created in the system by calling the api request to view the transactions in the system

4- call the api request to process the transactions in the buffer

5- call the transaction info request once again to check the transaction statuses after exceution

6- call the accounts info api request to check the accounts after the transactions made

7- you can check a specific acount through the following api call

-----------------------------------------

Notes about the Design:


