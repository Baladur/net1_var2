package com.ipovselite;

import sun.misc.IOUtils;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Server {

    private ServerSocket serverSocket;
    private String downloadDir;
    private List<ClientProcessor> clientProcessors = new ArrayList<>();

    public Server(int pPort, String pDownloadDir) throws UnknownHostException, IOException, Exception {
        String host = resolveHost();
        if (host == null) {
            throw new Exception("Unresolved host");
        }
        serverSocket = new ServerSocket(pPort, 0, InetAddress.getByName(host));
        downloadDir = pDownloadDir;
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
                        ClientProcessor clientProcessor = new ClientProcessor(sock, downloadDir);
                        clientProcessors.add(clientProcessor);
                        clientProcessor.start();
                    } catch (SocketException se) {
                        break;
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        });
        t.start();

    }

    private String resolveHost() throws SocketException {
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                String host = addr.getHostAddress();
                if (!host.equals("127.0.0.1") && !host.startsWith("192.168")) {
                    String[] sp = host.split("\\.");
                    if (sp.length == 4) {
                        return host;
                    }
                }
            }
        }

        return null;
    }


    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
}
