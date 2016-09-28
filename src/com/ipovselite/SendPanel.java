package com.ipovselite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class SendPanel extends FileTransferPanel {
    public SendPanel(int pWidth, int pHeight, List<String> pFiles, List<Integer> pSizes, Client pClient) {
        super(pWidth, pHeight, pFiles, pSizes, pClient);
        ltitle.setText("Отправка файлов на " + pClient.getAddress());
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<File> files = new ArrayList<File>();
                for (String fileName : pFiles) {
                    files.add(new File(fileName));
                }
                try {
                    if (client.sendRequest(files)) {
                        client.sendFiles(files, progressBars);
                    }
                } catch (IOException ioe) {
                    //process error!
                    ioe.printStackTrace();
                }

            }
        });
    }


}
