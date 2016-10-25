package com.ipovselite;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Protocol {

    public static void sendRequest(Socket sock, List<File> files) throws IOException, AppException {
        byte[] startRequestMsg = {'r', 'e', 'q'};
        byte[] ansBuf = new byte[3];
        byte[] answerBuf = new byte[1];
        sock.getOutputStream().write(startRequestMsg);
        sock.getOutputStream().write(ByteBuffer.allocate(Integer.BYTES).putInt(files.size()).array());
        for (File file : files) {
            String fileName = file.getName();
            sock.getOutputStream().write(ByteBuffer.allocate(Integer.BYTES).putInt(fileName.length()).array());
            sock.getOutputStream().write(fileName.getBytes());
            sock.getOutputStream().write(ByteBuffer.allocate(Long.BYTES).putLong(file.length()).array());
            int read1 = sock.getInputStream().read(answerBuf);
            System.out.println("transfered name = " + fileName + " and size = " + file.length() + new String(answerBuf, 0, read1));
        }

        //int read = sock.getInputStream().read(answerBuf);

        /*if (ansBuf[0] != 'a' && ansBuf[1] != 'n' && ansBuf[2] != 's') {
            System.out.println(new String(ansBuf, 0, 3));
            System.out.println(ansBuf[0] + ansBuf[1] + ansBuf[2]);
            throw new Exception("protocol");
        } else {
            System.out.println(new String(ansBuf, 0, 3));
        }*/
        //sock.getInputStream().read(answerBuf);
        /*if (sanswer.equals("n")) {
            return false;
        } else if (sanswer.equals("y")) {
            return true;
        } else {
            throw new Exception("protocol");
        }*/
    }

    public static void sendFile (Socket sock, File file, JProgressBar progressBar,  JLabel time) throws IOException {

        /*String fileName = file.getName();
        byte[] data = Files.readAllBytes(file.toPath());

        //portion's size
        int bufsize = 1024;

        //send file
        byte[] tmpData = new byte[Math.min(bufsize, data.length)];
        int totalSent = 0;
        for (int i = 0; i<data.length; i+=Math.min(bufsize, (int)(data.length - i))) {
            for (int j=i; j<Math.min(i+bufsize, i+(int)(data.length - i)); j++) {
                tmpData[j-i] = data[j];
            }

            sock.getOutputStream().write(tmpData);
            totalSent += Math.min(bufsize, data.length - i);
            progressBar.setValue((int)(totalSent * 100 / data.length));
            System.out.println("Percent ready = " + (totalSent * 100 / data.length) + " %");
            progressBar.repaint();
        }*/
        int bufSize = 1024;
        byte[] tmpData = new byte[Math.min(bufSize, (int)file.length())];
        FileInputStream fis = new FileInputStream(file);
        int totalSent = 0;
        int read = 0;
        AppTimer timer = new AppTimer(time);
        timer.start();
        for (int i = 0; i < file.length(); i += Math.min(bufSize, (int)(file.length() - i))) {
            read = fis.read(tmpData);
            sock.getOutputStream().write(tmpData);
            totalSent += Math.min(bufSize, file.length() - i);
            double dtotalSent = totalSent / file.length() * 100;
            progressBar.setValue((int)(dtotalSent));
            long ltotalSent = totalSent * 100;
            System.out.println("Percent ready = " + dtotalSent + " %");
            progressBar.repaint();

        }
        timer.stop();
        fis.close();

    }

    public static void answerForRequest(Socket sock, boolean banswer) throws IOException {
        //byte[] ansBuf = {'a', 'n', 's'};
        byte[] answerBuf = new byte[1];
        String sanswer = banswer ? "y" : "n";

        System.out.println(answerBuf[0]);
        //sock.getOutputStream().write(ansBuf);
        sock.getOutputStream().write(sanswer.getBytes());
    }

    public static void processRequest(Socket sock, List<String> fileNames, List<Integer> fileSizes) throws IOException {
        byte[] fileCountBuf = new byte[Integer.BYTES];
        byte[] fileSizeBuf = new byte[Long.BYTES];
        System.out.println("before reading file count");
        sock.getInputStream().read(fileCountBuf);
        int fileCount = ByteBuffer.wrap(fileCountBuf).getInt();
        System.out.println(fileCount);
        for (int i = 0; i < fileCount; i++) {
            byte[] fileNameLengthBuf = new byte[Integer.BYTES];
            int read = sock.getInputStream().read(fileNameLengthBuf);
            byte[] fileNameBuf = new byte[ByteBuffer.wrap(fileNameLengthBuf).getInt()];
            read = sock.getInputStream().read(fileNameBuf);
            System.out.println(read);
            String fileName = new String(fileNameBuf, 0, read);
            fileNames.add(fileName);
            System.out.println("filename = " + fileName);
            read = sock.getInputStream().read(fileSizeBuf);
            System.out.println("size = " + ByteBuffer.wrap(fileSizeBuf).getLong());
            fileSizes.add((int)ByteBuffer.wrap(fileSizeBuf).getLong());

            sock.getOutputStream().write("f".getBytes());
        }
        System.out.println("All file names and sizes are received");
    }

    public static void receiveFile (Socket sock, String fileName, long fileSize, String saveFolder, JProgressBar progressBar, JLabel time) throws IOException{
        System.out.println("Receiving");

        //receive name length
        //receive name

        //receive size

        //receive file
        byte[] fileBuf = new byte[1024];
        int read = 0;
        FileOutputStream fos = new FileOutputStream(saveFolder + "/" + fileName);
        int totalRead = 0;
        AppTimer timer = new AppTimer(time);
        timer.start();
        for (; totalRead < fileSize; totalRead += Math.min(read, (int) fileSize - totalRead)) {
            //use Math.min because in the last iteration we dont get correct count of bytes, we get 100 bytes somewhy
            read = sock.getInputStream().read(fileBuf);
            read = Math.min(read, (int) fileSize - totalRead);
            fos.write(fileBuf, 0, read);
            //totalRead += Math.min(read, (int) fileSize - totalRead);
            long ltotalRead = totalRead * 100;
            long maxVal = Long.MAX_VALUE;
            int totalReadPercent = (int)(ltotalRead / fileSize);
            if (ltotalRead > fileSize) {
                System.out.println("dtotalRead = " + ltotalRead);
            }

            //BigDecimal totalReadPercent = dtotalRead.divide(new BigDecimal(fileSize), 5, BigDecimal.ROUND_CEILING);
            System.out.println("read = " + read + ", totalRead = " + totalReadPercent);

            progressBar.setValue((int)totalReadPercent);
            progressBar.repaint();
            System.out.println("Percent ready = " + totalReadPercent + " %");
        }
        timer.stop();
        progressBar.setValue(100);
        progressBar.repaint();
        fos.close();
        System.out.println("File is received!");
    }
}
