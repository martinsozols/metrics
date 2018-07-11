import com.eclipsesource.json.JsonObject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import spark.Spark;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static spark.Spark.*;

public class Metrics {
    private static StatisticsHandler statisticsHandler;

    public static void main(String[] args) throws  Exception{
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, new EmbeddedJettyFactory((i, j, k) -> {
            Server server =  new Server();
            statisticsHandler = new StatisticsHandler();
            statisticsHandler.setServer(server);
            return server;
        }));
        Spark.staticFileLocation("/public");
        port(8091);
        routes();
        statisticsHandler.statsReset();
        statisticsHandler.start();
    }

    private static void routes() throws IOException {
        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "http://localhost");
            response.header("Access-Control-Request-Method", "GET,PUT,POST,DELETE,PATCH,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
            response.header("Access-Control-Allow-Credentials", "true");
        });

        get("/getContent", ((request, response) -> {
            JsonObject json = new JsonObject();

            json.add("message", "SUCCESS")
                    .add("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:dd.SSSSSS'Z'").format(new Date()))
                    .add("url","/getContent");
            Elastic.sendPOST("http://hauser.corp.tele2.com:9200/ugis/_doc/", json.toString());
//            Thread.sleep(500);
            return "SUCCESS";
        }));

    }
}
