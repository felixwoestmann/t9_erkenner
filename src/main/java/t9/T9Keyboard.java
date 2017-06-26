package t9;

import java.util.ArrayList;

/**
 * Created by lostincoding on 12.06.17.
 */
public class T9Keyboard {

    public static ArrayList<Character> mapStringToButtons(String input) {
        input = input.toLowerCase();
        ArrayList<Character> output = new ArrayList<>();
        for (char input_char : input.toCharArray()) {
            output.add(mapCharToButton(input_char));
        }

        return output;
    }

    public static char mapCharToButton(char c) {
        char buttons[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        for (char button : buttons) {
            if (mapButton(button).contains(c + "")) {
                return button;
            }
        }
        throw new RuntimeException("Char has no button equiv.");
    }

    public static ArrayList<String> mapButton(char button) throws IllegalArgumentException {

        ArrayList<String> list = new ArrayList<>();
        switch (button) {
            case '1':
                list.add("1");
                list.add(".");
                list.add(",");
                return list;
            case '2':
                list.add("2");
                list.add("a");
                list.add("b");
                list.add("c");
                return list;
            case '3':
                list.add("3");
                list.add("d");
                list.add("e");
                list.add("f");
                return list;
            case '4':
                list.add("4");
                list.add("g");
                list.add("h");
                list.add("i");
                return list;
            case '5':
                list.add("5");
                list.add("j");
                list.add("k");
                list.add("l");
                return list;
            case '6':
                list.add("6");
                list.add("m");
                list.add("n");
                list.add("o");
                return list;
            case '7':
                list.add("7");
                list.add("p");
                list.add("q");
                list.add("r");
                list.add("s");
                return list;
            case '8':
                list.add("8");
                list.add("t");
                list.add("u");
                list.add("v");
                return list;
            case '9':
                list.add("9");
                list.add("w");
                list.add("x");
                list.add("y");
                list.add("z");
                return list;
            case '0':
                list.add("0");
                list.add(" ");
                return list;
            case '*':
                return list;
            case '#':
                return list;


            default:
                throw new IllegalArgumentException("Zeichen ist nicht bekannt");
        }
    }
}
