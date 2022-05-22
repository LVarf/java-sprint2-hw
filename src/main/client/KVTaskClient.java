package client;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class KVTaskClient {
    private URL url;
    private String API_TOKEN;
    private String key;


    public KVTaskClient(URL url) {
        this.url = url;
        //System.out.println("Inter your key.");
       // key = (new Scanner(System.in)).next();
        try {
            URI newUrl = URI.create(url.toString() + "/register/"+ key);

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(newUrl)
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpResponse<String> response = client.send(request, handler);

            System.out.println("Код ответа при создании объекта: " + response.statusCode());
            API_TOKEN = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getKey() {
        return key;
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI newUrl = URI.create(url.toString() + "/save/" + key + "?" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(newUrl)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Код ответа: " + response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI newUrl = URI.create(url.toString() + "/load/" + key + "?" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(newUrl)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response = client.send(request, handler);

        System.out.println("Код ответа: " + response.statusCode());
        return response.body();
    }
}
