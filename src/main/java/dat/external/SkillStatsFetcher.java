package dat.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dat.utils.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillStatsFetcher {

    private static final String BASE_URL =  Utils.getPropertyValue("SKILL_API_URL", "config.properties");

    public static Map<String, JsonNode> fetchSkillStats(List<String> slugs){

        try{

            String joined = String.join(",", slugs);
            URL url = new URL(BASE_URL + joined);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(con.getInputStream());
            Map<String, JsonNode> statsMap = new HashMap<>();

            for (JsonNode skillNode : root.get("data")){

                String slug = skillNode.get("slug").asText();
                statsMap.put(slug, skillNode);

            }

            return statsMap;

        }catch (IOException e){

            e.printStackTrace();
            return Collections.emptyMap();

        }

    }

}
