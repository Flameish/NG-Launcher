package launcher;

import org.json.simple.JSONObject;

import java.util.Objects;

public class ManifestItem {
    private String filePath;
    private String downloadLink;
    private long timestamp;

    public ManifestItem(JSONObject manifestItem) {
        filePath = (String) manifestItem.get("filePath");
        downloadLink = (String) manifestItem.get("downloadLink");
        timestamp = (long) manifestItem.get("timestamp");
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManifestItem that = (ManifestItem) o;
        return filePath.equals(that.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
}
