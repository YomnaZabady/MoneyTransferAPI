package com.transaction.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class TransactionsEngine {

  public static long lastTransactionId = 0;

  public static enum transactionStatusType {
    INQUEUE, FAILED, SUCCESS, CANCELLED;
  }

  public static HashMap<Long, Transaction> transactionsInSystem = new HashMap<Long, Transaction>();
  public static Queue<Transaction> transactionBuffer = new LinkedList<Transaction>();

  public static long insertNewTransaction(long fromAccount, long toAccount, Double value ) {

    lastTransactionId += 1;
    Transaction newTransaction =
        new Transaction(fromAccount, toAccount, value, lastTransactionId, transactionStatusType.INQUEUE);
    transactionsInSystem.put(lastTransactionId, newTransaction);
    transactionBuffer.add(newTransaction);
    return lastTransactionId;
  }

  public static void changeTransactionStatus(long transactionId, transactionStatusType status) {
    Transaction currentTransaction = transactionsInSystem.get(transactionId);
    currentTransaction.transactionStatus = status;
    transactionsInSystem.put(transactionId, currentTransaction);

  }
  
  public static void processTransactions() {
    while (!transactionBuffer.isEmpty()) {
      Transaction currentTransaction = transactionBuffer.poll();
      
      long fromAccId = currentTransaction.getFromAccount();
      long toAccId = currentTransaction.getToAccount();
      
      Double transactionAmount = currentTransaction.getValue();
      Account fromAcc = AccountManagement.accounts.get(fromAccId);
      Double newFromAccAmount = fromAcc.getAmount() - transactionAmount;
      
      if (newFromAccAmount < 0) {
        changeTransactionStatus(currentTransaction.id, transactionStatusType.FAILED);
        continue;
      } 
      
      fromAcc.setAmount(newFromAccAmount);
      
      Account toAcc = AccountManagement.accounts.get(toAccId);
      Double newToAccValue = toAcc.getAmount() + transactionAmount;
      toAcc.setAmount(newToAccValue);
      
      AccountManagement.accounts.put(fromAccId, fromAcc);
      AccountManagement.accounts.put(toAccId, toAcc);
      
      changeTransactionStatus(currentTransaction.id, transactionStatusType.SUCCESS);
      
    }
  }
}
