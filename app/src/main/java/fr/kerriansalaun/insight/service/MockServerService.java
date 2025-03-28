package fr.kerriansalaun.insight.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class MockServerService extends Service {
    private static final int PORT = 12345;
    private static final int BACKLOG = 50;
    private static final String LOCAL_HOST = "127.0.0.1";

    private ServerSocket serverSocket;
    private boolean isRunning = false;
    private Thread serverThread;

    private final Map<String, String> COMMAND_RESPONSES = Map.of(
        "START", "Motors started",
        "STOP", "Motors stopped",
        "DIRECT_LEFT", "Turning left",
        "DIRECT_RIGHT", "Turning right",
        "DIRECT_FRONT", "Moving forward"
    );

    @Override
    public void onCreate() {
        super.onCreate();
        startMockServer();
    }

    private void startMockServer() {
        serverThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(PORT, BACKLOG, java.net.InetAddress.getByName(LOCAL_HOST));
                isRunning = true;

                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new Thread(() -> handleClient(clientSocket)).start();
                    } catch (IOException e) {
                        if (isRunning) {
                            throw new RuntimeException("Erreur lors de l'acceptation de la connexion client", e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du démarrage du serveur mock", e);
            }
        });
        serverThread.start();
    }

    private void handleClient(Socket clientSocket) {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null && isRunning) {
                String response = processCommand(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la gestion du client", e);
        } finally {
            closeClientResources(clientSocket, in, out);
        }
    }

    private String processCommand(String command) {
        return COMMAND_RESPONSES.getOrDefault(command, "Unknown command");
    }

    private void closeClientResources(Socket clientSocket, BufferedReader in, PrintWriter out) {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la fermeture des ressources client", e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la fermeture du serveur", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
} 