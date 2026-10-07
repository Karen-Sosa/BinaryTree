package co.edu.uptc.presentation;

import co.edu.uptc.business.Manager;
import co.edu.uptc.business.Tree;
import co.edu.uptc.business.TreeNode;
import co.edu.uptc.data.Reader;
import co.edu.uptc.list.DoublyLinked;
import co.edu.uptc.list.ListNode;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {

    private final Scanner sc;
    private final Tree tree;
    private final Manager game;
    private final Reader reader;

    public Console(Tree tree, Reader reader) {
        this.sc = new Scanner(System.in);
        this.tree = tree;
        this.reader = reader;
        this.game = new Manager(tree);
    }

    public void start() {
        loadTree();
        try {
            menu();
        } catch (NoSuchElementException e) {
            showMessage("\nEntrada finalizada.");
        }
        saveTree();
    }

    public void menu() {
        int option;
        do {
            showMenu();
            option = readInt();
            handleOption(option);
        } while (option != 0);
    }

    private void showMenu() {
        showMessage("\nBienvenido a Adivina tu personaje\n"
                + "OPCIONES--->\n"
                + "1. Iniciar sesión de juego\n"
                + "2. Mostrar el árbol\n"
                + "3. Mostrar la cantidad de soluciones e interrogantes\n"
                + "4. Reiniciar a la estructura inicial\n"
                + "0. Salir");
    }

    private void handleOption(int option) {
        switch (option) {
            case 1:
                playGame();
                break;
            case 2:
                showTree();
                break;
            case 3:
                showStatistics();
                break;
            case 4:
                resetTree();
                break;
            case 0:
                showMessage("Te veo luego :)");
                break;
            default:
                showMessage("Opción inválida. Intente nuevamente.");
        }
    }

    private void playGame() {
        if (tree.isEmpty()) {
            showMessage("No existe el juego.");
            return;
        }
        showMessage("\nPiensa en un personaje.");
        game.start();
        while (!game.isCharacter()) {
            showMessage("\n" + game.getCurrent().getData());
            game.answer(readYesNo());
        }
        guess();
    }

    private void guess() {
        showMessage("\n¿Es un " + game.getCurrent().getData() + "?");
        if (readYesNo()) {
            showMessage("\n¡Lo adiviné!");
        } else {
            learn();
        }
    }

    private void learn() {
        TreeNode wrong = game.getCurrent();
        showMessage("\nNo logré adivinar.");
        String character = readValidText("¿En qué personaje estabas pensando? ");
        String question = readValidText("¿Qué pregunta distingue a " + character
                + " de " + wrong.getData() + "? ");
        showMessage("Para " + character + ", ¿la respuesta es Sí o No?");
        game.learn(character, question, readYesNo());
        showSection("¡He aprendido un personaje nuevo! Árbol actualizado", tree.draw());
        saveTree();
    }

    private void showStatistics() {
        int characters = tree.countCharacters();
        int questions = tree.countQuestions();

        showMessage(
                "\n--- ESTADÍSTICAS DEL ÁRBOL ---\n" +
                "Soluciones/personajes: " + characters + "\n" +
                "Interrogantes/preguntas: " + questions
        );
    }

    private void showTree() {
        if (tree.isEmpty()) {
            showMessage("El árbol está vacío.");
            return;
        }
        showSection("ÁRBOL", tree.draw());
        showSection("RECORRIDO PREORDEN", tree.preOrder());
        showSection("RECORRIDO INORDEN", tree.inOrder());
        showSection("RECORRIDO POSTORDEN", tree.postOrder());
    }

    private void resetTree() {
        showMessage("Se perderá lo aprendido. ¿Desea continuar?");
        if (!readYesNo()) {
            return;
        }
        try {
            tree.load(reader.reset());
            showMessage("El árbol volvió a su estado inicial.");
        } catch (IllegalArgumentException | UncheckedIOException e) {
            showMessage("No se pudo restablecer el árbol: " + e.getMessage());
        }
    }

    private void loadTree() {
        try {
            tree.load(reader.read());
        } catch (IllegalArgumentException | UncheckedIOException e) {
            showMessage("Archivo del árbol inválido (" + e.getMessage() + "). Se restaurará el inicial.");
            resetFromDefault();
        }
    }

    private void resetFromDefault() {
        try {
            tree.load(reader.reset());
        } catch (IllegalArgumentException | UncheckedIOException e) {
            showMessage("No se pudo cargar el árbol inicial: " + e.getMessage());
        }
    }

    private void saveTree() {
        if (tree.isEmpty()) {
            return;
        }
        try {
            reader.write(tree.serialize());
        } catch (UncheckedIOException e) {
            showMessage("No se pudo guardar el árbol: " + e.getMessage());
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    private void showSection(String title, DoublyLinked<String> lines) {
        showMessage("\n--- " + title + " ---");
        for (ListNode<String> node = lines.getHead(); node != null; node = node.getNext()) {
            showMessage(node.getData());
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public int readInt() {
        try {
            return Integer.parseInt(readText("Opción: "));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readValidText(String prompt) {
        String text = readText(prompt);
        while (!game.isValidText(text)) {
            text = readText("Texto inválido (no vacío y sin comas). Intente de nuevo: ");
        }
        return text;
    }

    private boolean readYesNo() {
        String answer = readText("(Sí/No): ").toLowerCase();
        while (!isYes(answer) && !isNo(answer)) {
            answer = readText("Respuesta inválida. Escriba Sí o No: ").toLowerCase();
        }
        return isYes(answer);
    }

    private boolean isYes(String answer) {
        return List.of("s", "si", "sí").contains(answer);
    }

    private boolean isNo(String answer) {
        return List.of("n", "no").contains(answer);
    }
}