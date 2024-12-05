package draft;
import java.io.File;
import draft.CommentChecker;
import draft.ListFilesAndSelect;
import java.io.IOException;
import draft.VerificateurTypage;
public class CLI {

    public void checkFile(String[] args) {
    	
        if (args.length == 0) {
            System.out.println("Veuillez fournir le chemin du fichier Python en argument, tapez -h ou --help pour obtenir de l’aide  ");
            return;
        }
        for (String arg : args) {
            if (arg.equals("-h") || arg.equals("--help")) {
            	System.out.println("Utilisation :");
                System.out.println("java CLI -d <nom_dossier>");
                System.out.println("java CLI -f <nom_fichier> [--type] [--head] [--pydoc] [--sbutf8] [--comment] [-stat]");
                System.out.println("Options :");
                System.out.println("--type  :  Vérification des annotations de type");
                System.out.println("--head  :  Vérification des deux premières lignes de commentaire");
                System.out.println("--pydoc  :  Vérification des commentaires pydoc");
                System.out.println("--sbutf8   : Ajouter les deux premières lignes de commentaire manquantes (exception si déjà présentes)");
                System.out.println("--comment  : Ajouter un squelette de commentaire pydoc sur les fonctions non documentées");
                System.out.println("-stat     : Afficher les statistiques de qualité");
                return;
            }
        }

        String filePath = args[1];
        if (args.length == 5 && args[2].equals("--type") && args[3].equals("--head") && args[4].equals("--pydoc")) {
        	handleTypeHeadPydocOptions(args[1]);
        } 
        else if (args.length == 3) {
            switch (args[2]) {
                case "--type":
                    VerificateurTypage verificateur = new VerificateurTypage(filePath, false, "cc.py");
                    String resTypage = verificateur.verifierTypage();
                    System.out.println("Résultats de la vérification de typage:\n " + resTypage);
                    break;
                case "--head":
                    CommentChecker checker = new CommentChecker();
                    try {
                        boolean resChecker=checker.verifierCommentaires(filePath);
                        System.out.println("Les commentaires ont été vérifiés\n"+resChecker);
                    } catch (IOException e) {
                        System.out.println("Une erreur s'est produite lors de la lecture ou de l'écriture du fichier : " + e.getMessage());
                    }
                    break;
                case "--sbutf8":
                    CommentChecker ajout = new CommentChecker();
                    try {
                        if (CommentChecker.verifierCommentaires(filePath)) {
                            System.out.println("Les commentaires existent déjà, pas besoin d'ajouter.");
                        } else {
                            CommentChecker.ajoutCommentaires(filePath);
                            System.out.println("Les commentaires ont été ajoutés avec succès.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                    break;

                case "--pydoc":
                    CommentChecker verif = new CommentChecker();
                    try {
                        boolean verification=verif.verifierCommentairesPydoc(filePath);
                        System.out.println("Résultats de la vérification des commentaires pydoc:\n " + verification);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "--comment":
                    CommentChecker add = new CommentChecker();
                    try {
                        if (CommentChecker.verifierCommentairesPydoc(filePath)) {
                            System.out.println("Les commentaires existent déjà, pas besoin d'ajouter.");
                        } else {
                            CommentChecker.ajoutCommentairesPydoc(filePath);
                            System.out.println("Ajout des commentaires pydoc avec succès.");
                        }
                    } catch (IOException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                    break;

                case "-stat":
	            	analyzeQualityStatistics(args[1]);
	                break;
                default:
                    System.out.println("Option non reconnue.");
                    break;
            }
        } else if (args.length == 2) {
            switch (args[0]) {
                case "-d":
                    // Code for the case when the option is -d
                	listPythonFilesInDirectory(args[1]);
                    break;
                case "-f":
                    // Code for the case when the option is -f
                    File file = new File(args[1]);
                    file.isFile();
                    break;
         
                default:
                    System.out.println("Option non reconnue.");
                    break;
            }
        }
    }
    private void analyzeQualityStatistics(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Le répertoire spécifié n'existe pas.");
            return;
        }

        Statistics statistics = new Statistics();
        statistics.analyzeDirectory(directoryPath);
    }
    private void handleTypeHeadPydocOptions(String filePath) {
        // Check for the existence of the file
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Le fichier spécifié n'existe pas.");
            return;
        }

        // Handle the --type option
        VerificateurTypage verificateur = new VerificateurTypage(filePath, false, "cc.py");
        String resTypage = verificateur.verifierTypage();
        System.out.println("Résultats de la vérification de typage:\n " + resTypage);

        // Handle the --head option
        CommentChecker checker = new CommentChecker();
        try {
            boolean resChecker=checker.verifierCommentaires(filePath);
            System.out.println("Les commentaires ont été vérifiés\n"+resChecker);
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite lors de la lecture ou de l'écriture du fichier : " + e.getMessage());
        }

        // Handle the --pydoc option
        CommentChecker verif = new CommentChecker();
        try {
            boolean verification = verif.verifierCommentairesPydoc(filePath);
            System.out.println("Résultats de la vérification des commentaires pydoc:\n " + verification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void listPythonFilesInDirectory(String directoryPath) {
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

        System.out.println("Liste des fichiers Python dans le répertoire :");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i + 1) + ". " + files[i].getName());
        }

        // Check if the selected index is valid
        int selectedFileIndex = 1; // You can change this to the desired index
        if (selectedFileIndex > 0 && selectedFileIndex <= files.length) {
            File selectedFile = files[selectedFileIndex - 1];
            System.out.println("Vous avez sélectionné le fichier : " + selectedFile.getName());

            
        }
    }

    public static void main(String[] args) {
        CLI cli = new CLI();
        cli.checkFile(args);
    }
}
