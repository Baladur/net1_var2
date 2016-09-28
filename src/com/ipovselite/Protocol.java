package com.ipovselite;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Protocol {
    public static void sendFile (Socket sock, String actualFileName, JProgressBar progressBar) throws IOException {

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

    }

    public static void receiveFile (Socket sock, Boolean status, String saveFolder, JProgressBar progressBar) throws IOException{

        if (status == true) {
            System.out.println("Receiving");

            //receive name
            byte[] name = new byte[256];
            int readName = sock.getInputStream().read(name);
            String sname = new String(name, 0, readName);
            System.out.println(sname);

            //receive size
            byte[] size = new byte[Long.BYTES];
            sock.getInputStream().read(size);
            long lsize = ByteBuffer.wrap(size).getLong();
            System.out.println("size = " + lsize);

            //receive file
            byte[] fileBuf = new byte[1024];
            int read = 0;
            FileOutputStream fos = new FileOutputStream(saveFolder + "/" + sname);
            int totalRead = 0;

            while ((read = sock.getInputStream().read(fileBuf)) > 0) {
                //use Math.min because in the last iteration we dont get correct count of bytes, we get 100 bytes somewhy
                read = Math.min(read, (int) lsize - totalRead);
                fos.write(fileBuf, 0, read);
                totalRead += Math.min(read, (int) lsize - totalRead);
                System.out.println("read = " + read + ", totalRead = " + totalRead);
            }
            fos.close();
        }
        else System.out.println("Receiving is forbidden");
    }
}
