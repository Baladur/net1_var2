package com.ipovselite;

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

        Protocol.send(sock);

        sock.close();
    }


}
