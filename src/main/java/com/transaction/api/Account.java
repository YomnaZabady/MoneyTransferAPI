package com.transaction.api;

public class Account {
    public long id;
    private String name;
    private Double amount;
    
    public Account(long id, String name, Double amount) {
      this.id = id;
      setName(name);
      setAmount(amount);
    }
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    
    public Double getAmount() {
      return amount;
    }
    public void setAmount(Double amount) {
      this.amount = amount;
    }
}
