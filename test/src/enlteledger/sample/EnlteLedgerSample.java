/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enlteledger.sample;

import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
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

/**
 *
 * @author sample app to view ongoing trasaction
 */
public class EnlteLedgerSample {

    static String previousHash = "";
    static long index = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        //It opens Command prompt.
        Console console = System.console();
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            //Sample.class.getProtectionDomain().getCodeSource().getLocation().toString()
            String filename = EnlteLedgerSample.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
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
            time.schedule(st, 0, 7000); // Create Repetitively task for every 1 secs
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
        int counter = 0;

        public void run() {
            now = new Date(); // initialize date
            System.out.println("Time is :" + now); // Display current time
            counter++;
            hitBlockChain();
            if (counter == 10) {
                System.out.println("Program is closed. This is sample project.");
                this.cancel();
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter any two character to continue.");
                String userId = scanner.next();
                if (userId != null && userId.length() > 1) {
                    counter = 0;
                    Timer time = new Timer(); // Instantiate Timer Object
                    ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
                    time.schedule(st, 0, 7000);
                }
            }
        }
    }

    /**
     * It get latest hash from api and update on console
     */
    private static void hitBlockChain() {
        try {
            //Create new block
            String postText = "This information is related to post";
            String timestamp = System.currentTimeMillis() + "";

            if (previousHash.equals("")) {
                previousHash = sha256Hex("this is the genesis block");
            }
            String currentHash = sha256Hex(timestamp + "-" + postText + "-" + previousHash);

            JSONObject jsono = new JSONObject();
            index++;
            jsono.put("index", index);
            jsono.put("previous_hash", previousHash);
            jsono.put("data_hash", currentHash);
            jsono.put("time_stamp", timestamp);

            System.out.println("New hash generated:-" + jsono);

            System.out.println("Broadcast this hash to other nodes for validating the data.");
            // this is offline sample. so we are saving it to local path
            String home = System.getProperty("user.home");
            File file = new File(home + "/Downloads/" + "enlte-db.txt");
            writeToFile(jsono.toString(), file);
            previousHash = currentHash;
            //handleResult(result);
        } catch (Exception ex) {
            Logger.getLogger(EnlteLedgerSample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * this method is used to store new broadcasted hash from server.
     *
     * @param content
     * @param result
     */
    private static void writeToFile(String content, File filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            //String content = "Enlte\n";
            bw.append(content);
            // no need to close it.
            //bw.close();
            System.out.println("File updated.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sha256Hex(String data) {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            return bytesToHex(hash); // make it printable
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static String bytesToHex(byte[] hash) {
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : hash) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2) {
                h = "0" + h;
            }
            hexString.append(h);
        }
        return hexString.toString();
        //return DatatypeConverter.printHexBinary(hash);
    }
}
