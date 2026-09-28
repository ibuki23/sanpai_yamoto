package aks.industrialwaste.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import jp.co.astax.awt.FontUtility;

public class Panel extends JPanel implements AncestorListener {

    protected final JPanel northPane_ = new JPanel(new GridBagLayout());
    protected final JPanel southPane_ = new JPanel(new GridBagLayout());
    protected final JPanel westPane_ = new JPanel(new GridBagLayout());
    protected final JPanel eastPane_ = new JPanel(new GridBagLayout());
    protected final JPanel centerPane_ = new JPanel(new GridBagLayout());
    protected final JLabel titleField_ = new JLabel("", JLabel.CENTER);

    protected static Logger logger_ = Logger.getLogger(Panel.class.getName());

    public Panel() {
        initComponents();
        addAncestorListener(this);
    }

    public void ancestorAdded(AncestorEvent event) {
        updateView();
    }

    public void ancestorMoved(AncestorEvent event) {
    }

    public void ancestorRemoved(AncestorEvent event) {
    }

    protected void initComponents() {
        setLayout(new BorderLayout());

        add(northPane_, BorderLayout.NORTH);
        add(southPane_, BorderLayout.SOUTH);
        add(westPane_, BorderLayout.WEST);
        add(eastPane_, BorderLayout.EAST);
        add(centerPane_, BorderLayout.CENTER);

        // タイトル
        titleField_.setBorder(BorderFactory.createRaisedBevelBorder());
        titleField_.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 18));
        titleField_.setBackground(Color.BLUE);
        titleField_.setForeground(Color.WHITE);
        titleField_.setOpaque(true);
        titleField_.setMinimumSize(new Dimension(100, 35));
        titleField_.setPreferredSize(new Dimension(100, 35));
        northPane_.add(
                 titleField_,
                 new GridBagConstraints(
                          0, 0, 1, 1, 0.9, 1.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(2, 2, 2, 2), 0, 0));
    }

    protected void addContainer(
             Container pane,
             Component c,
             int gridx,
             int gridy,
             int gridwidth,
             int gridheight,
             double weightx,
             double weighty,
             int anchor,
             int fill,
             Insets insets,
             int ipadx,
             int ipady) {
        if (pane.getLayout() instanceof GridBagLayout) {
            pane.add(
                     c,
                     new GridBagConstraints(
                              gridx, gridy, gridwidth, gridheight, weightx, weighty,
                              anchor, fill,
                              insets, ipadx, ipady));
        }
    }

    protected void updateView() {
    }
}
