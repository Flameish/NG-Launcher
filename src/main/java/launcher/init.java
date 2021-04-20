package launcher;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class init {
    public static Splash splash;

    public static void main(String[] args) {
        final Map<String, List<String>> params = createParamsFromArgs(args);
        processParams(params);
        System.exit(0);
    }

    private static void processParams(Map<String, List<String>> params) {
        if(params.containsKey("nogui") || params.isEmpty()) {
            if (!GraphicsEnvironment.isHeadless()) startSplash();
        }
        try {
            Launcher launcher = new Launcher();
            if(!launcher.isUpToDate()) {
                launcher.update();
            } else {
                System.out.println("[INFO] Everything up-to-date.");
            }
            if (!params.containsKey("nostart")) launcher.startNG();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startSplash() {
        EventQueue.invokeLater(() -> {
            try {
                System.setProperty("awt.useSystemAAFontSettings","on");
                System.setProperty("swing.aatext", "true");
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                setUIFont (new javax.swing.plaf.FontUIResource("DejaVuSans", Font.PLAIN,13));
                splash = new Splash();
                splash.pack();
                splash.setLocationRelativeTo(null);
                splash.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }

    public static Map<String, List<String>> createParamsFromArgs(String[] args) {
        final Map<String, List<String>> params = new HashMap<>();
        List<String> options = null;
        for (final String a : args) {
            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return null;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            } else if (options != null) {
                options.add(a);
            } else {
                System.err.println("Illegal parameter usage");
                return null;
            }
        }
        return params;
    }
}
