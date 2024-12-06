import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private boolean running = false;
    private serverGameStateController gameStateController;
    private ConcurrentHashMap<Integer, ClientHandler> clients = new ConcurrentHashMap<>();
    private int clientIdCounter = 1;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            updateGameState("Server started on port " + port);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                int clientId = clientIdCounter++;
                ClientHandler clientHandler = new ClientHandler(clientId, clientSocket, this);
                clients.put(clientId, clientHandler);
                new Thread(clientHandler).start();
                updateGameState("Client " + clientId + " connected.");
                updateConnectedClientsCount(clients.size());
            }
        } catch (IOException e) {
            if (running) {
                updateGameState("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (ClientHandler client : clients.values()) {
                client.stopClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(int clientId) {
        clients.remove(clientId);
        updateGameState("Client " + clientId + " disconnected.");
        updateConnectedClientsCount(clients.size());
    }

    public void setGameStateController(serverGameStateController controller) {
        this.gameStateController = controller;
    }

    public void updateGameState(String message) {
        if (gameStateController != null) {
            gameStateController.updateGameState(message);
        }
    }

    public void updateConnectedClientsCount(int count) {
        if (gameStateController != null) {
            gameStateController.updateConnectedClientsCount(count);
        }
    }
}
