import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Elastic {

    public static void send(String url, JsonObject data) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity("&document=" + URLEncoder.encode(data.toString(), "UTF-8"), ContentType.APPLICATION_FORM_URLENCODED);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPOST(String endpoint, String body) {
        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
            InputStream is;
            try {
                is = connection.getInputStream();
            } catch (Exception e ) {
                is = connection.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String decodedString;
            while ((decodedString = br.readLine()) != null) {
                sb.append(decodedString);
            }

        } catch (java.io.IOException e) {
        }
    }
}
