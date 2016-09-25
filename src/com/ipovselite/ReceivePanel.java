package com.ipovselite;

import java.io.File;
import java.util.List;

/**
 * Created by roman on 25.09.2016.
 */
public class ReceivePanel extends FileTransferPanel {
    public ReceivePanel(int pWidth, int pHeight, List<File> pFiles, String pAddress) {
        super(pWidth, pHeight, pFiles);
        ltitle.setText("Получение файлов от " + pAddress);
    }
}
