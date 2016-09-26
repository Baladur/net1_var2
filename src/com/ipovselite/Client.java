package com.ipovselite;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {

    public static void main(String[] args) {
        System.out.println("Client");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = "";

        String host = "127.0.0.1";
        int port = 49001;


        try {
            Socket sock = new Socket(host, port);

            /*message = br.readLine();
            sock.getOutputStream().write(message.getBytes());

            InputStream is = sock.getInputStream();
            byte buf[] = new byte[64*1024];
            int r = is.read(buf);
            String data = new String(buf, 0, r);
            System.out.println(data);*/

            String actualFileName = "sharik.png";

            Path path = Paths.get(actualFileName);
            byte[] data = Files.readAllBytes(path);
            String fileName = path.getFileName().toString();
            sock.getOutputStream().write(fileName.getBytes());
            sock.getOutputStream().write(String.valueOf(data.length).getBytes());
            sock.getOutputStream().write(data);

        }
        catch (IOException ex) {ex.printStackTrace();}
    }
}
