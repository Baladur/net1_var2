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

    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
    }

    public Client(Socket socket) {
        this.socket = socket;
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public boolean sendRequest(List<File> files) throws IOException {
        return Protocol.sendRequest(this.socket, files);
    }

    public void sendFiles(List<File> files, List<JProgressBar> progressBars) throws IOException {
        for (int i = 0; i < files.size(); i++) {
            Protocol.sendFile(this.socket, files.get(i), progressBars.get(i));
        }
    }

    public List<String> processRequest() throws Exception, IOException {
        byte[] req = new byte[3];
        socket.getInputStream().read(req);
        if (req[0] != 'r' && req[1] != 'e' && req[2] != 'q') {
            throw new Exception("protocol");
        }

        return Protocol.processRequest(this.socket);
    }

    public void answerForRequest(boolean answer) throws IOException {
        Protocol.answerForRequest(this.socket, answer);
    }
}
