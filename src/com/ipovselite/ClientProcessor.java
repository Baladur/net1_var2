package com.ipovselite;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 28.09.2016.
 */
public class ClientProcessor extends Thread {
    private Client client;
    private boolean isFinished = false;

    public ClientProcessor(Socket sock, String downloadDir) {
        client = new Client(sock, downloadDir);
    }

    public void run() {
        FileTransferFrame frame = null;
        try {
            System.out.println("In run");
            List<String> fileNames = new ArrayList<>();
            List<Integer> fileSizes = new ArrayList<>();
            client.processRequest(fileNames, fileSizes);

            //here roman creates Receive Window

            frame = new FileTransferFrame(TransferAction.RECEIVE, fileNames, fileSizes, client);
            frame.render();
            //Thread.sleep(500);
            client.receiveFiles(fileNames, fileSizes, frame.getProgressBars(), frame.getTimeLabels());
        } catch (SocketException se) {
            if (se.getMessage().startsWith("Socket closed")) {
                Message.show("Приём файлов прерван!");
            } else {
                se.printStackTrace();
            }
        }
        catch (IOException ioe) {
            //process error!
            Message.show("Ошибка соединения!");
            ioe.printStackTrace();
        } catch (AppException ae) {
            if (ae.getMessage().equals("protocol")) {
                //process protocol error!
                Message.show("Ошибка приложения!");
            } else {
                ae.printStackTrace();
            }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
            try {
                client.closeConnection();
                isFinished = true;
                frame.setOkEnabled();
                //frame.setVisible(false);
            } catch (IOException e) {
                //process error!
                Message.show("Ошибка при закрытии приёма!");
                e.printStackTrace();
            }
        }
    }

    public Client getClient() {
        return this.client;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
