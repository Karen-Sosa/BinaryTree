package co.edu.uptc.data;

import co.edu.uptc.list.DoublyLinked;
import co.edu.uptc.list.ListNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Properties;

public class Reader {
    private static final String CONFIG = "config.properties";

    private final String path;
    private final String defaultResource;

    public Reader(){
        Properties config = loadConfig();
        this.path = config.getProperty("tree.path", "data/tree.csv");
        this.defaultResource = config.getProperty("tree.default", "defaultTree.csv");
    }

    private Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = Reader.class.getClassLoader().getResourceAsStream(CONFIG)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            // Se usan los valores por defecto.
        }
        return properties;
    }

    public DoublyLinked<String> read() {
        if (Files.notExists(Path.of(path))) {
            createExternalFile();
        }
        return readFile();
    }

    public DoublyLinked<String> reset() {
        createExternalFile();
        return readFile();
    }

    private DoublyLinked<String> readFile() {
        DoublyLinked<String> data = new DoublyLinked<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                addIfValid(data, line);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el archivo " + path, e);
        }
        return data;
    }

    private void addIfValid(DoublyLinked<String> data, String line) {
        String clean = line.replace("\uFEFF", "").trim();
        if (!clean.isEmpty() && !clean.startsWith("#")) {
            data.addLast(clean);
        }
    }

    public void createExternalFile(){
        try (InputStream input = Reader.class.getClassLoader().getResourceAsStream(defaultResource)) {
            if (input == null) {
                throw new FileNotFoundException("No existe el archivo por defecto: " + defaultResource);
            }
            ensureDirectory();
            Files.copy(input, Path.of(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear el archivo " + path, e);
        }
    }

    private void ensureDirectory() throws IOException {
        Path parent = Path.of(path).toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    public void write(DoublyLinked<String> data) {
        try {
            ensureDirectory();
            writeLines(data);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo " + path, e);
        }
    }

    private void writeLines(DoublyLinked<String> data) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path), StandardCharsets.UTF_8)) {
            for (ListNode<String> node = data.getHead(); node != null; node = node.getNext()) {
                bw.write(node.getData());
                bw.newLine();
            }
        }
    }
}
