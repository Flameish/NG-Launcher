package launcher;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Manifest {
    private String manifestURL;
    private final List<ManifestItem> manifestItems = new ArrayList<>();

    public Manifest(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JSONObject manifestJSON = (JSONObject) new JSONParser().parse(br);

            manifestURL = (String) manifestJSON.get("manifestURL");

            JSONArray items = (JSONArray) manifestJSON.get("items");
            for (Object item: items) {
                manifestItems.add(new ManifestItem((JSONObject) item));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public Manifest(URL url) {
        System.out.println("[INFO] Fetching manifest...");
        try (
                InputStream in = url.openStream();
                InputStreamReader inReader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(inReader)
        ) {
            JSONObject manifestJSON = (JSONObject) new JSONParser().parse(br);
            JSONArray items = (JSONArray) manifestJSON.get("items");
            for (Object item: items) {
                manifestItems.add(new ManifestItem((JSONObject) item));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public String getManifestURL() {
        return manifestURL;
    }

    public List<ManifestItem> getManifestItems() {
        return manifestItems;
    }
}
