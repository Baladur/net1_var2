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
        Socket s = serv.accept();

        byte[] size = new byte[Long.BYTES];
        byte[] name = new byte[256];

        int readName = s.getInputStream().read(name);
        String sname = new String(name, 0, readName);
        System.out.println(sname);

        s.getInputStream().read(size);
        long lsize = ByteBuffer.wrap(size).getLong();
        System.out.println("size = " + lsize);

        byte[] fileBuf = new byte[1024];
        int read = 0;
        FileOutputStream fos = new FileOutputStream("download/" + sname);
        int totalRead = 0;

        while ((read = s.getInputStream().read(fileBuf)) > 0) {
            //use Math.min because in the last iteration we dont get correct count of bytes, we get 100 bytes somewhy
            read = Math.min(read, (int)lsize - totalRead);
            fos.write(fileBuf, 0, read);
            totalRead += Math.min(read, (int)lsize - totalRead);
            System.out.println("read = " + read + ", totalRead = " + totalRead);
        }

        s.close();
        fos.close();
    }

}
