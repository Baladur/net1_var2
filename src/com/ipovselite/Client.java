package com.ipovselite;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.*;

public class Client {

    public static void main(String[] args) {
        System.out.println("Client");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String message = "";

        String host = "127.0.0.1";
        int port = 12345;
        Socket sock = null;

        try {
            sock = new Socket(host, port);

            /*message = br.readLine();
            sock.getOutputStream().write(message.getBytes());

            InputStream is = sock.getInputStream();
            byte buf[] = new byte[64*1024];
            int r = is.read(buf);
            String data = new String(buf, 0, r);
            System.out.println(data);*/

            String actualFileName = "sharik.png";
            String[] nullStr = {""};

            Path path = Paths.get(actualFileName, nullStr);
            byte[] data = Files.readAllBytes(path);
            String fileName = path.getFileName().toString();

            File file = new File(fileName);

            //this thing to read file by portions
            BufferedReader fileReader = new BufferedReader(new FileReader(new File(fileName)));
            //portion's size is 100 bytes (change it)
            int bufsize = 100;
            char[] buf = new char[bufsize];

            //send name of file
            sock.getOutputStream().write(actualFileName.getBytes());
            //send length of file
            sock.getOutputStream().write(ByteBuffer.allocate(Long.BYTES).putLong(file.length()).array());
            int read = 0;

            byte[] tmpData = new byte[bufsize];
            for (int i = 0; i<data.length; i+=Math.min(bufsize, (int)(data.length - i))) {
                for (int j=i; j<Math.min(i+bufsize, i+(int)(data.length - i)); j++) {
                    tmpData[j-i] = data[j];
                }
                sock.getOutputStream().write(tmpData);
            }

            //use Math.min because in the last iteration we have to pass less than 100 bytes
            /*for (long i = 0; i < file.length(); i += Math.min(bufsize, (int)(file.length() - i))) {

                read = fileReader.read(buf);
                sock.getOutputStream().write(new String(buf).getBytes());
                System.out.println("Client:Sent " + (i + read));
            }*/

            /*sock.getOutputStream().write(fileName.getBytes());
            sock.getOutputStream().write(String.valueOf(data.length).getBytes());
            sock.getOutputStream().write(data);*/

        }
        catch (IOException ex) {ex.printStackTrace();} finally {
            try {
                //don't forget to close fucken sockets
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
