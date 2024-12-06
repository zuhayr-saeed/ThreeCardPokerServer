import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class serverIntroController {

    @FXML private TextField portField;
    @FXML private Button startButton;

    @FXML
    private void handleStartServer(ActionEvent event) {
        try {
            int port = Integer.parseInt(portField.getText().trim());

            Server server = new Server(port);
            new Thread(server).start();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/serverGameState.fxml"));
            Parent root = loader.load();

            serverGameStateController controller = loader.getController();
            controller.setServer(server);

            Scene scene = new Scene(root, 1500, 750);
            scene.getStylesheets().add(getClass().getResource("/styles/darkTheme.css").toExternalForm());

            Stage stage = (Stage) startButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Three Card Poker Server - Game State");
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid port number.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while starting the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
