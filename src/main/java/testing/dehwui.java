package testing;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class dehwui {
    public static void main(String[] args) throws IOException {
        for(int i = 0;i<4;i++){
            add(("participant" + (i)), i+1);
            add(("participant" + (i)), i+1);
        }
    }
    public static void add(String name, int seed) throws IOException {
        URL url = new URL("https://api.challonge.com/v1/tournaments/10037732/participants.json");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Authorization", "Basic T2Nhc3U6NDVGRWMzNWlMdnl1ajNRQlhDanR1NHB0QW5Ub2IwWlNOV1o1TGR0Ng==");
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{\n  \"api_key\": \"45FEc35iLvyuj3QBXCjtu4ptAnTob0ZSNWZ5Ldt6\", \n  \"name\": \""+name+"\",\n  \"seed\": "+seed+"\n}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();


    }
}
