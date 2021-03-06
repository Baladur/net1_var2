package com.ipovselite;

import org.omg.IOP.IOR;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.util.List;

public class Client {
    private Socket socket;
    private String downloadDir;

    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public Client(Socket socket) {
        this.socket = socket;
    }

    public Client(Socket socket, String downloadDir) {
        this.socket = socket;
        this.downloadDir = downloadDir;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public void sendRequest(List<File> files) throws IOException, AppException {
        Protocol.sendRequest(this.socket, files);
    }

    public void sendFiles(List<File> files, List<JProgressBar> progressBars, List<JLabel> times) throws IOException {
        for (int i = 0; i < files.size(); i++) {
            Protocol.sendFile(this.socket, files.get(i), progressBars.get(i), times.get(i));
        }
    }

    public void receiveFiles(List<String> fileNames, List<Integer> fileSizes, List<JProgressBar> progressBars, List<JLabel> times) throws IOException {
        for (int i = 0; i < progressBars.size(); i++) {
            Protocol.receiveFile(this.socket, fileNames.get(i), fileSizes.get(i), downloadDir, progressBars.get(i), times.get(i));
        }
    }

    public void processRequest(List<String> fileNames, List<Integer> fileSizes) throws Exception, IOException {
        byte[] req = new byte[3];
        socket.getInputStream().read(req);
        if (req[0] != 'r' && req[1] != 'e' && req[2] != 'q') {
            throw new Exception("protocol");
        }
        System.out.println("Received req");

        Protocol.processRequest(this.socket, fileNames, fileSizes);
    }

    public void answerForRequest(boolean answer) throws IOException {
        Protocol.answerForRequest(this.socket, answer);
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}
