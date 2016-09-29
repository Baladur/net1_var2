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
import java.util.ArrayList;
import java.util.List;

public class Protocol {

    public static boolean sendRequest(Socket sock, List<File> files) throws IOException {
        byte[] startRequestMsg = {'r', 'e', 'q'};
        byte[] answer = new byte[1];
        sock.getOutputStream().write(startRequestMsg);
        sock.getOutputStream().write(ByteBuffer.allocate(Integer.BYTES).putInt(files.size()).array());
        for (File file : files) {
            String fileName = file.getName();
            sock.getOutputStream().write(fileName.getBytes());
            sock.getOutputStream().write(ByteBuffer.allocate(Long.BYTES).putLong(file.length()).array());
            System.out.println("transfered name = " + fileName + " and size = " + file.length());
        }
        sock.getInputStream().read(answer);
        if (answer[0] == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void sendFile (Socket sock, File file, JProgressBar progressBar) throws IOException {

        String[] nullStr = {""};

        //Path path = Paths.get(actualFileName, nullStr);

        String fileName = file.getName();
        byte[] data = Files.readAllBytes(file.toPath());

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

    public static void answerForRequest(Socket sock, boolean answer) throws IOException {
        byte[] answerBuf = new byte[0];
        answerBuf[0] = answer ? (byte)1 : 0;
        sock.getOutputStream().write(answerBuf);
    }

    public static void processRequest(Socket sock, List<String> fileNames, List<Integer> fileSizes) throws IOException {
        byte[] fileCountBuf = new byte[Integer.BYTES];
        byte[] fileNameBuf = new byte[256];
        byte[] fileSizeBuf = new byte[Long.BYTES];
        System.out.println("before reading file count");
        sock.getInputStream().read(fileCountBuf);
        int fileCount = ByteBuffer.wrap(fileCountBuf).getInt();
        System.out.println(fileCount);
        for (int i = 0; i < fileCount; i++) {
            try {
                int read = sock.getInputStream().read(fileNameBuf);
                System.out.println(read);
                String fileName = new String(fileNameBuf, 0, read);
                fileNames.add(fileName);
                System.out.println("filename = " + fileName);
                int read2 = sock.getInputStream().read(fileSizeBuf);
                System.out.println(read2);
                fileSizes.add(ByteBuffer.wrap(fileSizeBuf).getInt());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
