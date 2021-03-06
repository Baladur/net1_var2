package com.ipovselite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class FileTransferFrame extends JFrame {

    FileTransferPanel panel;

    public FileTransferFrame(TransferAction pAction, List<String> pFiles, List<Integer> pSizes, Client pClient) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 4, d.height / 4);
        setSize(d.width / 3, d.height/4);
        panel = pAction.equals(TransferAction.SEND) ? new SendPanel(getWidth(), getHeight(), pFiles, pSizes, pClient) : new ReceivePanel(getWidth(), getHeight(), pFiles, pSizes, pClient);
        panel.getOkButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        panel.setDoubleBuffered(true);
        add(panel);
        //setVisible(true);

    }

    public FileTransferPanel getPanel() {
        return this.panel;
    }

    public List<JProgressBar> getProgressBars() {
        return panel.getProgressBars();
    }

    public List<JLabel> getTimeLabels() { return panel.getTimeLabels(); }

    public void render() {
        setVisible(true);
    }

    public void setOkEnabled() {
        panel.getOkButton().setEnabled(true);
    }
}
