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
public class FileTransferPanel extends JPanel {

    protected JLabel ltitle = new JLabel("");
    protected JButton ok = new JButton("OK");
    protected JButton cancel = new JButton("Прервать");

    public FileTransferPanel(int pWidth, int pHeight, List<String> pFiles, List<Integer> pSizes) {
        setSize(pWidth, pHeight);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel ptitle = new JPanel();
        ptitle.setSize(getWidth(), getHeight() / 8);
        ptitle.setLayout(new BorderLayout());
        ptitle.add(ltitle, BorderLayout.CENTER);
        add(ptitle);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel pprogress = new JPanel();
        pprogress.setLayout(new GridLayout(pFiles.size(), 3));
        for (int i = 0; i < pFiles.size(); ) {
            pprogress.add(new JLabel(file));
            JProgressBar pb = new JProgressBar(0, pSizes.get());
            pb.setValue(0);
            pb.setStringPainted(true);
            pprogress.add(pb);
        }
        add(pprogress);
        JPanel pokCancel = new JPanel();
        pokCancel.setSize(getWidth(), getHeight()/4);
        pokCancel.setLayout(new BoxLayout(pokCancel, BoxLayout.X_AXIS));
        pokCancel.add(ok);
        pokCancel.add(cancel);
        add(pokCancel);
        ok.setEnabled(false);
        cancel.setEnabled(true);
    }
}
