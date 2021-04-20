package launcher;

import javax.swing.*;
import java.awt.event.*;

public class Splash extends JDialog {
    private JPanel contentPane;
    public JLabel progressLbl;

    public Splash() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Novel-Grabber Launcher");
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {

    }
}
