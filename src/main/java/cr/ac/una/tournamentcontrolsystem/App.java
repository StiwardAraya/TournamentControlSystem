package cr.ac.una.tournamentcontrolsystem;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goViewInWindow("MainView");
    }

    public static void main(String[] args) {
        launch();
    }

}
