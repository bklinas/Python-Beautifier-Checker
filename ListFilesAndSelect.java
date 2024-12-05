package draft;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ListFilesAndSelect {

    public static boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }

    public static void main(String[] args) {
        // Demandez à l'utilisateur de saisir le chemin du répertoire
        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez entrer le chemin du répertoire : ");
        String directoryPath = scanner.nextLine();

        if (isFile(directoryPath)) {
            System.out.println("Le chemin spécifié correspond à un fichier. Veuillez entrer un répertoire.");
            return;
        }

        // Créez un objet File pour représenter le répertoire
        File directory = new File(directoryPath);

        // Vérifiez si le répertoire existe
        if (directory.exists() && directory.isDirectory()) {
            // Obtenez la liste des fichiers dans le répertoire
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".py"));

            // Vérifiez s'il y a des fichiers Python dans le répertoire
            if (files != null && files.length > 0) {
                // Affichez la liste des fichiers Python
                System.out.println("Liste des fichiers Python dans le répertoire :");
                for (int i = 0; i < files.length; i++) {
                    System.out.println((i + 1) + ". " + files[i].getName());
                }

                // Demandez à l'utilisateur de sélectionner un fichier
                System.out.print("Veuillez entrer le numéro du fichier à traiter : ");
                int selectedFileIndex = scanner.nextInt();

                // Vérifiez si l'indice sélectionné est valide
                if (selectedFileIndex > 0 && selectedFileIndex <= files.length) {
                    File selectedFile = files[selectedFileIndex - 1];
                    System.out.println("Vous avez sélectionné le fichier : " + selectedFile.getName());

                    // Call the method from CommentChecker to check and update comments
                    /*try {
                        CommentChecker.verifierEtMettreAJourCommentaires(selectedFile.getPath());
                        System.out.println("Les commentaires ont été vérifiés et mis à jour avec succès.");
                    } catch (IOException e) {
                        System.out.println("Une erreur s'est produite lors de la lecture ou de l'écriture du fichier : " + e.getMessage());
                    }

                    // Create an instance of VerificateurTypage
                    VerificateurTypage verificateur = new VerificateurTypage(selectedFile.getPath(), false, selectedFile.getName());

                    // Call the method to verify typing
                    String resTypage = verificateur.verifierTypage();
                    System.out.println("Résultats de la vérification de typage:\n " + resTypage);*/
                } else {
                    System.out.println("Indice invalide. Veuillez sélectionner un numéro de fichier valide.");
                }
            } else {
                System.out.println("Aucun fichier Python dans le répertoire.");
            }
        } else {
            System.out.println("Le répertoire spécifié n'existe pas ou n'est pas un répertoire.");
        }
    }
    
}
