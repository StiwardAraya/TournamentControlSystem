package cr.ac.una.tournamentcontrolsystem;

import cr.ac.una.tournamentcontrolsystem.util.FlowController;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Clase app de JavaFX
 *
 * @author Stiward Araya C.
 * @author Angie Marks S.
 * @author Kevin Calderón Z.
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
