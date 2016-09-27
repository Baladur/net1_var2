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

        String actualFileName = "sharik.png";
        String[] nullStr = {""};

        Path path = Paths.get(actualFileName, nullStr);
        byte[] data = Files.readAllBytes(path);
        String fileName = path.getFileName().toString();

        File file = new File(fileName);

        //portion's size
        int bufsize = 1024;

        //send name of file
        sock.getOutputStream().write(fileName.getBytes());
        //send length of file
        sock.getOutputStream().write(ByteBuffer.allocate(Long.BYTES).putLong(file.length()).array());

        //send file
        byte[] tmpData = new byte[bufsize];
        for (int i = 0; i<data.length; i+=Math.min(bufsize, (int)(data.length - i))) {
            for (int j=i; j<Math.min(i+bufsize, i+(int)(data.length - i)); j++) {
                tmpData[j-i] = data[j];
            }
            sock.getOutputStream().write(tmpData);
        }

        sock.close();


    }
}
