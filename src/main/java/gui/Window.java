package gui;

import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import javafx.application.Application;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import t9.T9Tree;

import java.io.IOException;
import java.util.Observable;
import java.util.function.Consumer;

/**
 * Created by lostincoding on 13.06.17.
 */
public class Window extends Application {
    private Stage primaryStage;
    private SplitPane rootLayout;
    private T9Tree tree;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("T9 Erkenner");
        initRootLayout();
        //load t9 tree

    }

    private void initRootLayout() {
        try {
            //load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Window.class.getResource("t9keyboard.fxml"));
            rootLayout = loader.load();

            //show scene
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

       GridPane keyboard= (GridPane) rootLayout.getItems().get(1);
       ObservableList<Node> buttons=keyboard.getChildren();


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
