package co.edu.uptc.business;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Manager {

    private final Tree tree;
    private TreeNode current;

    public void start() {
        if (tree.isEmpty()) {
            throw new IllegalStateException("No existe el juego.");
        }
        current = tree.getRoot();
    }

    public boolean isCharacter() {
        return current.isLeaf();
    }

    public void answer(boolean yes) {
        if (isCharacter()) {
            throw new IllegalStateException("Ya se llegó a un personaje.");
        }
        current = yes ? current.getLeft() : current.getRight();
    }

    public boolean isValidText(String text) {
        return text != null && !text.isBlank() && !text.contains(",");
    }

    public void learn(String character, String question, boolean answerForNew) {
        if (!isCharacter() || !isValidText(character) || !isValidText(question)) {
            throw new IllegalArgumentException("Datos inválidos para aprender.");
        }
        TreeNode newCharacter = new TreeNode(character.trim());
        TreeNode newQuestion = new TreeNode(question.trim());
        newQuestion.setLeft(answerForNew ? newCharacter : current);
        newQuestion.setRight(answerForNew ? current : newCharacter);
        tree.replaceNode(current, newQuestion);
        current = newQuestion;
    }
}
