package com.ipovselite;

import sun.misc.IOUtils;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Server");

        String host = "127.0.0.1";
        int port = 49001;
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


        InputStream name = s.getInputStream();
        byte buf[] = new byte[256];
        int r = name.read(buf);
        String nameStr = new String(buf, 0, r);

        InputStream size = s.getInputStream();
        byte buf2[] = new byte[256];
        r = size.read(buf2);
        String sizeStr = new String(buf2, 0, r);
        int sizeVal = Integer.parseInt(sizeStr);;

        InputStream is = s.getInputStream();
        byte[] buffer = new byte[sizeVal];
        r = is.read(buffer);
        System.out.println(r+" "+sizeVal);
        FileOutputStream fos=new FileOutputStream("download/"+nameStr);
        fos.write(buffer, 0, sizeVal);

        is.close();
        fos.close();
    }

    public void go(){

    }

}
