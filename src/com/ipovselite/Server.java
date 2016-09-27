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

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = "";

        serv = new ServerSocket(port, 0, InetAddress.getByName(host));


        Socket s = serv.accept();

        /*InputStream is = s.getInputStream();
        byte buf[] = new byte[64*1024];
        int r = is.read(buf);
        String data = new String(buf, 0, r);
        System.out.println(data);

        message = br.readLine();
        s.getOutputStream().write(message.getBytes());
        s.close();*/
        byte[] size = new byte[Long.BYTES];

        byte[] name = new byte[256];
        int readName = s.getInputStream().read(name);
        String sname = new String(name, 0, readName);
        System.out.println(sname);
        s.getInputStream().read(size);
        long lsize = ByteBuffer.wrap(size).getLong();
        System.out.println("size = " + lsize);
        byte[] fileBuf = new byte[100];
        int read = 0;
        FileOutputStream fos = new FileOutputStream("download/" + sname);
        int totalRead = 0;
        byte[] file = new byte[(int)lsize];

        while ((read = s.getInputStream().read(fileBuf)) > 0) {
            //use Math.min because in the last iteration we dont get correct count of bytes, we get 100 bytes somewhy
            fos.write(fileBuf, 0, Math.min(read, (int)lsize - totalRead));
            totalRead += Math.min(read, (int)lsize - totalRead);
            System.out.println("read = " + read + ", totalRead = " + totalRead);
        }
        //InputStream name = s.getInputStream();
        /*byte buf[] = new byte[256];
        int r = name.read(buf);
        String nameStr = new String(buf, 0, r);*/

        /*InputStream size = s.getInputStream();
        byte buf2[] = new byte[256];
        r = size.read(buf2);
        String sizeStr = new String(buf2, 0, r);
        int sizeVal = Integer.parseInt(sizeStr);;

        InputStream is = s.getInputStream();
        byte[] buffer = new byte[sizeVal];
        r = is.read(buffer);
        System.out.println(r+" "+sizeVal);
        FileOutputStream fos=new FileOutputStream("download/"+nameStr);
        fos.write(buffer, 0, sizeVal);*/

        //is.close();
        s.close();
        fos.close();
    }

    public void go(){

    }

}
