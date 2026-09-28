package aks.swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class JTextFieldKeiryo extends javax.swing.JTextField {

    private static final Color oColor = new Color(255, 255, 255);
    private static final Color dColor = new Color(255, 255, 204);

    public JTextFieldKeiryo() {
        super();
        initComponents();
    }

    public JTextFieldKeiryo(String s_) {
        super(s_);
        initComponents();
    }

    private void initComponents() {
        addFocusListener(new FocusAction());
        addMouseListener(new MouseAction());
    }

    private class MouseAction implements MouseListener {

        @Override
        public void mouseReleased(MouseEvent e) {
            ((JTextFieldKeiryo) e.getSource()).selectAll();
        }

        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }
    }

    private class FocusAction implements FocusListener {

        public void focusGained(final FocusEvent e) {
            ((JTextFieldKeiryo) e.getSource()).setBackground(dColor);
            ((JTextFieldKeiryo) e.getSource()).selectAll();
        }

        public void focusLost(final FocusEvent e) {
            ((JTextFieldKeiryo) e.getSource()).setBackground(oColor);
        }
    }
}
