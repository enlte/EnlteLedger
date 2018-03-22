/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enlteledger;

import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import presenter.BroadcastedPost;
import presenter.BroadcastedTransaction;

/**
 *
 * @author new
 */
public class EnlteLedger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException{
        
        //It opens Command prompt.
        Console console = System.console();
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            //Sample.class.getProtectionDomain().getCodeSource().getLocation().toString()
            String filename = EnlteLedger.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            filename = filename.replace("build/classes/", "");
//            if(!filename.contains("dist/EnlteLedger.jar")){
//                 filename = filename + "dist/EnlteLedger.jar";
//            }           
            System.out.println("sample.Sample.main()" + filename);
            // Command to run .jar file in Command prompt.
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
        } else {
            //Sample.main(new String[0]);
            Timer time = new Timer(); // Instantiate Timer Object
            ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
            time.schedule(st, 5000, 50000); // Create Repetitively task for every 1 secs
            // TODO code application logic here
        }

    }

    /**
     * 
     */
    // Create a class extends with TimerTask
    public static class ScheduledTask extends TimerTask {

        Date now; // to display current time
        // Add your task here

        public void run() {
            now = new Date(); // initialize date
            System.out.println("Time is :" + now); // Display current time
            //hitBlockChain();
            //new BroadcastedPost().checkBroadcastedHash();
            new BroadcastedTransaction().checkBroadcastedHash();
        }
    }

    /**
     * It get latest hash from api and update on console
     */ 
    private static void hitBlockChain() {
        try {
            //System.out.println("hitBlockChain:");
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://enlte.com/blockchain/broadcast_client_hash");
            //System.out.println("http://enlte.com/blockchain/broadcast_client_hash:");
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
            System.out.println("response:-" + result);
            //handleResult(result);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(EnlteLedger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnlteLedger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
