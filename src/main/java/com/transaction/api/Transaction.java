package com.transaction.api;

import java.time.Instant;

public class Transaction {

    public long epochTime;
    public long id;
    private long fromAccount;
    private long toAccount;
    private Double value;
    public TransactionsEngine.transactionStatusType transactionStatus;
    
    public Transaction(
         long fromAccount,
         long toAccount,
         Double value,
         long id,
         TransactionsEngine.transactionStatusType transactionStatus
        ) {
      
      this.epochTime = Instant.now().toEpochMilli();
      this.setFromAccount(fromAccount);
      this.setToAccount(toAccount);
      this.setValue(value);
      this.id = id;
      this.transactionStatus = transactionStatus;
    }
    
    

  public long getToAccount() {
    return toAccount;
  }

  public void setToAccount(long toAccount) {
    this.toAccount = toAccount;
  }

  public long getFromAccount() {
    return fromAccount;
  }

  public void setFromAccount(long fromAccount) {
    this.fromAccount = fromAccount;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }
    
}
