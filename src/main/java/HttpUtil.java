import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {

    public static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    String maxResultEnd;


    public static User sendPost(URI uri, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }


    public static User sendPut(URI uri, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return GSON.fromJson(response.body(), User.class);
    }

    public static void sendDelet(URI uri, User user) throws InterruptedException, IOException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .header("Content-type", "application/json")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Body = " + response.body());
        System.out.println("Status = " + response.statusCode());
    }

    public static void sendGet(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Header = " + response.headers());
        System.out.println("Body = " + response.body());
        System.out.println("Status = " + response.statusCode());

    }


    public static User sendGetById(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            User user = GSON.fromJson(response.body(), User.class);
            return user;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

    }

    public static User sendGetByName(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            List<User> user = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
            }.getType());
            return user.get(0);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public static List<Comments> getComments(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Comments> comments = GSON.fromJson(response.body(), new TypeToken<List<Comments>>() {
        }.getType());
        return comments;
    }


    public void getCommentsByPost(URI uri, String userId) throws IOException, InterruptedException {
        final List<Comments> comments = getComments(uri);
        Gson gsonMapper = new GsonBuilder().setPrettyPrinting().create();
        String gson = gsonMapper.toJson(comments);
        System.out.println("gson = " + gson);
        ObjectMapper mapper = new ObjectMapper();
        String fileName = "user-" + userId + "-post-" + maxResultEnd + "-comments.json";
        mapper.writeValue(new File(fileName), gson);
    }

    public String maxResult(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Posts> posts = GSON.fromJson(response.body(), new TypeToken<List<Posts>>() {
        }.getType());
        Integer max = posts.stream()
                .max(Comparator.comparing(Posts::getId))
                .map(posts1 -> posts1.getId())
                .get();
        String maxResult = String.valueOf(max);
        maxResultEnd = maxResult;
        return maxResult;
    }

    public static List<String> getToDos(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        List<Todos> todos = GSON.fromJson(response.body(), new TypeToken<List<Todos>>() {
        }.getType());
        final List<String> collect = todos.stream()
                .filter(todos1 -> !todos1.getCompleted())
                .map(todos1 -> todos1.getTitle())
                .collect(Collectors.toList());
        return collect;
    }
}
