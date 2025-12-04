package magenative.automation;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileUploadDialog {

    public static String showFileUploadDialogAndSaveToProject() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("APK Files", "apk"));

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Check if the file is really an APK
            if (!selectedFile.getName().endsWith(".apk")) {
                JOptionPane.showMessageDialog(null, "Please select a valid .apk file.");
                return null;
            }

            try {
                // Create target directory if it doesn't exist
                String projectPath = System.getProperty("user.dir");
                File targetDir = new File(projectPath + File.separator + "apk-files");
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }

                // Create a unique name for the file to avoid overwrite
                String newFileName = selectedFile.getName();
                Path targetPath = Paths.get(targetDir.getAbsolutePath(), newFileName);

                // Copy the file to apk-files folder
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("APK saved to: " + targetPath.toString());
                return targetPath.toString(); // return this path for automation use

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to copy the file.");
            }
        }

        return null;
    }
}
