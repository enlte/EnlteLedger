/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

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
            System.out.println("http://enlte.com/transaction_blockchain/broadcast_client_hash");
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

            System.out.println("response:-" + result.trim());

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BroadcastedTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
