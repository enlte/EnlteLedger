/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

import enlteledger.EnlteLedger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class BroadcastedTransaction {

    public BroadcastedTransaction() {

    }

    public void checkBroadcastedHash() {
        //To change body of generated methods, choose Tools | Templates.
        try {
            //System.out.println("Checking for new updates... ");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://enlte.com/transaction_blockchain/broadcast_client_hash");
            //System.out.println("http://enlte.com/transaction_blockchain/broadcast_client_hash");
            System.out.println("Loading ongoing transactions...");
            JSONObject json = new JSONObject();
            StringEntity input = new StringEntity(json.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);

            HttpResponse response = httpClient.execute(postRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            String result = EntityUtils.toString(response.getEntity());

            handleResult(result);
            //System.out.println("response:-" + result.trim());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleResult(String result) {
        if (result != null && result.length() > 0) {

            try {
                //JSONObject comparedJson = new JSONObject();
                JSONArray jSONArray = new JSONArray(result);
                if (jSONArray != null && jSONArray.length() > 0) {
                    //jSONObject = jSONArray.getJSONObject(0);                        
                    processJsonArry(jSONArray);
                    //JSONArray jarray = jSONArray.getJSONArray(jSONArray.length() - 1);
                    //jSONObject = jarray.getJSONObject(0);
                }
            } catch (JSONException e) {
                //Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, e);
                try {
                    JSONObject jSONObject = new JSONObject(result);
                    processJson(jSONObject);
                } catch (JSONException ex) {
                    Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    private void processJsonArry(JSONArray jsonArray) {

        //boolean islastItem = false;
        for (int i = 0; i < jsonArray.length(); i++) {

            if (i == 0) {
                try {
                    processJson(jsonArray.getJSONObject(0));
                } catch (JSONException e) {
                    //e.printStackTrace();
                    try {
                        JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                        processJson(jsonArray2.getJSONObject(0));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                try {
                    JSONArray jsonArray2 = jsonArray.getJSONArray(i);
                    processJson(jsonArray2.getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void processJson(JSONObject jsonObject) {

        try {
            if (jsonObject.has("data_hash")) {
                String dHash = jsonObject.getString("data_hash");
                System.out.println("Data hash: "+dHash);
            }
        } catch (JSONException e) {
            Logger.getLogger(EnlteLedger.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
