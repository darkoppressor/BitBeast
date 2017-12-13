/* Copyright (c) 2017 Cheese and Bacon Games, LLC */
/* This file is licensed under the MIT License. */
/* See the file development/LICENSE.txt for the full license text. */

package org.cheeseandbacon.bitbeast;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;

public class BattleClientThread extends Thread {
    private static final String TAG = BattleClientThread.class.getName();

    private static final int CONNECTION_TIMEOUT = 1000;

    private Handler handler;
    private BlockingQueue<String> queue;
    private InetAddress serverInetAddress;
    private int serverPort;

    public BattleClientThread (Handler handler, BlockingQueue<String> queue, InetAddress serverInetAddress, int serverPort) {
        this.handler = handler;
        this.queue = queue;
        this.serverInetAddress = serverInetAddress;
        this.serverPort = serverPort;
    }

    @Override
    public void run () {
        Socket socket = new Socket();

        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(serverInetAddress.getHostAddress(), serverPort), CONNECTION_TIMEOUT);

            if (interrupted()) {
                closeSocket(socket);

                return;
            }

            Log.d(TAG, "Connection established to server socket: " + serverInetAddress.getHostAddress() + ":" + serverPort);

            new Thread(new BattleIoRunnable(socket, handler, queue)).start();
        } catch (NullPointerException e) {
            Log.e(TAG, "NullPointerException while connecting with socket", e);

            closeSocket(socket);

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();
        } catch (SocketTimeoutException e) {
            Log.e(TAG, "Timed out while connecting with socket", e);

            closeSocket(socket);

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();
        } catch (IOException e) {
            Log.e(TAG, "Error binding to or connecting with socket", e);

            closeSocket(socket);

            handler.obtainMessage(Activity_Battle_Menu_Wifi.MESSAGE_SOCKET_ERROR).sendToTarget();
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
