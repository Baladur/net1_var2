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
    }


}
