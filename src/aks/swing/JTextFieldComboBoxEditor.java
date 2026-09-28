package aks.swing;

import aks.util.FontUtility;
import java.awt.Color;
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
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.DefaultEditorKit;

public class JTextFieldComboBoxEditor extends BasicComboBoxEditor {

    private static final Color oColor = new Color(255, 255, 255);
    private static final Color dColor = new Color(255, 255, 204);
    private Font font14;
    private aks.swing.JTextField editor;

    @Override
    protected JTextField createEditorComponent() {
        editor = new JTextField();
        editor.setEditable(true);

        return editor;
    }

    public JTextFieldComboBoxEditor() {
        super();
        initComponents();
    }

    public JTextFieldComboBoxEditor(String s_) {
        initComponents();
    }

    private void initComponents() {

        editor.addFocusListener(new FocusAction());
        editor.addKeyListener(new KeyAction());
        editor.addMouseListener(new PopupMenuMsouseListener());
        font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 44);

    }

    class PopupMenuMsouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mousePopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mousePopup(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        private void mousePopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                // ポップアップメニューを表示する
                JComponent c = (JComponent) e.getSource();
                showPopup(c, e.getX(), e.getY());
                e.consume();
            }
        }

        protected void showPopup(JComponent c, int x, int y) {
            JPopupMenu pmenu = new JPopupMenu();

            ActionMap am = c.getActionMap();

            Action cut = am.get(DefaultEditorKit.cutAction);
            addMenu(pmenu, "切り取り", cut, 'X', KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));

            Action copy = am.get(DefaultEditorKit.copyAction);
            addMenu(pmenu, "コピー", copy, 'C', KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));

            Action paste = am.get(DefaultEditorKit.pasteAction);
            addMenu(pmenu, "貼り付け", paste, 'V', KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));

            Action all = am.get(DefaultEditorKit.selectAllAction);
            addMenu(pmenu, "すべて選択", all, 'A', KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));

            //font14
            pmenu.setFont(font14);
            pmenu.show(c, x, y);
        }

        protected void addMenu(JPopupMenu pmenu, String text, Action action, int mnemonic, KeyStroke ks) {
            if (action != null) {
                JMenuItem mi = pmenu.add(action);
                if (text != null) {
                    mi.setText(text);
                }
                if (mnemonic != 0) {
                    mi.setMnemonic(mnemonic);
                }
                if (ks != null) {
                    mi.setAccelerator(ks);
                }
            }
        }

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
                editor.transferFocus();
            }

        }

        @Override
        public void keyTyped(KeyEvent event) {
        }
    }

    private class FocusAction implements FocusListener {

        @Override
        public void focusGained(final FocusEvent e) {
            editor.setBackground(dColor);
            editor.selectAll();

        }

        @Override
        public void focusLost(final FocusEvent e) {
            editor.setBackground(oColor);
        }
    }

}
