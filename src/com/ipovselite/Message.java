package com.ipovselite;

import javax.swing.*;

/**
 * Created by roman on 25.10.2016.
 */
public class Message {
    public static void show(String pMsg) {
        JOptionPane.showMessageDialog(null, pMsg, "Сообщение", JOptionPane.PLAIN_MESSAGE);
    }
}
