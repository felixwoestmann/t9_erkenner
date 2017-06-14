package gui;

import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import t9.T9Tree;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Created by lostincoding on 13.06.17.
 */
public class Window extends Application {
    private T9Tree tree;
    // ui components
    private Stage primaryStage;
    private SplitPane rootLayout;
    GridPane textgrid;
    GridPane keyboardgrid;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("T9 Erkenner");
        initRootLayout();
        //load t9 tree
        initTree();
    }

    private void initRootLayout() {
        try {
            //load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            URL url = Window.class.getResource("t9keyboard.fxml");
            loader.setLocation(url);
            rootLayout = loader.load();

            //show scene
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            //load ui components for programmatic access
            textgrid  = (GridPane) ((AnchorPane) rootLayout.getItems().get(0)).getChildren().get(0);
            keyboardgrid  = (GridPane) ((AnchorPane) rootLayout.getItems().get(1)).getChildren().get(0);

            //adds a eventhandler for each button which gets the text of the button a forwards it to the tree
            keyboardgrid.getChildren().forEach(node -> ((Button) node).setOnAction(event -> tree.processButton(((Button)event.getSource()).getText().charAt(0))));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

    private void initTree() throws IOException {
        TreeReader reader = new TreeReader();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(parseTree);

        tree = new T9Tree(probabilityCalculator, 2);
    }

}
