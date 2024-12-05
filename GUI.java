package draft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.attribute.FileTime;
import draft.CommentChecker;

public class GUI extends JFrame {
    private JList<String> fileList;
    private DefaultListModel<String> listModel;
    private JTextField directoryTextField;
    private JTextArea fileDetailsTextArea;
    private JButton refreshButton;

    public GUI() {
        super("File Explorer");

        // Create components
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(fileList);
        JButton editButton = new JButton("Edit File");
        JButton addHeaderButton = new JButton("Add File Header");
        JButton checkPydocButton = new JButton("Check Pydoc Comments");
        JButton addPydocButton = new JButton("add Pydoc Comments");
        JButton checkHeaderButton = new JButton("Check File Header");
        JButton exploreButton = new JButton("Explore Files");
        
        directoryTextField = new JTextField("C:/Users/lenovo/Downloads", 20); // Default directory
        fileDetailsTextArea = new JTextArea();
        fileDetailsTextArea.setEditable(false);

        // Set layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Directory: "));
        inputPanel.add(directoryTextField);
        inputPanel.add(editButton);
        inputPanel.add(addHeaderButton);
        inputPanel.add(checkPydocButton);
        inputPanel.add(addPydocButton);
        inputPanel.add(checkHeaderButton);
        inputPanel.add(exploreButton);
        add(inputPanel, BorderLayout.SOUTH);

        add(new JScrollPane(fileDetailsTextArea), BorderLayout.EAST);

        // Add action listeners
        editButton.addActionListener(e -> editSelectedFile());
        addHeaderButton.addActionListener(e -> addFileHeader());
        checkPydocButton.addActionListener(e -> checkPydocComments());
        addPydocButton.addActionListener(e -> addPydocComments());
        checkHeaderButton.addActionListener(e -> checkFileHeader());
        exploreButton.addActionListener(e -> exploreFiles());
        refreshButton = new JButton("Refresh");

        // Add the refresh button to the inputPanel
        inputPanel.add(refreshButton);

        // Add action listener for the refresh button
        refreshButton.addActionListener(e -> refreshFileList());

        // Set frame properties
        setSize(800, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the initial directory to explore
        updateFileList(directoryTextField.getText());
    }
    private void refreshFileList() {
        String newDirectory = directoryTextField.getText();
        updateFileList(newDirectory);
    }
    private void updateFileList(String directory) {
        listModel.clear();

        // Explore the directory and its subdirectories
        List<String> pythonFiles = exploreDirectory(directory);

        for (String file : pythonFiles) {
            listModel.addElement(file);
        }
    }

    private List<String> exploreDirectory(String directory) {
        try {
            // Print the directory being explored (for debugging purposes)
            System.out.println("Exploring directory: " + directory);

            return Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().toLowerCase().endsWith(".py"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    

    private void editSelectedFile() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            // Implement the logic to edit the selected file
            // You can open a text editor or implement your own editor here

            // Example: Open the file in Notepad (Windows)
            try {
                Runtime.getRuntime().exec("notepad.exe " + selectedFilePath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addFileHeader() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            try {
                if (CommentChecker.verifierCommentaires(selectedFilePath)) {
                    JOptionPane.showMessageDialog(this, "File header already present.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    CommentChecker.ajoutCommentaires(selectedFilePath);
                    updateFileDetails(selectedFilePath);
                    JOptionPane.showMessageDialog(this, "File header added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding file header: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPydocComments() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            try {
                if (CommentChecker.verifierCommentairesPydoc(selectedFilePath)) {
                    JOptionPane.showMessageDialog(this, "Pydoc comments already present.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    CommentChecker.ajoutCommentairesPydoc(selectedFilePath);
                    updateFileDetails(selectedFilePath);
                    JOptionPane.showMessageDialog(this, "Pydoc comments added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding Pydoc comments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void checkPydocComments() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            try {
                if (CommentChecker.verifierCommentairesPydoc(selectedFilePath)) {
                    JOptionPane.showMessageDialog(this, "Pydoc comments are present in the file.", "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Pydoc comments are absent in the file.", "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void checkFileHeader() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            try {
                if (CommentChecker.verifierCommentaires(selectedFilePath)) {
                    JOptionPane.showMessageDialog(this, "File header is present in the file.", "Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "File header is absent in the file.", "Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exploreFiles() {
        String selectedFilePath = fileList.getSelectedValue();
        if (selectedFilePath != null) {
            updateFileDetails(selectedFilePath);
            checkTypeAnnotations(selectedFilePath);
            
            // Ajouter les fonctionnalités supplémentaires
            showFileSize(selectedFilePath);
            showFileDate(selectedFilePath);
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showFileSize(String filePath) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            long fileSizeInKB = fileAttributes.size() / 1024;

            JOptionPane.showMessageDialog(this, "File Size: " + fileSizeInKB + " KB", "File Size", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showFileDate(String filePath) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);
            FileTime fileTime = fileAttributes.lastModifiedTime();

            JOptionPane.showMessageDialog(this, "Last Modified Date: " + fileTime, "Last Modified Date", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    
    private void updateFileDetails(String filePath) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get(filePath), BasicFileAttributes.class);

            String details = "File Path: " + filePath + "\n" +
                    "Size: " + fileAttributes.size() / 1024 + " KB\n" +
                    "Last Modified Time: " + fileAttributes.lastModifiedTime() + "\n";

            fileDetailsTextArea.setText(details);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private void checkTypeAnnotations(String filePath) {
        VerificateurTypage verificateur = new VerificateurTypage(filePath, false, "exercice3TP4.py");
        String resTypage = verificateur.verifierTypage();
        JOptionPane.showMessageDialog(this, "Results of type checking:\n " + resTypage, "Type Checking", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.setVisible(true);
        });
    }
}
