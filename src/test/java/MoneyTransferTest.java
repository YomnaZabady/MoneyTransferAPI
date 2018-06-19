import static com.jayway.restassured.RestAssured.given;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.transaction.api.Account;
import com.transaction.api.Transaction;
import com.transaction.api.TransactionsEngine;
import com.transaction.api.TransactionsEngine.transactionStatusType;
import junit.framework.Assert;

public class MoneyTransferTest {

  /**
   * Tests the API call of the creation of Dummy Accounts that we will use next
   * @throws URISyntaxException
   * @throws JSONException
   */
  @Test
  public void testCreateDummyAccounts() throws URISyntaxException, JSONException{
    HashMap<Long, Account> accounts = new HashMap<Long, Account>();
    Response response = given().accept(ContentType.JSON).when()
        .post(new URI("http://localhost:8080/MoneyTransfer/rest/api/createAccounts"));
    
    JSONArray JSONResponseBody = new JSONArray(response.body().asString());
    System.out.println(response.body().asString());
    for (int i = 0; i < JSONResponseBody.length(); i ++) {
      JSONObject currentObj = JSONResponseBody.getJSONObject(i);
      
      long id = currentObj.getLong("id");
      String name = currentObj.getString("name");
      Double amount = currentObj.getDouble("amount");
      
      Account acc = new Account(id, name, amount);
      accounts.put(id, acc);
    }
    Assert.assertEquals(3, accounts.size());
    Assert.assertEquals(1000d, accounts.get(1l).getAmount());
    Assert.assertEquals(1000d, accounts.get(2l).getAmount());
    Assert.assertEquals(1000d, accounts.get(3l).getAmount());
    
  }
  
  /**
   * Tests the API request of transferring money between accounts
   * This api request creates a transaction in the system
   * in which we asserts its creation
   * @throws URISyntaxException
   */
  @Test
  public void testCreateNewTransactionRequest() throws URISyntaxException {
      
      Response response = given().
              accept(ContentType.JSON).
              contentType(ContentType.JSON).
              formParam("fromAcc", 1l).
              formParam("toAcc", 2l).
              formParam("value", 300).
      when().
              post(new URI("http://localhost:8080/MoneyTransfer/rest/api/transferMoneybetAccs")).
      then().
              statusCode(200).extract().response();

      JsonPath jp = response.jsonPath();
      System.out.println(response.asString());
      Assert.assertEquals(1l, jp.getLong("fromAccount"));
      Assert.assertEquals(2l, jp.getLong("toAccount"));
      Assert.assertEquals(300d, jp.getDouble("value"));
      Assert.assertEquals("INQUEUE", jp.getString("transactionStatus"));
      
  }
  
  /**
   * Testing the call of the transactions processing, for all the previous transactions 
   * created in our system buffer, we test their successful execution for the correct ones
   * and the fail execution in the wrong one
   * we check that through the transaction status
   * then we check the balance the balance in each account
   * @throws URISyntaxException
   * @throws JSONException
   */
  @Test
  public void testprocessTransactionRequest() throws URISyntaxException, JSONException {
      
      given().
          accept(ContentType.JSON).
          contentType(ContentType.JSON).
          formParam("fromAcc", 1l).
          formParam("toAcc", 2l).
          formParam("value", 800).
        when().
            post(new URI("http://localhost:8080/MoneyTransfer/rest/api/transferMoneybetAccs")).
        then().
          statusCode(200).extract().response();
      
    given().accept(ContentType.JSON).contentType(ContentType.JSON).
        formParam("fromAcc", 1l).
        formParam("toAcc", 3l).
        formParam("value", 1800).
        when().
          post(new URI("http://localhost:8080/MoneyTransfer/rest/api/transferMoneybetAccs")).
        then().
          statusCode(400).extract().response();
      
      Response response = given().
              accept(ContentType.JSON).
              contentType(ContentType.JSON).
      when().
              get(new URI("http://localhost:8080/MoneyTransfer/rest/api/processTransactions")).
      then().
              statusCode(200).extract().response();
      
      HashMap<Long, Transaction> transactions = new HashMap<Long, Transaction> ();
      JSONArray JSONResponseBody = new JSONArray(response.body().asString());
      System.out.println(response.body().asString());
      for (int i = 0; i < JSONResponseBody.length(); i ++) {
        JSONObject currentObj = JSONResponseBody.getJSONObject(i);
        long id = currentObj.getLong("id");
        Double value = currentObj.getDouble("value");
        Long fromAccount = currentObj.getLong("fromAccount");
        Long toAccount = currentObj.getLong("toAccount");
        TransactionsEngine.transactionStatusType transStatus = getTransStatus(currentObj.getString("transactionStatus"));
        
        Transaction trans =  new Transaction(fromAccount, toAccount, value, TransactionsEngine.lastTransactionId, transStatus);
        transactions.put(id, trans);
      }
      
      
      Assert.assertEquals(2, transactions.size());
      
      Assert.assertEquals(transactionStatusType.SUCCESS, transactions.get(1l).transactionStatus);
      
      Response acc1Response = given().accept(ContentType.JSON).contentType(ContentType.JSON).
      when().
        get(new URI("http://localhost:8080/MoneyTransfer/rest/api/getAccountInfo?accountID=1")).
      then().
        statusCode(200).extract().response();
      JSONObject json = new JSONObject(acc1Response.asString());
      Assert.assertEquals(700d,json.getDouble("amount"));

      Response acc2Response = given().accept(ContentType.JSON).contentType(ContentType.JSON).
          when().
            get(new URI("http://localhost:8080/MoneyTransfer/rest/api/getAccountInfo?accountID=2")).
          then().
            statusCode(200).extract().response();
      JSONObject json2 = new JSONObject(acc2Response.asString());
      Assert.assertEquals(1300d,json2.getDouble("amount"));
      
      Assert.assertEquals(transactionStatusType.FAILED, transactions.get(2l).transactionStatus);

  }
  
  private TransactionsEngine.transactionStatusType getTransStatus(String value) {
    if (value.equals("INQUEUE")) {
      return transactionStatusType.INQUEUE;
    } else if (value.equals("SUCCESS")) {
      return transactionStatusType.SUCCESS;
    } else if (value.equals("FAILED")) {
      return transactionStatusType.FAILED;
    } else {
      return transactionStatusType.CANCELLED;
    }
  }
}
