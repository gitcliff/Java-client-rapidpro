package itech.module;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.json.JSONObject;
import org.json.JSONArray;

public class RapidproClient {

    public static void main( String[] args )
    {
        System.gc();
        try {
            httpGetRequest();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void httpGetRequest() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(20))
        .build();
        HttpRequest request = HttpRequest.newBuilder()
         .version(HttpClient.Version.HTTP_2)
            .uri(new URI("https://textit.com/api/v2/messages.json"))
            .headers("Authorization", "Token f5e24f098f922f7fa7675b471d569ab0c74c9271")
            .GET()
            .build();
         HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String responseBody = response.body();
            //  System.out.println("httpGetRequest: " + responseBody);
            processJson(responseBody);
    }
     private static void processJson(String bodyResposne)throws URISyntaxException, IOException, InterruptedException{
           JSONObject jsonObject = new JSONObject(bodyResposne);
          JSONArray resultsArray = jsonObject.getJSONArray("results");
          for (int i = 0; i < 100 && i < resultsArray.length(); i++) {
            JSONObject message = resultsArray.getJSONObject(i);
            int message_id = message.getInt("id");
            int[] arr = new int[message_id];

            JSONArray jsonArray = new JSONArray(arr);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject.put("numbers", jsonArray);
            String jsonString = jsonObject2.toString();

            System.out.println(jsonString);

            HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();
            HttpRequest request2 = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_2)
               .uri(new URI("https://textit.com/api/v2/message_actions.json"))
               .headers("Authorization", "Token f5e24f098f922f7fa7675b471d569ab0c74c9271")
               .POST(HttpRequest.BodyPublishers.ofString("{\"messages\":\"jsonArray\",\"action\":\"delete\"}", StandardCharsets.UTF_8))
               .build();
               HttpResponse<String> response2 = client.send(request2, BodyHandlers.ofString());
               String responseBody2 = response2.body();
                 System.out.println("httpGetRequest: " + responseBody2);
          }
        }
}















