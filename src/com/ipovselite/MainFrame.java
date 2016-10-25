package com.ipovselite;

import javax.swing.*;
import java.awt.*;

/**
 * Created by roman on 25.09.2016.
 */
public class MainFrame extends JFrame {
    public void render() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 4, d.height / 4);
        setSize(d.width / 2, d.height/4);
        //JMenuBar menuBar = new JMenuBar();
        MainPanel panel = new MainPanel(d.width / 2 , d.height / 2);
        //panel.setSize(getWidth(), getHeight());
        panel.setDoubleBuffered(true);
        //setJMenuBar(menuBar);
        add(panel);


        setVisible(true);
    }

}
