import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("V-H");

        initRootLayout();
        showInputOverview();

    }

    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            URL xmlUrl = getClass().getResource("fx/RootLayout.fxml");
            loader.setLocation(xmlUrl);
            //loader.setLocation(MainApp.class.getResource("fx/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showInputOverview() {
        try {
            // Загружаем сцену.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("fx/InputOverview.fxml"));
            AnchorPane inputOverview = (AnchorPane) loader.load();

            // Помещаем в центр корневого макета.
            this.rootLayout.setCenter(inputOverview);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

}
