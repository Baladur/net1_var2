package com.ipovselite;

import sun.misc.IOUtils;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;
    private String downloadDir;
    private List<ClientProcessor> clientProcessors = new ArrayList<>();

    public Server(int pPort, String pDownloadDir) throws UnknownHostException, IOException {
        serverSocket = new ServerSocket(pPort, 0, InetAddress.getLocalHost());
    }

    public void shutDown() throws IOException {
        //find way to shut the while (true) loop in waitForClients
        serverSocket.close();
    }

    public void waitForClients() throws IOException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket sock = serverSocket.accept();
                        ClientProcessor clientProcessor = new ClientProcessor(sock);
                        clientProcessors.add(clientProcessor);
                        clientProcessor.start();
                    }catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        });
        t.start();



    }



}
