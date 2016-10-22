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
    public ReceivePanel(int pWidth, int pHeight, List<String> pFiles, List<Integer> pSizes, Client pClient) {
        super(pWidth, pHeight, pFiles, pSizes, pClient);
        ltitle.setText("Получение файлов от " + pClient.getAddress());
    }
}
