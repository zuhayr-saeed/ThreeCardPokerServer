// Server.java
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
            }
        } catch (IOException e) {
            if (running) {
                updateGameState("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the server and closes all client connections.
     */
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

    /**
     * Removes a client from the active clients map.
     *
     * @param clientId The ID of the client to remove.
     */
    public void removeClient(int clientId) {
        clients.remove(clientId);
        updateGameState("Client " + clientId + " disconnected.");
    }

    /**
     * Sets the game state controller for GUI updates.
     *
     * @param controller The server game state controller.
     */
    public void setGameStateController(serverGameStateController controller) {
        this.gameStateController = controller;
    }

    /**
     * Updates the game state on the GUI.
     *
     * @param message The message to display.
     */
    public void updateGameState(String message) {
        if (gameStateController != null) {
            gameStateController.updateGameState(message);
        }
    }
}
