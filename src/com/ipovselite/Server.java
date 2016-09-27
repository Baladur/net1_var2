package com.ipovselite;

import sun.misc.IOUtils;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Server");

        String host = "127.0.0.1";
        int port = 12345;
        ServerSocket serv;

        serv = new ServerSocket(port, 0, InetAddress.getByName(host));
        Socket sock = serv.accept();

        Protocol.receive(sock, "download");

        sock.close();
    }



}
