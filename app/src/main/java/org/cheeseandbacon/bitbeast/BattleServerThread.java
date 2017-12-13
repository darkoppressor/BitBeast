/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BattleServerThread extends Thread {
    private static final String TAG = BattleServerThread.class.getName();

    private static final int THREAD_COUNT = 10;
    private static final long THREAD_KEEP_ALIVE_TIME = 10;

    private ServerSocket serverSocket;
    private Handler handler;
    private BlockingQueue<String> queue;

    public BattleServerThread (Handler handler, BlockingQueue<String> queue) {
        this.handler = handler;
        this.queue = queue;

        try {
            serverSocket = new ServerSocket(Activity_Battle_Menu_Wifi.SERVER_PORT);
        } catch (IOException e) {
            Log.e(TAG, "Failed to open a server socket");

            pool.shutdownNow();

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();
        }
    }

    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, THREAD_KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    @Override
    public void run () {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                if (interrupted()) {
                    closeSocket(clientSocket);
                    closeServerSocket();

                    pool.shutdownNow();

                    return;
                }

                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();

                pool.execute(new BattleIoRunnable(clientSocket, handler, queue));

                Log.d(TAG, "Connection established to client socket: " + clientAddress + ":" + clientPort);
            } catch (NullPointerException e) {
                closeServerSocket();

                Log.e(TAG, "NullPointerException while accepting client socket connection", e);

                pool.shutdownNow();

                handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();

                break;
            } catch (IOException e) {
                closeServerSocket();

                Log.e(TAG, "Error executing runnable", e);

                pool.shutdownNow();

                handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();

                break;
            }
        }
    }

    private void closeServerSocket () {
        if (serverSocket != null) {
            if (!serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }

            serverSocket = null;
        }
    }

    private void closeSocket (Socket socket) {
        if (socket != null) {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
