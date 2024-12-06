// ClientHandler.java
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private int clientId;
    private Socket clientSocket;
    private Server server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean running = false;

    // Game-related fields
    private Player player;
    private Dealer dealer;

    public ClientHandler(int clientId, Socket clientSocket, Server server) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.server = server;
        this.player = new Player();
        this.dealer = new Dealer();
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
            running = true;

            while (running) {
                // Read PokerInfo from client
                PokerInfo pokerInfo = (PokerInfo) input.readObject();
                processClientRequest(pokerInfo);
            }
        } catch (Exception e) {
            server.updateGameState("Client " + clientId + " error: " + e.getMessage());
        } finally {
            stopClient();
        }
    }

    /**
     * Processes the client's request and responds accordingly.
     *
     * @param pokerInfo The PokerInfo object received from the client.
     */
    private void processClientRequest(PokerInfo pokerInfo) {
        try {
            switch (pokerInfo.getAction()) {
                case "PLACE_BETS":
                    player.setAnteBet(pokerInfo.getAnteBet());
                    player.setPairPlusBet(pokerInfo.getPairPlusBet());
                    server.updateGameState("Client " + clientId + " placed bets: Ante $" + pokerInfo.getAnteBet() + ", Pair Plus $" + pokerInfo.getPairPlusBet());

                    // Deal cards
                    dealer.deal(player, null);
                    pokerInfo.setPlayerHand(player.getHand());
                    pokerInfo.setDealerHand(dealer.getHand()); // Dealer's hand sent as hidden
                    pokerInfo.setAction("CARDS_DEALT");
                    output.writeObject(pokerInfo);
                    output.flush();
                    break;

                case "PLAY_DECISION":
                    boolean play = pokerInfo.isPlay();
                    server.updateGameState("Client " + clientId + " decided to " + (play ? "Play" : "Fold"));

                    if (!play) {
                        // Player folds; calculate losses
                        int totalLoss = player.getAnteBet() + player.getPairPlusBet();
                        player.subtractWinnings(totalLoss);
                        pokerInfo.setTotalWinnings(player.getWinnings());
                        pokerInfo.setAction("ROUND_RESULT");
                        pokerInfo.setResultMessage("You folded. You lost $" + totalLoss);
                        output.writeObject(pokerInfo);
                        output.flush();
                    } else {
                        // Player plays; reveal dealer's hand and evaluate
                        pokerInfo.setDealerHand(dealer.getHand());
                        int result = ThreeCardLogic.compareHands(dealer.getHand(), player.getHand());
                        int winnings = calculateWinnings(result);
                        player.addWinnings(winnings);
                        pokerInfo.setTotalWinnings(player.getWinnings());
                        pokerInfo.setAction("ROUND_RESULT");
                        pokerInfo.setResultMessage(generateResultMessage(result, winnings));
                        output.writeObject(pokerInfo);
                        output.flush();

                        server.updateGameState("Client " + clientId + " result: " + pokerInfo.getResultMessage());
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the player's winnings based on the game result.
     *
     * @param result The result of the hand comparison.
     * @return The total winnings or losses.
     */
    private int calculateWinnings(int result) {
        int totalWinnings = 0;

        // Evaluate Pair Plus winnings
        int ppWinnings = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlusBet());
        totalWinnings += ppWinnings;

        // Evaluate Ante and Play wagers
        if (result == 2) { // Player wins
            totalWinnings += player.getAnteBet() * 2; // Ante and Play bets pay 1:1
        } else if (result == 0) { // Tie
            totalWinnings += player.getAnteBet(); // Return Ante bet
        } else {
            totalWinnings -= player.getAnteBet() * 2; // Lose Ante and Play bets
        }

        return totalWinnings;
    }

    /**
     * Generates a result message based on the game outcome.
     *
     * @param result        The result of the hand comparison.
     * @param totalWinnings The total winnings or losses.
     * @return The result message to display to the player.
     */
    private String generateResultMessage(int result, int totalWinnings) {
        StringBuilder message = new StringBuilder();
        if (result == 2) {
            message.append("You win against the dealer! ");
        } else if (result == 1) {
            message.append("You lose to the dealer. ");
        } else {
            message.append("It's a tie with the dealer. ");
        }

        if (totalWinnings >= 0) {
            message.append("You won $" + totalWinnings);
        } else {
            message.append("You lost $" + (-totalWinnings));
        }
        return message.toString();
    }

    /**
     * Stops the client handler and closes resources.
     */
    public void stopClient() {
        running = false;
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            server.removeClient(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
