package pl.kuezese.tiktokgame.license;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.ChatColor;
import pl.kuezese.tiktokgame.TikTokGame;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TikAuth {

    public static boolean checkLicense(TikTokGame plugin, String license) {
        try {
            // Replace the URL with the actual endpoint URL
            String apiUrl = "http://api.cmclient.pl:2086/tikauth?license=" + license;
            String response = sendGETRequest(apiUrl);

            // Parse JSON response
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

            // Check if "success" field is "true" or "false"
            boolean success = jsonObject.has("success") ? jsonObject.get("success").getAsBoolean() : false;
            String message = jsonObject.has("message") ? jsonObject.get("message").getAsString() : "";

            // Log the message if failed
            if (!success) {
                plugin.getLogger().warning(ChatColor.RED + "Failed to authorize license! Response: " + message);
            }

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String sendGETRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        return response.toString();
    }
}
