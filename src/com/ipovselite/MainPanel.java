package com.ipovselite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by roman on 25.09.2016.
 */
public class MainPanel extends JPanel {
    static final int FILES_LIMIT = 5;
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
    JLabel lserverHost = new JLabel("");
    JLabel lserverPort = new JLabel("Порт приёма");
    List<JLabel> lfiles = new ArrayList<>();
    JTextField taddress = new JTextField(15);
    JTextField tport = new JTextField(4);
    JTextField tserverPort = new JTextField(4);
    JTextField tserverHost = new JTextField();
    JButton bselectFile = new JButton("Выбрать файл");
    JButton bcancelSelectFile = new JButton("Отменить выбор файлов");
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
                        if (tserverPort.getText().trim().length() == 0) {
                            cbserverOnOff.setSelected(false);
                            Message.show("Не указан порт приёма!");
                            return;
                        }
                        if (downloadDir == null) {
                            //process error!
                            cbserverOnOff.setSelected(false);
                            Message.show("Не указана папка скачки!");
                            return;
                        }
                        int serverPort = Integer.parseInt(tserverPort.getText().trim());
                        server = new Server(tserverHost.getText(), serverPort, downloadDir);
                        tserverHost.setText(server.getHost());
                        server.waitForClients();
                        System.out.println("Server started!");

                    } else {
                        //stop server
                        server.shutDown();
                        System.out.println("Server stopped!");
                    }
                } catch (NumberFormatException nfe) {
                    cbserverOnOff.setSelected(false);
                    Message.show("Порт приёма коряво записан!");
                } catch (IllegalArgumentException iae) {
                    cbserverOnOff.setSelected(false);
                    if (iae.getMessage().startsWith("Port value out of range")) {
                        cbserverOnOff.setSelected(false);
                        Message.show("Порт вне диапазона (1 - 65 535)!");
                    } else {
                        iae.printStackTrace();
                    }
                } catch (BindException be) {
                    if (be.getMessage().startsWith("Address already in use")) {
                        cbserverOnOff.setSelected(false);
                        Message.show("Порт занят!");
                    }
                } catch (UnknownHostException ukhe) {
                    cbserverOnOff.setSelected(false);
                    Message.show("Неизвестный адрес " + tserverHost.getText());
                } catch (IOException ioe) {
                    //process error!
                    Message.show("Ошибка соединения!");
                    ioe.printStackTrace();
                } catch (AppException ae) {
                    cbserverOnOff.setSelected(false);
                    if (ae.getMessage().equals("Unresolved host")) {
                        //process error!
                        Message.show("Публичный IP-адрес неизвестен!");
                    } else if (ae.getMessage().equals("protocol")) {
                        Message.show("Ошибка приложения!");
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
        for (int i = 0; i < FILES_LIMIT; i++) {
            lfiles.add(new JLabel(""));
        }
        bselectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //select file
                if (files.size() == FILES_LIMIT) {
                    Message.show("Нельзя передавать за раз больше " + FILES_LIMIT + " файлов!");
                    return;
                }
                File file = getFileFromFileChooser();
                if (file != null) {
                    if (!file.exists()) {
                        Message.show("Файл '" + file.getAbsolutePath() + "' не существует!");
                        return;
                    }
                    fileCounter++;
                    boolean found = false;
                    for (int i = 0; i < files.size(); i++) {
                        if (file.getAbsolutePath().equals(files.get(i).getAbsolutePath())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        lfiles.get(fileCounter-1).setText(file.getName());
                        files.add(file);
                    } else {
                        Message.show("Файл '" + file.getAbsolutePath() + "' уже выбран!");
                        fileCounter--;
                    }
                } else {
                    return;
                }
            }
        });
        bselectFile.setToolTipText("За раз можно отправить не более " + FILES_LIMIT + " файлов");
        bcancelSelectFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //clear files
                fileCounter = 0;
                for (JLabel fileName : lfiles) {
                    fileName.setText("");
                }
                files.clear();
            }
        });
        bsend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //send file
                int port = 0;
                FileTransferFrame ftf = null;
                try {
                    if (taddress.getText().trim().length() == 0) {
                        Message.show("Адрес получателя не указан!");
                        return;
                    }
                    if (tport.getText().trim().length() == 0) {
                        Message.show("Порт получателя не указан!");
                        return;
                    }
                    if (files.size() == 0) {
                        Message.show("Не выбрано ни одного файла!");
                    } else {
                        port = Integer.parseInt(tport.getText().trim());
                        System.out.println("Sending files:");
                        List<String> fileNames = new ArrayList<String>();
                        List<Integer> fileSizes = new ArrayList<Integer>();
                        for (File file : files) {
                            System.out.println(file.getAbsolutePath());
                            fileNames.add(file.getName());
                            fileSizes.add((int) file.length());
                        }

                        Client client = new Client(taddress.getText(), port);
                        client.sendRequest(files);
                        ftf = new FileTransferFrame(TransferAction.SEND, fileNames, fileSizes, client);
                        ftf.render();
                        client.sendFiles(files, ftf.getProgressBars(), ftf.getTimeLabels());
                        //clear files
                        fileCounter = 0;
                        for (JLabel fileName : lfiles) {
                            fileName.setText("");
                        }
                        files.clear();
                    }
                } catch (NumberFormatException nfe) {
                    Message.show("Порт коряво записан!");
                } catch (IllegalArgumentException iae) {
                    if (iae.getMessage().startsWith("port out of range")) {
                        Message.show("Порт вне диапазона (1 - 65 535)!");
                    } else {
                        iae.printStackTrace();
                    }
                } catch (ConnectException ce) {
                    if (ce.getMessage().startsWith("Connection refused")) {
                        Message.show("Не удалось подключиться к " + taddress.getText() + ":" + port + "!");
                    } else if (ce.getMessage().startsWith("Connection timed out")){
                        Message.show("Тайм аут подключения!");
                    } else {
                        ce.printStackTrace();
                    }
                } catch (SocketException se) {
                    if (se.getMessage().startsWith("Connection reset by peer") || se.getMessage().startsWith("Software caused connection abort")) {
                        ftf.setVisible(false);
                        Message.show("Получатель закрыл соединение!");
                    } else {
                        se.printStackTrace();
                    }
                } catch (UnknownHostException ukhe) {
                    Message.show("Неизвестный адрес " + taddress.getText());
                    ukhe.printStackTrace();
                } catch (IOException ioe) {
                    if (ftf != null) {
                        ftf.setVisible(false);
                    }
                    Message.show("Ошибка соединения!");
                    ioe.printStackTrace();
                } catch (AppException ae) {
                    Message.show("Ошибка приложения!");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Finished!");
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
        JPanel psendCancelSelect = new JPanel();
        psendCancelSelect.add(bcancelSelectFile);
        psendCancelSelect.add(bsend);
        psend.add(psendCancelSelect);
        //psend.add(new JPanel().add(bsend));
        preceive.setLayout(new BoxLayout(preceive, BoxLayout.Y_AXIS));

        JPanel preceiveTitle = new JPanel();
        JPanel pserverOnOff = new JPanel();
        JPanel pOnOff = new JPanel();
        JPanel pserverPort = new JPanel();
        JPanel pselectDownloadDir = new JPanel();
        JPanel pserverHost = new JPanel();
        pserverHost.setMaximumSize(new Dimension(getWidth() / 2, getHeight() / 4));
        pserverHost.setLayout(new GridLayout(1,2));
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
        pserverOnOff.add(pserverHost);
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

        pserverHost.add(new JLabel("Адрес приёма"));
        //pserverHost.add(lserverHost);
        pserverHost.add(tserverHost);
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
