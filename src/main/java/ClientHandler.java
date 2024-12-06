import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private int clientId;
    private Socket clientSocket;
    private Server server;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private boolean running = false;

    // Game related fields:
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
                PokerInfo pokerInfo = (PokerInfo) input.readObject();
                processClientRequest(pokerInfo);
            }
        } catch (Exception e) {
            server.updateGameState("Client " + clientId + " error: " + e.getMessage());
        } finally {
            stopClient();
        }
    }

    private void processClientRequest(PokerInfo info) {
        try {
            switch (info.getAction()) {
                case "PLACE_BETS":
                    player.setAnteBet(info.getAnteBet());
                    player.setPairPlusBet(info.getPairPlusBet());
                    // Acknowledge bets
                    PokerInfo response = new PokerInfo();
                    response.setAction("BETS_ACCEPTED");
                    output.writeObject(response);
                    output.flush();
                    server.updateGameState("Client " + clientId + " placed bets: Ante $" + info.getAnteBet() + ", Pair Plus $" + info.getPairPlusBet());
                    break;

                case "REQUEST_CARDS":
                    dealer.reset();
                    dealer.deal(player, null);
                    PokerInfo dealInfo = new PokerInfo();
                    dealInfo.setAction("CARDS_DEALT");
                    dealInfo.setPlayerHand(player.getHand());
                    dealInfo.setDealerHand(dealer.getHand()); // hidden on client side for now, client shows backs
                    output.writeObject(dealInfo);
                    output.flush();
                    server.updateGameState("Client " + clientId + " cards dealt.");
                    break;

                case "PLAY_DECISION":
                    boolean play = info.isPlay();
                    server.updateGameState("Client " + clientId + " decided to " + (play ? "Play" : "Fold"));
                    if (!play) {
                        // Player folds
                        // Round result: player loses ante and pair plus bets
                        int totalLoss = player.getAnteBet() + player.getPairPlusBet();
                        player.subtractWinnings(totalLoss);

                        PokerInfo foldResult = new PokerInfo();
                        foldResult.setAction("ROUND_RESULT");
                        foldResult.setTotalWinnings(player.getWinnings());
                        foldResult.setResultMessage("You folded. You lost $" + totalLoss);
                        output.writeObject(foldResult);
                        output.flush();
                    } else {
                        // Player plays, reveal dealer cards
                        PokerInfo showDealer = new PokerInfo();
                        showDealer.setAction("SHOW_DEALER");
                        showDealer.setPlayerHand(player.getHand());
                        showDealer.setDealerHand(dealer.getHand());
                        output.writeObject(showDealer);
                        output.flush();

                        // Compare hands and evaluate results
                        int result = ThreeCardLogic.compareHands(dealer.getHand(), player.getHand());
                        int pairPlusWinnings = ThreeCardLogic.evalPPWinnings(player.getHand(), player.getPairPlusBet());
                        int totalWinnings = pairPlusWinnings;

                        if (ThreeCardLogic.dealerQualifies(dealer.getHand())) {
                            if (result == 2) {
                                // Player wins
                                totalWinnings += player.getAnteBet() * 2; // ante+play win
                                String resMsg = "You win against the dealer! You won $" + totalWinnings;
                                player.addWinnings(totalWinnings);
                                sendRoundResult(resMsg, player.getWinnings());
                            } else if (result == 1) {
                                // Dealer wins
                                totalWinnings -= player.getAnteBet() * 2; // lose ante and play
                                player.addWinnings(totalWinnings);
                                sendRoundResult("You lose to the dealer. You lost $" + (-totalWinnings), player.getWinnings());
                            } else {
                                // Tie
                                totalWinnings += player.getAnteBet(); // return ante
                                player.addWinnings(totalWinnings);
                                sendRoundResult("It's a tie. You get your ante back. You won $" + totalWinnings, player.getWinnings());
                            }
                        } else {
                            // Dealer does not qualify
                            totalWinnings += player.getAnteBet() * 2; // Ante and Play returned as a win
                            player.addWinnings(totalWinnings);
                            sendRoundResult("Dealer does not qualify. You won your ante. Total winning this round: $" + totalWinnings, player.getWinnings());
                        }

                        server.updateGameState("Client " + clientId + " round completed.");
                    }
                    break;

                default:
                    server.updateGameState("Unknown action from client " + clientId + ": " + info.getAction());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRoundResult(String message, int totalWinnings) throws Exception {
        PokerInfo resultInfo = new PokerInfo();
        resultInfo.setAction("ROUND_RESULT");
        resultInfo.setTotalWinnings(totalWinnings);
        resultInfo.setResultMessage(message);
        output.writeObject(resultInfo);
        output.flush();
    }

    public void stopClient() {
        running = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            server.removeClient(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
