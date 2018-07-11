import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.net.URLEncoder;

public class Elastic {

    public static void send(String url, JsonObject data) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity("&document=" + URLEncoder.encode(data.toString(), "UTF-8"), ContentType.APPLICATION_FORM_URLENCODED);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
//            request.setHeader("Authorization", auth);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
