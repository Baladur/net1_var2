package com.ipovselite;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 28.09.2016.
 */
public class ClientProcessor extends Thread {
    private Client client;
    private boolean isFinished = false;

    public ClientProcessor(Socket sock) {
        client = new Client(sock);
    }

    public void run() {
        try {
            System.out.println("In run");
            List<String> fileNames = new ArrayList<>();
            List<Integer> fileSizes = new ArrayList<>();
            client.processRequest(fileNames, fileSizes);

            //here roman creates Receive Window
            FileTransferFrame frame = new FileTransferFrame(TransferAction.RECEIVE, fileNames, fileSizes, client);

        } catch (IOException ioe) {
            //process error!
        } catch (Exception e) {
            if (e.getMessage().equals("protocol")) {
                //process protocol error!
            }
        } finally {
            try {
                client.closeConnection();
            } catch (IOException e) {
                //process error!
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
