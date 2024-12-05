package draft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificateurTypage {
    private final String cheminFichierPython;
    private boolean typageCorrect;
    private String codePython;

    public VerificateurTypage(String cheminFichierPython, boolean typageCorrect, String codePython) {
        this.cheminFichierPython = cheminFichierPython;
        this.typageCorrect = typageCorrect;
        this.codePython = codePython;
    }
   
    public String verifierTypage() {
        try {
            String pythonCode = readFileContent(cheminFichierPython);

            // Vérifier la présence d'annotations de type
            List<String> missingTypeAnnotations = findMissingTypeAnnotations(pythonCode);

            if (missingTypeAnnotations.isEmpty()) {
                return "Typage vérifié pour le fichier: " + cheminFichierPython;
            } else {
                return "Annotations de type manquantes dans les fonctions :\n " +
                        String.join(", ", missingTypeAnnotations);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur est survenue lors de la vérification du typage.";
        }
    }

    private String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private List<String> findMissingTypeAnnotations(String pythonCode) {
        List<String> missingTypeAnnotations = new ArrayList<>();

        // Pattern pour rechercher les définitions de fonction
        Pattern functionDefPattern = Pattern.compile("def\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*:");

        Matcher matcher = functionDefPattern.matcher(pythonCode);
        while (matcher.find()) {
            String functionName = matcher.group(1);
            String parameters = matcher.group(2);

            // Vérifier la présence d'une annotation de type pour les paramètres et la valeur de retour
            if (!parameters.contains(":") || !parameters.contains("->")) {
                missingTypeAnnotations.add(functionName);
            }
        }

        return missingTypeAnnotations;
    }

    

    public static void main(String[] args) {
        String pythonFilePath = "C:\\Users\\lenovo\\Downloads\\cc.py";
        VerificateurTypage verificateur = new VerificateurTypage(pythonFilePath, false, "cc.py");

        String resTypage = verificateur.verifierTypage();
        System.out.println("Résultats de la vérification de typage:\n " + resTypage);
    }
}

