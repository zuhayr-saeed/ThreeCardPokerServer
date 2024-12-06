import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.Scene;

public class serverGameStateController {

    @FXML
    private ListView<String> gameStateListView;

    @FXML
    private Button stopServerButton;

    @FXML
    private Button changeThemeButton;

    @FXML
    private Label connectedClientsLabel;

    private Server server;
    private String[] themes = {"/styles/darkTheme.css", "/styles/lightTheme.css", "/styles/neonTheme.css"};
    private int currentThemeIndex = 0;

    public void setServer(Server server) {
        this.server = server;
        server.setGameStateController(this);
    }

    public void updateGameState(String message) {
        Platform.runLater(() -> gameStateListView.getItems().add(message));
    }

    public void updateConnectedClientsCount(int count) {
        Platform.runLater(() -> connectedClientsLabel.setText(String.valueOf(count)));
    }

    @FXML
    private void handleStopServer(ActionEvent event) {
        try {
            server.stopServer();
            stopServerButton.setDisable(true);
            updateGameState("Server stopped.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while stopping the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeTheme(ActionEvent event) {
        Scene scene = changeThemeButton.getScene();
        scene.getStylesheets().clear();
        currentThemeIndex = (currentThemeIndex + 1) % themes.length;
        scene.getStylesheets().add(getClass().getResource(themes[currentThemeIndex]).toExternalForm());
        updateGameState("Theme changed to: " + themes[currentThemeIndex].substring(themes[currentThemeIndex].lastIndexOf('/')+1));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
