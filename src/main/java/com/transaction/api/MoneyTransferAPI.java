package com.transaction.api;

import java.util.ArrayList;
import java.util.Collection;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("api")
public class MoneyTransferAPI {
	
    /**
     * API function for creation of Dummy Accounts 
     * for the use of simulating the money transfer
     * @return a json list of the accounts created
     */
    @POST
    @Path("/createAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAndGetAccounts() {
      AccountManagement.createDummyAccounts();
      Collection<Account> values = AccountManagement.accounts.values();
      Account[] accounts = values.toArray(new Account[values.size()]);
      return Response.ok(accounts).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
    
    /**
     * A function to help view the accounts in the memory
     * @return a map of all the accounts in the memory
     */
	@GET
    @Path("/accountsInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts() {
        return Response.ok(AccountManagement.accounts).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
	
	/**
	 * API function for getting certain account information
	 * @param accId the account id
	 * @return json object of the details of the account
	 */
	@GET
    @Path("/getAccountInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountInfo(@QueryParam("accountID") long accId) {
        return Response.ok(AccountManagement.accounts.get(accId)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
	
	/**
	 * API Function to request money transfer (transaction) between accounts
	 * It is worth noting that once the transaction is created it is not yet processed
	 * @param fromAcc the account id from which we are going to transfer the money
	 * @param toAcc the account id to which the money will be transferred
	 * @param value the amount to be transferred
	 * @return the transaction information that has been created based on this request
	 */
	@Path("transferMoneybetAccs")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response transactionRequest(
	    @FormParam("fromAcc") long fromAcc, 
	    @FormParam("toAcc") long toAcc, 
	    @FormParam("value") Double value) {
	  if (AccountManagement.accounts.get(fromAcc) != null && 
	      AccountManagement.accounts.get(toAcc) != null) {
	    // check if the from account has enough value to transfer
	    if (AccountManagement.accounts.get(fromAcc).getAmount() < value) {
	      return Response.status(Response.Status.BAD_REQUEST).build();
	    } else {
	      TransactionsEngine.insertNewTransaction(fromAcc, toAcc, value);
	      return Response.ok(
	          TransactionsEngine.transactionsInSystem.get(
	              TransactionsEngine.lastTransactionId
	              )
	          ).build();
	    }
	  } else {
	    return Response.status(Response.Status.NO_CONTENT).build();
	  }
	}
	
	/**
	 * API function to process the transaction in the system buffer -all INQUEUE transactions-
	 * @return a list of the transactions in the system with their processing status
	 */
	@GET
    @Path("/processTransactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processTransactions() {
        TransactionsEngine.processTransactions();
        Collection<Transaction> values = TransactionsEngine.transactionsInSystem.values();
        Transaction[] transactions = values.toArray(new Transaction[values.size()]);
        return Response.ok(transactions).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
	
	/**
	 * API function to view all transactions made in the system
	 * @return json of all the transactions created in the system
	 */
	@GET
    @Path("/transactionInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processAndGetTransactions() {
        return Response.ok(TransactionsEngine.transactionsInSystem).type(MediaType.APPLICATION_JSON_TYPE).build();
        
    }
	
	
}
