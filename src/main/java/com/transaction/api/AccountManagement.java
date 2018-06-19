package com.transaction.api;

import java.util.HashMap;

public class AccountManagement {
  
    public static long lastAccountId = 0;
    public static HashMap<Long, Account> accounts = new HashMap<Long, Account>();
    
    public static void createDummyAccounts() {
      Account acc1 = new Account(1, "Yomna", 1000d);
      accounts.put(1l, acc1);
      Account acc2 = new Account(2, "Ahmad", 1000d);
      accounts.put(2l, acc2);
      Account acc3 = new Account(3, "Amr", 1000d);
      accounts.put(3l, acc3);
      lastAccountId = 3;
    }
    
    public long addNewAccount(String name, Double amount) {
      lastAccountId += 1;
      Account acc = new Account(lastAccountId, name, amount);
      accounts.put(lastAccountId, acc);
      return lastAccountId;
    }
}
