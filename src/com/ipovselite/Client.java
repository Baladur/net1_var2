package com.ipovselite;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.*;

public class Client {

    public static void main(String[] args) throws IOException {
        System.out.println("Client");

        String host = "127.0.0.1";
        int port = 12345;
        Socket sock = new Socket(host, port);

        JProgressBar bar = new JProgressBar();

        Protocol.sendFile(sock, "sharik1.png", bar);

        sock.close();
    }


}
