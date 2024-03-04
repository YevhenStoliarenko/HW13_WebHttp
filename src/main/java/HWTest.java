import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HWTest {

    public static User createUser() {
        Geo geo = new Geo(65.666, 67.777);
        Address address = new Address("Zollner", "Aprt 128", "Bamberg", "9576", geo);
        Company company = new Company("Arch", "qui cherca, trova", "noch mal");

        User user1 = new User();
        user1.setId(122);
        user1.setName("Eugen");
        user1.setUsername("EU");
        user1.setEmail("EU@gmail.com");
        user1.setAddress(address);
        user1.setPhone("868-98789");
        user1.setWebsite("archeodigital.com");
        user1.setCompany(company);
        return user1;

    }

    public static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    public static final String BASE_URL_USERS_POST = "https://jsonplaceholder.typicode.com/users";
    public static final String BASE_URL_USERS_POST_COMMENTS = "https://jsonplaceholder.typicode.com/posts/";
    public static final String BASE_URL_TODOS = "https://jsonplaceholder.typicode.com/users";


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        final User user = HWTest.createUser();
        //Exercise 1.1
        final User createdUser = HttpUtil.sendPost(URI.create(BASE_URL + "/users"), user);
        System.out.println(createdUser);

        //Exercise 1.2
        final User renewUser = HttpUtil.sendPut(URI.create(BASE_URL + "/users/7"), user);
        System.out.println(renewUser);

        //Exercise 1.3
        HttpUtil.sendDelet(URI.create(BASE_URL + "/users/1"), user);

        //Exercise 1.4
        HttpUtil.sendGet(URI.create(BASE_URL + "/users"));


        //Exercise 1.5
        User user1 = HttpUtil.sendGetById(URI.create(String.format("%s/%d", BASE_URL + "/users", 1)));
        System.out.println("user1 = " + user1);

        //Exercise 1.5
        User userName = HttpUtil.sendGetByName(URI.create(String.format("%s?username=%s", BASE_URL + "/users", "Maxime_Nienow")));
        System.out.println("user2 = " + userName);


        //Exercise 2
        HttpUtil httpUtil = new HttpUtil();
        String userNumber = "10";
        String maxResult = httpUtil.maxResult(URI.create(BASE_URL_USERS_POST + "/" + userNumber + "/posts"));
        httpUtil.getCommentsByPost(URI.create(BASE_URL_USERS_POST_COMMENTS + maxResult + "/comments"), userNumber);


        //Exercise 3
        final List<String> toDos = HttpUtil.getToDos(URI.create(String.format("%s/%s/todos", BASE_URL_TODOS, "1")));
        System.out.println("toDos = " + toDos);


    }
}
