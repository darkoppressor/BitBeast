package org.cheeseandbacon.bitbeast;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class BattleIoRunnable implements Runnable {
    private static final String TAG = BattleIoRunnable.class.getName();

    private Socket socket;
    private Handler handler;
    private BlockingQueue<String> queue;
    private OutputStream outputStream;

    public BattleIoRunnable(Socket socket, Handler handler, BlockingQueue<String> queue) {
        this.socket = socket;
        this.handler = handler;
        this.queue = queue;
    }

    @Override
    public void run () {
        try {
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_RUNNABLE_READY).sendToTarget();

            String rawData = "";

            while (!socket.isClosed() && socket.isConnected()) {
                checkQueue();

                int availableBytes;
                boolean endOfStream = false;
                boolean endOfMessage = false;
                while (!endOfStream && !endOfMessage && (availableBytes = inputStream.available()) > 0) {
                    for (int i = 0; i < availableBytes; i++) {
                        int nextByte = inputStream.read();

                        if (nextByte != -1) {
                            rawData += (char) nextByte;

                            if ((char) nextByte == BattleIo.END_OF_MESSAGE) {
                                endOfMessage = true;
                                break;
                            }
                        } else {
                            endOfStream = true;
                            break;
                        }
                    }
                }

                if (endOfStream) {
                    break;
                }

                if (rawData.length() > 0 && rawData.charAt(rawData.length() - 1) == BattleIo.END_OF_MESSAGE) {
                    handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_READ, rawData.getBytes().length, -1, rawData.getBytes()).sendToTarget();

                    rawData = "";
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading from input stream", e);

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_RUNNABLE_ERROR).sendToTarget();
        } finally {
            closeSocket();
        }
    }

    private void closeSocket () {
        if (socket != null) {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }

            socket = null;
        }
    }

    private void write (byte[] buffer) {
        try {
            outputStream.write(buffer);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to output stream", e);

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_RUNNABLE_ERROR).sendToTarget();
        }
    }

    private void checkQueue () {
        String data;
        while ((data = queue.poll()) != null) {
            Log.d(TAG, "Write request received");

            write(data.getBytes());
        }
    }
}
