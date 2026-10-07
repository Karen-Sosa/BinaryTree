package co.edu.uptc.presentation;

import co.edu.uptc.business.Tree;
import co.edu.uptc.data.Reader;

public class Main {
    public static void main(String[] args) {
        Console console = new Console(new Tree(), new Reader());
        console.start();
    }
}