package com.ipovselite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class ReceivePanel extends FileTransferPanel {
    private Client client;
    public ReceivePanel(int pWidth, int pHeight, List<String> pFiles, Client pClient) {
        super(pWidth, pHeight, pFiles);
        ltitle.setText("Получение файлов от " + pClient.getAddress());
        client = pClient;
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.answerForRequest(true);
                    client.receiveFiles();
                } catch (IOException ioe) {

                }

            }
        });
    }
}
