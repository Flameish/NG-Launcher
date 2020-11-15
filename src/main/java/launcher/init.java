package launcher;

import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import java.awt.*;

public class init {
    public static Splash splash;
    public static void main(String[] args) {
        try {
            if(args.length == 0 || !args[0].toLowerCase().equals("-nogui")) {
                startSplash();
            }
            Thread.sleep(5000);
            Launcher launcher = new Launcher();
            if(!launcher.isUpToDate()) {
                launcher.update();
            } else {
                System.out.println("[INFO] Everything up-to-date.");
            }
            launcher.startNG();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void startSplash() {
        EventQueue.invokeLater(() -> {
            try {
                System.setProperty("awt.useSystemAAFontSettings","on");
                System.setProperty("swing.aatext", "true");
                UIManager.setLookAndFeel(new FlatIntelliJLaf());
                setUIFont (new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,12));
                splash = new Splash();
                splash.setLocationRelativeTo(null);
                splash.pack();
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
}
