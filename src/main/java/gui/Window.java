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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import t9.T9Keyboard;
import t9.T9Tree;
import utility.TimeUnit;
import utility.Timer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lostincoding on 13.06.17.
 */
public class Window extends Application {
    private T9Tree tree;
    // ui components
    private Stage primaryStage;
    private TextField bestguessText;
    private TextField buttonsPressed;
    private ArrayList<Character> buttonInput = new ArrayList<>();

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
            Button newword = (Button) textgrid.getChildren().get(4);


            //set action for "new word" button
            newword.setOnAction(e -> clearTreeAndStartOver());
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
                tree.printTree();
            }));
            buttonsPressed.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
                if (event.isConsumed()) return;
                if (buttonsPressed.getText().length() <= 0) return;
                buttonInput.clear();
                char[] chars = buttonsPressed.getText().toLowerCase().toCharArray();
                for (char c : chars) {
                    Character mapped = T9Keyboard.mapCharToButton(c);
                    if (mapped != c) {
                        c = mapped;
                    }
                    buttonInput.add(c);
                    System.out.println("Char " + c);
                }
                buttonsPressed.clear();
                buttonInput.forEach(c -> buttonsPressed.appendText(c + ""));
                tree.newWord();
                buttonInput.forEach(tree::processButton);
                bestguessText.setText(tree.getBestGuess());
                tree.printTree();
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearTreeAndStartOver() {
        tree.newWord();
        bestguessText.setText("");
        buttonsPressed.setText("");
    }

    public static void main(String args[]) {
        launch(args);
    }

    private void initTree() throws IOException {
        TreeReader reader = new TreeReader();
        Timer timer = new Timer();

        System.out.println("Load persisted Tree");
        timer.start();
        CrawlerTree parseTree = reader.getTreeFromFile("tree_5.json");
        timer.stop();
        System.out.print("Loading the persisted tree took ");
        timer.printTime(TimeUnit.MILLISECONDS);
        System.out.print(" milliseconds.\n");

        ProbabilityCalculator probabilityCalculator = new ProbabilityCalculator(parseTree);

        tree = new T9Tree(probabilityCalculator, 5);
    }

}
