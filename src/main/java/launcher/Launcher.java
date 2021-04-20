package launcher;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Launcher {
    private final String manifestBackupURL = "https://dl.dropboxusercontent.com/s/i636lfkkvfdz6pv/manifest.json";
    private final Manifest oldManifest;
    private final Manifest newManifest;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private List<ManifestItem> toUpdateList = new ArrayList<>();
    private List<ManifestItem> toRemove = new ArrayList<>();
    private URL manifestURL;


    public Launcher() throws MalformedURLException {
        oldManifest = new Manifest(new File("manifest.json"));
        if(oldManifest.getManifestURL() != null) {
            manifestURL = new URL(oldManifest.getManifestURL());
        } else {
            System.out.println("[INFO] Using backup manifest URL: " + manifestBackupURL);
            if(init.splash != null) {
                init.splash.progressLbl.setText("Using backup manifest URL: " + manifestBackupURL);
            }
            manifestURL = new URL(manifestBackupURL);
        }
        newManifest = new Manifest(manifestURL);
    }

    public boolean isUpToDate() {
        getUpgradeable();
        return toUpdateList.isEmpty();
    }

    public void update() {
        System.out.println("[INFO] Updating...");
        if(init.splash != null) {
            init.splash.progressLbl.setText("Updating...");
        }
        try {
            for(ManifestItem toUpdateFile: toUpdateList) {
                executor.execute(() -> {
                    File file = new File(toUpdateFile.getFilePath());
                    file.getParentFile().mkdirs();
                    try {
                        downloadFileFromURL(new URL(toUpdateFile.getDownloadLink()), file);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();
            try {
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                for(ManifestItem toRemoveFile: toRemove) {
                    File file = new File(toRemoveFile.getFilePath());
                    if(file.exists()) {
                        if(file.delete()) {
                            System.out.println("[INFO] " + toRemoveFile.getFilePath() + " removed");
                        } else {
                            System.err.println("[ERROR] Failed to remove " + toRemoveFile.getFilePath());
                        }
                    }
                }
                downloadFileFromURL(manifestURL, new File("manifest.json"));
            } catch (InterruptedException e) {
                System.err.println("[ERROR] " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[INFO] Finished.");
    }

    private void getUpgradeable() {
        toUpdateList.addAll(newManifest.getManifestItems());
        for(ManifestItem newItem: newManifest.getManifestItems()) {
            if(newItem.getTimestamp() == -1) {
                toRemove.add(newItem);
                toUpdateList.remove(newItem);
                continue;
            }
            for(ManifestItem oldItem: oldManifest.getManifestItems()) {
                if(newItem.equals(oldItem)) {
                    File localFile = new File(oldItem.getFilePath());
                    if(newItem.getTimestamp() <= oldItem.getTimestamp() && localFile.exists()) {
                        toUpdateList.remove(newItem);
                    }
                }
            }
        }

    }

    public static void downloadFileFromURL(URL url, File targetFile) throws Exception {
        String filename = targetFile.toString().substring(targetFile.toString().lastIndexOf("/")+1);
        System.out.println("[INFO] Downloading " + filename);
        if(init.splash != null) {
            init.splash.progressLbl.setText("Downloading " + filename);
        }
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }

    public void startNG() {
        System.out.println("[INFO] Starting Novel-Grabber...");
        if(init.splash != null) {
            init.splash.progressLbl.setText("Starting Novel-Grabber...");
        }
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", "bin/Novel-Grabber.jar");
        try {
            Process process = pb.start();
        } catch (IOException e) {
            System.err.println("[ERROR] Could not start Novel-Grabber: " + e.getMessage());
            if(init.splash != null) {
                init.splash.progressLbl.setText("Finished.");
            }
        }
    }
}
