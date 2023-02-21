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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.json.JSONArray;

public class RapidproClient {

    public static void main( String[] args ) throws SchedulerException
    {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever())
                .build();

        JobDetail job = JobBuilder.newJob(HttpClientJob.class)
                .withIdentity("job1", "group1")
                .build();   

        // Schedule the job to run at the specified times             
        scheduler.scheduleJob(job, trigger);

        // Start the scheduler
        scheduler.start();   
    }

    public static class HttpClientJob implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
        processJson(responseBody);
    }
     private static void processJson(String bodyResposne)throws URISyntaxException, IOException, InterruptedException{
          List<Integer> idList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(bodyResposne);
          JSONArray resultsArray = jsonObject.getJSONArray("results");
           for (int i = 0; i < 100 && i < resultsArray.length(); i++) {
            
             JSONObject message = resultsArray.getJSONObject(i);
             if(message.get("direction").equals("in")){
                idList.add(message.getInt("id"));
             }}
            int[] idArray = idList.stream().mapToInt(Integer::intValue).toArray();

         JSONArray jsonArray = new JSONArray(Arrays.toString(idArray));
         
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("messages", jsonArray);
        jsonObject2.put("action", "restore");

         String jsonString = jsonObject2.toString();

        HttpClient client2 = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(20))
        .build();
        HttpRequest request2 = HttpRequest.newBuilder()
        .version(HttpClient.Version.HTTP_2)
           .uri(new URI("https://textit.com/api/v2/message_actions.json"))
           .headers("Authorization", "Token f5e24f098f922f7fa7675b471d569ab0c74c9271")
           .header("Content-Type", "application/json")
           .POST(HttpRequest.BodyPublishers.ofString(jsonString, StandardCharsets.UTF_8))
           .build();
           HttpResponse<String> response2 = client2.send(request2, BodyHandlers.ofString());
            String responseBody2 = response2.body();
            System.out.println("httpGetRequest: " + responseBody2);
          }  
  }















