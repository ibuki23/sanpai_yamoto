package aks.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class MyLine extends JPanel {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension wh = this.getSize();

        g.drawLine(0, wh.height / 2, wh.width, wh.height / 2);
    }
}
