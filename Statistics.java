package draft;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Statistics {
    private int totalFiles;
    private int totalFunctions;

    public void analyzeDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Le répertoire spécifié n'existe pas.");
            return;
        }

        File[] files = directory.listFiles((dir, name) -> name.endsWith(".py"));

        if (files == null || files.length == 0) {
            System.out.println("Aucun fichier Python trouvé dans le répertoire.");
            return;
        }

        totalFiles = files.length;
        int annotatedFiles = 0;
        int commentedFiles = 0;
        int pydocFiles = 0;

        for (File file : files) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                boolean hasAnnotations = false;
                boolean hasComments = false;
                boolean insideCommentBlock = false;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) {
                        hasComments = true;
                        if (isPydocComment(line, insideCommentBlock)) {
                            insideCommentBlock = !insideCommentBlock; // Toggle the insideCommentBlock flag
                        }
                    } else if (line.trim().startsWith("def ")) {
                        hasAnnotations = true;
                        totalFunctions++; // Count functions here
                    }
                }


                if (hasAnnotations) {
                    annotatedFiles++;
                }

                if (hasComments) {
                    commentedFiles++;
                }

                if (insideCommentBlock) {
                    pydocFiles++;
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double percentageAnnotated = totalFiles > 0 ? ((double) annotatedFiles / totalFiles) * 100 : 0;
        double percentageCommented = totalFiles > 0 ? ((double) commentedFiles / totalFiles) * 100 : 0;
        double percentagePydoc = totalFiles > 0 ? ((double) pydocFiles / totalFiles) * 100 : 0;

        System.out.println("Statistiques de qualité pour le répertoire :");
        System.out.println("Nombre total de fichiers analysés : " + totalFiles);
        System.out.println("Nombre total de fonctions : " + totalFunctions);
        System.out.println("Pourcentage des fichiers avec annotations : " + percentageAnnotated + "%");
        System.out.println("Pourcentage des fichiers avec les deux premières lignes de commentaire : " + percentageCommented + "%");
        System.out.println("Pourcentage des fichiers avec des commentaires de fonction au format pydoc : " + percentagePydoc + "%");
    }

    private boolean isPydocComment(String line, boolean insideCommentBlock) {
        // Check for the start of a pydoc comment block
        if (line.trim().startsWith("")) {
            return true;
        }

        // Check if we are inside a pydoc comment block
        if (insideCommentBlock) {
            // Check for the end of a pydoc comment block
            if (line.trim().endsWith("")) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Statistics statistics = new Statistics();
        String directoryPath = "C:\\Users\\lenovo\\Downloads";
        statistics.analyzeDirectory(directoryPath);
    }
}
