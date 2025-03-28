package fr.kerriansalaun.insight.service;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RobotControlService {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private static final int CONNECTION_TIMEOUT = 5000;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected = false;

    public interface CommandCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public void connect(CommandCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            throw new RuntimeException("Erreur lors de la fermeture du socket existant", e);
                        }
                    }
                    
                    socket = new Socket();
                    socket.connect(new java.net.InetSocketAddress(SERVER_IP, SERVER_PORT), CONNECTION_TIMEOUT);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    return null;
                } catch (IOException e) {
                    return e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String error) {
                if (error == null) {
                    isConnected = true;
                    callback.onResponse("Connected");
                } else {
                    isConnected = false;
                    callback.onError("Connection failed: " + error);
                }
            }
        }.execute();
    }

    public void sendCommand(String command, CommandCallback callback) {
        if (!isConnected) {
            callback.onError("Not connected");
            return;
        }

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    out.println(params[0]);
                    String response = in.readLine();
                    return response;
                } catch (IOException e) {
                    throw new RuntimeException("Erreur lors de l'envoi de la commande", e);
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    callback.onResponse(response);
                } else {
                    isConnected = false; 
                    callback.onError("Command failed");
                }
            }
        }.execute(command);
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la fermeture de la ressource", e);
            }
        }
    }

    public void disconnect() {
        closeQuietly(in);
        closeQuietly(out);
        closeQuietly(socket);
        isConnected = false;
    }
} 