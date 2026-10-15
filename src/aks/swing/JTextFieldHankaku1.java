package aks.swing;

import aks.util.FontUtility;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class JTextFieldHankaku1 extends javax.swing.JTextField {

    private Font font14;

    public JTextFieldHankaku1() {
        super();
        initComponents();
    }

    public JTextFieldHankaku1(String s_) {
        super(s_);
        initComponents();
    }

    private void initComponents() {
        addFocusListener(new FocusAction());
        addKeyListener(new KeyAction());
        font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

    }

    private class KeyAction implements KeyListener {

        @Override
        public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                transferFocus();
            }

        }

        @Override
        public void keyTyped(KeyEvent event) {
        }
    }

    private class FocusAction implements FocusListener {

        @Override
        public void focusGained(final FocusEvent e) {
        }

        @Override
        public void focusLost(final FocusEvent e) {
        }
    }
}
