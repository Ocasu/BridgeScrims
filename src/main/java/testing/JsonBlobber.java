package testing;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class JsonBlobber {
    public static String g = "de9f408a-e73a-11eb-9ebe-c75b0ae745f3";
    public static void main(String[] args) throws IOException {

//        Scanner scan = new Scanner(new File("src/main/java/testing/y.json"));
//        String data = "";
//        while(scan.hasNext()){
//            data += scan.next();
//        }
//        put(g, data);
        gameCtAdd(g);
    }
    public static void post() throws IOException {
        URL url = new URL("https://jsonblob.com/api/jsonBlob");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{\n  \"Games played\": 12345,\n  \"Customer\": \"John Smith\",\n  \"Quantity\": 1,\n  \"Price\": 10.00\n}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }

    public static void put(String id, String data) throws IOException {
        URL url = new URL("https://jsonblob.com/api/jsonBlob/" + id);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");
        http.setRequestProperty("Content-Type", "application/json");


        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }
    public static void gameCtAdd(String id) throws IOException {
        put(g, ""+(Integer.parseInt(get(g, "gameCt"))+1));
    }
    public static void restartCtAdd(String id) throws IOException {
        put(g, ""+(Integer.parseInt(get(g, "restartCt"))+1));
    }
    public static String get(String id, String field) throws IOException {
        URL url = new URL("https://jsonblob.com/api/jsonBlob/" + id);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestProperty("Authorization", "Bearer mt0dgHmLJMVQhvjpNXDyA83vA_PxH23Y");

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        System.out.println(http.getHeaderFields());
        JSONObject j = new JSONObject(http.getContent());
        http.disconnect();
        System.out.println(j.toMap());
        return j.getInt("gameCt") + "";

    }
}
