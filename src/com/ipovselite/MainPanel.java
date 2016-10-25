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
    JLabel lserverPort = new JLabel("Порт приёма");
    List<JLabel> lfiles = new ArrayList<>();
    JTextField taddress = new JTextField(15);
    JTextField tport = new JTextField(4);
    JTextField tserverPort = new JTextField(4);
    JButton bselectFile = new JButton("Выбрать файл");
    JButton bselectDownloadDir = new JButton("Выбрать папку скачки");
    JButton bsend = new JButton("Отправить");
    JCheckBox cbserverOnOff = new JCheckBox();
    JLabel lserverOnOff = new JLabel("Приём файлов включен");
    List<File> files = new ArrayList<>();
    int fileCounter = 0;
    String downloadDir;
    Server server;

    public MainPanel(int pWidth, int pHeight) {
        this.setSize(pWidth, pHeight);
        psend.setSize(pWidth / 2, pHeight);
        preceive.setSize(pWidth/ 2, pHeight);
        cbserverOnOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (cbserverOnOff.isSelected()) {
                        //start server
                        if (downloadDir == null) {
                            //process error!
                        }
                        int serverPort = Integer.parseInt(tserverPort.getText());
                        server = new Server(serverPort, downloadDir);
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
                } catch (AppException ae) {
                    if (ae.getMessage().equals("Unresolved host")) {
                        //process error!
                        ae.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        bselectDownloadDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dir = getDirFromFileChooser();
                if (dir != null) {
                    downloadDir = dir;
                    if (server != null) {
                        server.setDownloadDir(dir);
                    }
                    lselectDownloadDir.setText(dir);
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
                        client.sendRequest(files);
                        final FileTransferFrame ftf = new FileTransferFrame(TransferAction.SEND, fileNames, fileSizes, client);
                        ftf.render();
                        client.sendFiles(files, ftf.getProgressBars(), ftf.getTimeLabels());
                    } catch (IOException ioe) {
                        //process error!
                        ioe.printStackTrace();
                    } catch (Exception pe) {
                        if (pe.getMessage().equals("protocol")) {
                            //process error!
                            pe.printStackTrace();
                        }
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

        JPanel preceiveTitle = new JPanel();
        JPanel pserverOnOff = new JPanel();
        JPanel pOnOff = new JPanel();
        JPanel pserverPort = new JPanel();
        JPanel pselectDownloadDir = new JPanel();
        preceiveTitle.setSize(getWidth() / 2, getHeight() / 4);
        pOnOff.setSize(getWidth() /  2, getHeight() / 4);
        pOnOff.setMaximumSize(new Dimension(getWidth() /  2, getHeight() / 4));
        pOnOff.setLayout(new BoxLayout(pOnOff, BoxLayout.X_AXIS));
        pserverOnOff.setSize(getWidth() / 2, getHeight() /4 * 3);
        pserverOnOff.setMaximumSize(new Dimension(getWidth() / 2, getHeight() /4 * 3));
        pserverOnOff.setLayout(new BoxLayout(pserverOnOff, BoxLayout.Y_AXIS));
        pserverPort.setMaximumSize(new Dimension(getWidth() / 2, getHeight() / 4));
        pserverPort.setLayout(new BoxLayout(pserverPort, BoxLayout.X_AXIS));
        pselectDownloadDir.setSize(getWidth() / 2, getHeight() / 4);
        pselectDownloadDir.setMaximumSize(new Dimension(getWidth() / 2, getHeight() / 4));
        pselectDownloadDir.setLayout(new GridLayout(2, 1));
        lserverPort.setMaximumSize(new Dimension(getWidth() / 4, getHeight() / 4));
        tserverPort.setMaximumSize(new Dimension(getWidth() / 4, getHeight() / 4));
        pserverPort.add(lserverPort);
        pserverPort.add(tserverPort);
        pserverOnOff.add(pserverPort);
        pselectDownloadDir.add(bselectDownloadDir);
        pselectDownloadDir.add(lselectDownloadDir);
        pserverOnOff.add(pselectDownloadDir);
        pOnOff.add(lserverOnOff);
        pOnOff.add(cbserverOnOff);
        pserverOnOff.add(pOnOff);
        preceiveTitle.add(lreceive);
        preceive.add(new JPanel().add(lreceive));
        preceive.add(new JSeparator(SwingConstants.HORIZONTAL));
        preceive.add(pserverOnOff);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(psend);
        JSeparator svertical = new JSeparator(SwingConstants.VERTICAL);
        svertical.setMaximumSize(new Dimension(1, getHeight()));
        add(svertical);
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
        return file.getAbsolutePath();
    }
}
