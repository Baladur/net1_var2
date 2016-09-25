package com.ipovselite;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class FileTransferFrame extends JFrame {

    FileTransferPanel panel;

    public FileTransferFrame(TransferAction pAction, List<File> pFiles, String pAddress) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(d.width / 4, d.height / 4);
        setSize(d.width / 3, d.height/4);
        panel = pAction.equals(TransferAction.SEND) ? new SendPanel(getWidth(), getHeight(), pFiles, pAddress) : new ReceivePanel(getWidth(), getHeight(), pFiles, pAddress);
        panel.setDoubleBuffered(true);
        add(panel);
        setVisible(true);

    }
}
