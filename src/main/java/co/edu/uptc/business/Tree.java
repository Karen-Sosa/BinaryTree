package co.edu.uptc.business;

import co.edu.uptc.list.DoublyLinked;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tree {
    private static final String QUESTION = "PREGUNTA";
    private static final String CHARACTER = "PERSONAJE";

    private TreeNode root;

    public Tree() {
        root = null;
    }

    public DoublyLinked<String> preOrder() {
        DoublyLinked<String> result = new DoublyLinked<>();
        preOrder(root, result);
        return result;
    }

    private void preOrder(TreeNode node, DoublyLinked<String> result) {
        if (node == null) {
            return;
        }
        result.addLast(node.getData());
        preOrder(node.getLeft(), result);
        preOrder(node.getRight(), result);
    }

    public DoublyLinked<String> inOrder() {
        DoublyLinked<String> result = new DoublyLinked<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(TreeNode node, DoublyLinked<String> result) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft(), result);
        result.addLast(node.getData());
        inOrder(node.getRight(), result);
    }

    public DoublyLinked<String> postOrder() {
        DoublyLinked<String> result = new DoublyLinked<>();
        postOrder(root, result);
        return result;
    }

    private void postOrder(TreeNode node, DoublyLinked<String> result) {
        if (node == null) {
            return;
        }
        postOrder(node.getLeft(), result);
        postOrder(node.getRight(), result);
        result.addLast(node.getData());
    }

    public DoublyLinked<String> draw() {
        DoublyLinked<String> lines = new DoublyLinked<>();
        draw(root, "", "", lines);
        return lines;
    }

    private void draw(TreeNode node, String prefix, String childPrefix, DoublyLinked<String> lines) {
        if (node == null) {
            return;
        }
        lines.addLast(prefix + node.getData());
        draw(node.getLeft(), childPrefix + "|-- Sí: ", childPrefix + "|   ", lines);
        draw(node.getRight(), childPrefix + "`-- No: ", childPrefix + "    ", lines);
    }

    public void load(DoublyLinked<String> data) {
        TreeNode newRoot = buildTree(data);
        if (!data.isEmpty()) {
            throw new IllegalArgumentException("El archivo tiene registros sobrantes.");
        }
        root = newRoot;
    }

    private TreeNode buildTree(DoublyLinked<String> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("El archivo está incompleto: faltan nodos.");
        }
        String[] parts = parseLine(data.removeFirst());
        TreeNode node = new TreeNode(parts[1]);
        if (parts[0].equals(QUESTION)) {
            node.setLeft(buildTree(data));
            node.setRight(buildTree(data));
        }
        return node;
    }

    private String[] parseLine(String line) {
        String[] parts = line.split(",", 2);
        boolean valid = parts.length == 2
                && !parts[1].isBlank()
                && (parts[0].trim().equals(QUESTION) || parts[0].trim().equals(CHARACTER));
        if (!valid) {
            throw new IllegalArgumentException("Registro inválido: " + line);
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public DoublyLinked<String> serialize() {
        DoublyLinked<String> lines = new DoublyLinked<>();
        serialize(root, lines);
        return lines;
    }

    private void serialize(TreeNode node, DoublyLinked<String> lines) {
        if (node == null) {
            return;
        }
        String type = node.isLeaf() ? CHARACTER : QUESTION;
        lines.addLast(type + "," + node.getData());
        serialize(node.getLeft(), lines);
        serialize(node.getRight(), lines);
    }

    public int countCharacters() {
        return countCharacters(root);
    }

    private int countCharacters(TreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            return 1;
        }
        return countCharacters(node.getLeft()) + countCharacters(node.getRight());
    }

    public int countQuestions() {
        return countQuestions(root);
    }

    private int countQuestions(TreeNode node) {
        if (node == null || node.isLeaf()) {
            return 0;
        }
        return 1 + countQuestions(node.getLeft()) + countQuestions(node.getRight());
    }

    public void replaceNode(TreeNode oldNode, TreeNode newNode) {
        if (root == oldNode) {
            root = newNode;
            return;
        }
        replaceNode(root, oldNode, newNode);
    }

    private boolean replaceNode(TreeNode current, TreeNode oldNode, TreeNode newNode) {
        if (current == null) {
            return false;
        }
        return replaceChild(current, oldNode, newNode) || replaceNode(current.getLeft(), oldNode, newNode) || replaceNode(current.getRight(), oldNode, newNode);
    }

    private boolean replaceChild(TreeNode parent, TreeNode oldNode, TreeNode newNode) {
        if (parent.getLeft() == oldNode) {
            parent.setLeft(newNode);
            return true;
        }
        if (parent.getRight() == oldNode) {
            parent.setRight(newNode);
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
