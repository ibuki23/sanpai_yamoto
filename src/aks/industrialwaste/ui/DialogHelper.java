package aks.industrialwaste.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;

public class DialogHelper {

    public static JDialog getNewInstance(Component parent, boolean modal) {
        Window w = SwingUtilities.getWindowAncestor(parent);
        if (w instanceof Dialog) {
            return new JDialog((Dialog) w, modal);
        } else {
            Frame f = JOptionPane.getFrameForComponent(parent);

            return new JDialog(f, modal);
        }
    }

    public static Dimension getMaximumSize() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle desktopBounds = env.getMaximumWindowBounds();
        return desktopBounds.getSize();
    }
}
