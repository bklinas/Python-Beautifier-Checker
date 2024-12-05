package draft;

import java.io.*;
import java.util.Scanner;

public class CommentChecker {
    private static final String PREFERENCES_FILE = "comment_preferences.ser";
    private static final String PYTHON_COMMENT_HEADER = "#!/usr/bin/python3\n # -*- coding: utf-8 -*-\n";
    private static final String PYDOC_COMMENT = "\"\"\"\n"
            + "Description détaillée du contenu du module.\n"
            + "\n"
            + "Ce module fait... (écrivez une description significative)\n"
            + "\n"
            + "@author: Bkl\n"
            + "@version: 1.0\n\"\"\"\n\n";

    public static void main(String[] args) {
        loadPreferences();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Veuillez entrer le chemin du fichier Python : ");
        String filePath = scanner.nextLine();

        try {
        	if (!verifierCommentairesPydoc(filePath)) {
                ajoutCommentairesPydoc(filePath);
                System.out.println("Les commentaires au format pydoc ont été ajoutés avec succès.");
            }
            if (!verifierCommentaires(filePath)) {
                ajoutCommentaires(filePath);
                System.out.println("Ajout des 2 premières lignes de commentaires effectué avec succès.");
            }

           
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la lecture ou de l'écriture du fichier : " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static boolean verifierCommentaires(String filePath) throws IOException {
        String existingContent = readFileContent(filePath);
        return existingContent.startsWith(PYTHON_COMMENT_HEADER);
    }

    
    public static boolean verifierCommentairesPydoc(String filePath) throws IOException {
        String existingContent = readFileContent(filePath);
        return existingContent.contains(PYDOC_COMMENT);
    }

    public static void ajoutCommentaires(String filePath) throws IOException {
        String existingContent = readFileContent(filePath);

        // Remove existing shebang comment
        String contentWithoutShebang = existingContent.replaceFirst("#!.*\n", "");

        // Add the shebang comment at the very beginning
        String newContent = PYTHON_COMMENT_HEADER + contentWithoutShebang;

        // Write the updated content to the file
        writeFileContent(filePath, newContent);
    }


    public static void ajoutCommentairesPydoc(String filePath) throws IOException {
        String existingContent = readFileContent(filePath);

        // Remove existing docstring comment
        String contentWithoutComments = existingContent.replaceFirst("#.*?\"\"\".*?\"\"\"\n", "");

        // Add the docstring comment
        String newContent = PYDOC_COMMENT + contentWithoutComments;

        // Write the updated content to the file
        writeFileContent(filePath, newContent);
    }

    private static String readFileContent(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    private static void writeFileContent(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static void loadPreferences() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PREFERENCES_FILE))) {
            Preferences preferences = (Preferences) ois.readObject();
            setAuthorName(preferences.getAuthorName());
            setVersionNumber(preferences.getVersionNumber());
        } catch (FileNotFoundException e) {
            // Ignore if the preferences file is not found
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void savePreferences() {
        Preferences preferences = new Preferences(getAuthorName(), getVersionNumber());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PREFERENCES_FILE))) {
            oos.writeObject(preferences);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Preferences implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String authorName;
        private final String versionNumber;

        public Preferences(String authorName, String versionNumber) {
            this.authorName = authorName;
            this.versionNumber = versionNumber;
        }

        public String getAuthorName() {
            return authorName;
        }

        public String getVersionNumber() {
            return versionNumber;
        }
    }

    private static String authorName = "";
    private static String versionNumber = "";

    public static String getAuthorName() {
        return authorName;
    }

    public static void setAuthorName(String author) {
        authorName = author;
    }

    public static String getVersionNumber() {
        return versionNumber;
    }

    public static void setVersionNumber(String version) {
        versionNumber = version;
    }
}