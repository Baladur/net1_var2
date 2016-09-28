package com.ipovselite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by roman on 25.09.2016.
 */
public class MainPanel extends JPanel {
    JPanel psend = new JPanel();
    JPanel preceive = new JPanel();
    JSeparator ssend = new JSeparator();
    JSeparator sreceive = new JSeparator();
    JSeparator scenter = new JSeparator();
    JLabel lsend = new JLabel("Отправление");
    JLabel lreceive = new JLabel("Приём");
    JLabel laddress = new JLabel("Адрес");
    JLabel lport = new JLabel("Порт");
    JLabel lselectDownloadDir = new JLabel("");
    List<JLabel> lfiles = new ArrayList<>();
    JTextField taddress = new JTextField();
    JTextField tport = new JTextField();
    JButton bselectFile = new JButton("Выберите файлы");
    JButton bselectDownloadDir = new JButton("Выберите папку для сохранения файлов");
    JButton bsend = new JButton("Отправить");
    JCheckBox cbserverOnOff = new JCheckBox();
    JLabel lserverOnOff = new JLabel("Приём файлов включен");
    List<File> files = new ArrayList<>();
    int fileCounter = 0;
    String downloadDir;
    Server server;

    public MainPanel(int pWidth, int pHeight) {
        this.setSize(pWidth, pHeight);
        psend.setSize(pWidth / 4 * 3, pHeight);
        preceive.setSize(pWidth/ 4, pHeight);
        cbserverOnOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (cbserverOnOff.isSelected()) {
                        //start server
                        if (downloadDir == null) {
                            //process error!
                        }
                        server = new Server(12345, downloadDir);
                        server.waitForClients();
                        System.out.println("Server started!");

                    } else {
                        //stop server
                        server.shutDown();
                        System.out.println("Server stopped!");
                    }
                } catch (IOException ioe) {
                    //process error!
                    ioe.printStackTrace();
                }

            }
        });
        bselectDownloadDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dir = getDirFromFileChooser();
                if (dir != null) {
                    downloadDir = dir;
                    lselectDownloadDir.setText(downloadDir);
                }
            }
        });
        for (int i = 0; i < 5; i++) {
            lfiles.add(new JLabel(""));
        }
        bselectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //select file
                File file = getFileFromFileChooser();
                if (file != null) {
                    fileCounter++;
                }
                lfiles.get(fileCounter-1).setText(file.getName());
                files.add(file);

            }
        });
        bsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send file
                if (files.size() == 0) {
                    System.err.println("No file selected!");
                } else {
                    int port = 0;
                    try {
                        port = Integer.parseInt(tport.getText());
                    } catch (NumberFormatException nfe) {
                        //process error!
                        nfe.printStackTrace();
                    }
                    System.out.println("Sending files:");
                    List<String> fileNames = new ArrayList<String>();
                    List<Integer> fileSizes = new ArrayList<Integer>();
                    for (File file : files) {
                        System.out.println(file.getAbsolutePath());
                        fileNames.add(file.getName());
                        fileSizes.add((int)file.length());
                    }
                    try {
                        Client client = new Client(taddress.getText(), port);
                        new FileTransferFrame(TransferAction.SEND, fileNames, fileSizes,client);
                    } catch (IOException ioe) {
                        //process error!
                        ioe.printStackTrace();
                    }
                    //clear files
                    fileCounter = 0;
                    for (JLabel fileName : lfiles) {
                        fileName.setText("");

                    }
                    files.clear();
                }

            }
        });

        psend.setLayout(new BoxLayout(psend, BoxLayout.Y_AXIS));
        psend.add(new JPanel().add(lsend));
        psend.add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel addressPort = new JPanel();
        addressPort.setSize(getWidth() / 2, getHeight() / 6);
        addressPort.setLayout(new GridLayout(2, 2));
        addressPort.add(laddress);
        addressPort.add(taddress);
        addressPort.add(lport);
        addressPort.add(tport);
        psend.add(addressPort);
        JPanel pselectFile = new JPanel();
        pselectFile.setSize(getWidth() / 2, getHeight() / 4);
        pselectFile.setLayout(new GridLayout(2, 3));
        pselectFile.add(bselectFile);
        for (JLabel fileName : lfiles) {
            pselectFile.add(fileName);
        }
        psend.add(pselectFile);
        psend.add(new JPanel().add(bsend));
        preceive.setLayout(new BoxLayout(preceive, BoxLayout.Y_AXIS));
        preceive.add(new JPanel().add(lreceive));
        preceive.add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel pserverOnOff = new JPanel();
        pserverOnOff.setSize(getWidth() / 2, getHeight() / 8);
        pserverOnOff.setLayout(new BoxLayout(pserverOnOff, BoxLayout.Y_AXIS));
        pserverOnOff.add(lserverOnOff);
        pserverOnOff.add(cbserverOnOff);
        pserverOnOff.add(bselectDownloadDir);
        pserverOnOff.add(lselectDownloadDir);
        preceive.add(pserverOnOff);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(psend);
        add(new JSeparator(SwingConstants.VERTICAL));
        add(preceive);
    }

    private File getFileFromFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        // If cancel button selected return
        if (result == JFileChooser.CANCEL_OPTION) return null;

        // Obtain selected file

        File file = fileChooser.getSelectedFile();
        System.out.print("Filename " + file.getAbsolutePath());
        return file;
    }

    private String getDirFromFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        // If cancel button selected return
        if (result == JFileChooser.CANCEL_OPTION) return null;

        // Obtain selected file

        File file = fileChooser.getSelectedFile();
        System.out.print("Filename " + file.getAbsolutePath());
        return file.getName();
    }
}
