package aks.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class MyLineSlash extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension wh = this.getSize();

        g.drawLine(wh.width, 0, 0, wh.height);

    }
}
