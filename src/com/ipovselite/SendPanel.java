package com.ipovselite;

import java.io.File;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class SendPanel extends FileTransferPanel {
    public SendPanel(int pWidth, int pHeight, List<File> pFiles, String pAddress) {
        super(pWidth, pHeight, pFiles);
        ltitle.setText("Отправка файлов на " + pAddress);
    }
}
