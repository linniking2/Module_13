import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPlaceholderAPI {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static String sendRequest(String url, String method, String requestBody) throws IOException {
        URL apiUrl = new URL(BASE_URL + url);
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setRequestMethod(method);

        if (requestBody != null) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                osw.write(requestBody);
                osw.flush();
            }
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            return null;
        }
    }

    public static String createNewUser(String newUserJson) throws IOException {
        return sendRequest("/users", "POST", newUserJson);
    }

    public static String updateUser(int userId, String updatedUserJson) throws IOException {
        return sendRequest("/users/" + userId, "PUT", updatedUserJson);
    }

    public static void deleteUser(int userId) throws IOException {
        sendRequest("/users/" + userId, "DELETE", null);
    }

    public static String getAllUsers() throws IOException {
        return sendRequest("/users", "GET", null);
    }

    public static String getUserById(int userId) throws IOException {
        return sendRequest("/users/" + userId, "GET", null);
    }

    public static String getUserByUsername(String username) throws IOException {
        return sendRequest("/users?username=" + username, "GET", null);
    }

    public static String getCommentsForLastPost(int userId) throws IOException {
        String userPosts = sendRequest("/users/" + userId + "/posts", "GET", null);
        String[] postIds = userPosts.split("\"id\":");
        int lastPostId = Integer.parseInt(postIds[postIds.length - 1].split(",")[0]);

        return sendRequest("/posts/" + lastPostId + "/comments", "GET", null);
    }

    public static String getOpenTodosForUser(int userId) throws IOException {
        return sendRequest("/users/" + userId + "/todos?completed=false", "GET", null);
    }

    public static void main(String[] args) {
        try {
            // Завдання 1: Створення, оновлення та видалення користувача
            String newUserJson = "{ \"name\": \"John Doe\", \"username\": \"johndoe\", \"email\": \"johndoe@example.com\" }";
            String createdUserResponse = createNewUser(newUserJson);
            System.out.println("Created User: " + createdUserResponse);

            String updatedUserJson = "{ \"id\": 1, \"name\": \"Updated User\", \"username\": \"updateduser\", \"email\": \"updated@example.com\" }";
            String updatedUserResponse = updateUser(1, updatedUserJson);
            System.out.println("Updated User: " + updatedUserResponse);

            deleteUser(1);

            // Завдання 1: Отримання інформації про користувачів
            System.out.println("All Users: " + getAllUsers());
            System.out.println("User by ID 2: " + getUserById(2));
            System.out.println("User by username Bret: " + getUserByUsername("Bret"));

            // Завдання 2: Отримання коментарів до останнього поста
            System.out.println("Comments for last post: " + getCommentsForLastPost(1));

            // Завдання 3: Отримання відкритих задач для користувача
            System.out.println("Open Todos for User 1: " + getOpenTodosForUser(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
