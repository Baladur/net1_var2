package com.ipovselite;

import javax.swing.*;

/**
 * Created by roman on 25.10.2016.
 */
public class AppTimer {
    private JLabel label;
    private Thread t;
    private long secondsElapsed;
    boolean isStopped = true;

    public AppTimer(JLabel pLabel) {
        label = pLabel;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!t.isInterrupted()) {
                        Thread.sleep(1000);
                        if (!isStopped) {
                            secondsElapsed++;
                            label.setText(getTime());
                            label.repaint();
                        } else {
                            break;
                        }
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }


            }
        });
        secondsElapsed = 0;
    }

    public void start() {
        isStopped = false;
        t.start();
    }

    public void stop() {
        try {
            isStopped = true;
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getTime() {
        long seconds = secondsElapsed;
        int mins = (int) seconds / 60;
        int hours = (int) mins / 60;
        String time = "";
        if (hours < 10) {
            time += "0";
        }
        time += hours + ":";
        if (mins < 10) {
            time += "0";
        }
        time += mins + ":";
        if (seconds < 10) {
            time += "0";
        }
        time += seconds;

        return time;
    }
}
