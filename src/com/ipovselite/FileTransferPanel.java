package com.ipovselite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class FileTransferPanel extends JPanel {

    protected JLabel ltitle = new JLabel("");
    protected JButton ok = new JButton("OK");
    protected JButton cancel = new JButton("Прервать");
    protected List<JProgressBar> progressBars = new ArrayList<>();
    private List<JLabel> timeLabels = new ArrayList<>();
    protected Client client;

    public FileTransferPanel(int pWidth, int pHeight, List<String> pFiles, List<Integer> pSizes, Client pClient) {
        client = pClient;
        setSize(pWidth, pHeight);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel ptitle = new JPanel();
        ptitle.setSize(getWidth(), getHeight() / 8);
        ptitle.setLayout(new BorderLayout());
        ptitle.add(ltitle, BorderLayout.CENTER);
        add(ptitle);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel pprogress = new JPanel();
        pprogress.setLayout(new GridLayout(pFiles.size(), 4));
        for (int i = 0; i < pFiles.size(); i++) {
            pprogress.add(new JLabel(pFiles.get(i)));
            pprogress.add(new JLabel(pSizes.get(i) + " bytes"));
            JProgressBar pb = new JProgressBar(0, 100);
            JLabel ltime = new JLabel("00:00:00");
            pb.setValue(0);
            pb.setStringPainted(true);
            pprogress.add(pb);
            pprogress.add(ltime);
            progressBars.add(pb);
            timeLabels.add(ltime);
        }
        add(pprogress);
        JPanel pokCancel = new JPanel();
        pokCancel.setSize(getWidth(), getHeight()/4);
        pokCancel.setLayout(new BoxLayout(pokCancel, BoxLayout.X_AXIS));
        pokCancel.add(ok);
        ok.setEnabled(false);
    }


    public List<JProgressBar> getProgressBars() {
        return progressBars;
    }

    public List<JLabel> getTimeLabels() {
        return timeLabels;
    }

    public JButton getOkButton() { return ok; }
}
