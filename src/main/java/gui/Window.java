package gui;

import crawler.CrawlerTree;
import crawler.ProbabilityCalculator;
import crawler.TreeReader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import t9.T9Tree;
import utility.TimeUnit;
import utility.Timer;

import java.io.IOException;
import java.net.URL;

/**
 * Created by lostincoding on 13.06.17.
 */
public class Window extends Application {
    private T9Tree tree;
    // ui components
    private Stage primaryStage;
    private TextField bestguessText;
    private TextField buttonsPressed;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("T9 Erkenner");

        initTree();
        initRootLayout();
    }



    private void initRootLayout() {
        try {
            //load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            URL url = Window.class.getResource("t9keyboard.fxml");
            loader.setLocation(url);
            SplitPane rootLayout = loader.load();

            //show scene
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            //load ui components for programmatic access
            GridPane textgrid = (GridPane) ((AnchorPane) rootLayout.getItems().get(0)).getChildren().get(0);
            GridPane keyboardgrid = (GridPane) ((AnchorPane) rootLayout.getItems().get(1)).getChildren().get(0);

            bestguessText = (TextField) textgrid.getChildren().get(3);
            buttonsPressed = (TextField) textgrid.getChildren().get(2);
            Button newword=(Button) textgrid.getChildren().get(4);


            //set action for "new word" button
            newword.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    tree.newWord();
                    bestguessText.setText("");
                    buttonsPressed.setText("");
                }
            });
            /*
            * adds a eventhandler for each button which gets the text of the button a forwards it to the tree
            * then it displays the best guess
            * and displays which buttons were pressed
            */
            keyboardgrid.getChildren().forEach(node -> ((Button) node).setOnAction(event -> {
                char buttonchar = ((Button) event.getSource()).getText().charAt(0);
                tree.processButton(buttonchar);
                bestguessText.setText(tree.getBestGuess());
                buttonsPressed.setText(buttonsPressed.getText() + buttonchar);
            }));



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        launch(args);
    }

    private void initTree() throws IOException {
        TreeReader reader = new TreeReader();
        Timer timer=new Timer();

        System.out.println("Load persisted Tree");
        timer.start();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        timer.stop();
        System.out.print("Loading the persisted tree took ");
        timer.printTime(TimeUnit.MILLISECONDS);
        System.out.print(" milliseconds.\n");

        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(parseTree);

        tree = new T9Tree(probabilityCalculator, 2);
    }

}
