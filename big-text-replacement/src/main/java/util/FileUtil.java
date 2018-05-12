package util;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import common.FileType;
import common.ProcessorFactory;
import common.StreamProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FileUtil {

    
    static final ProcessorFactory FACTORY = ProcessorFactory.getInstance();
    
    public void openScreen() throws NoSuchAlgorithmException, IOException{
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose the .txt or XML file to replace text.");
        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            
            boolean isValidFile = InputPreProcessor.isValidInputFile(selectedFile);
            if (!isValidFile) {
                showWrongFileMessageAndCloseProgram();
            } else {
                showNextValueChooserScreen(selectedFile);
            }
            
        }
    }
    
    private void showWrongFileMessageAndCloseProgram() {
        JLabel viewLabel = new JLabel("Wrong file chosen. You must choose either XML or TEXT file.");
        JPanel viewPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 40, 40, 10);
        viewPanel.add(viewLabel,constraints);
        JFrame viewFrame = new JFrame();
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.add(viewPanel);
        viewFrame.pack();
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setVisible(true);
    }
    
    
    private void showNextValueChooserScreen(File selectedFile) {
        MainClass mainClass = new MainClass(selectedFile);
    }
    
    class MainClass {

        JTextField searchText;
        JTextField replacementText;
        JTextField newFileName;
        JButton submit;
        File selectedFile;
        
        SearchTextActionListener listener;

        MainClass(File selectedFile){
            
            JFrame main = new JFrame("Text Replacement Detail");
            main.setLocationRelativeTo(null);
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            searchText = new JTextField(10);
            replacementText = new JTextField(10);
            newFileName = new JTextField(10);

            JPanel gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));
            main.setContentPane(gui);

            JPanel labels = new JPanel(new GridLayout(0,1));
            JPanel controls = new JPanel(new GridLayout(0,1));
            gui.add(labels, BorderLayout.WEST);
            gui.add(controls, BorderLayout.CENTER);

            labels.add(new JLabel("Please enter Search Text: "));
            controls.add(searchText);
            labels.add(new JLabel("Please enter replacement Text: "));
            controls.add(replacementText);
            labels.add(new JLabel("Please enter result File Name: "));
            controls.add(newFileName);
            submit = new JButton("Submit");
            listener = new SearchTextActionListener(submit, searchText, replacementText, newFileName, selectedFile, main);
            submit.addActionListener(listener);

            gui.add(submit, BorderLayout.SOUTH);
            main.pack();
            main.setVisible(true);
        }
    }
    
    @RequiredArgsConstructor
    private static class SearchTextActionListener implements ActionListener {

        @Getter
        private String searchText;
        
        @Getter
        private String replacementText;
        
        @Getter
        private String newFileName;
        
        
        private final JButton submitButton;
        private final JTextField searchTextField;
        private final JTextField replacementTextField;
        private final JTextField newFileNameField;
        private final File selectedFile;
        private final JFrame main;
        
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == submitButton) {
                searchText = searchTextField.getText();
                replacementText = replacementTextField.getText();
                newFileName = newFileNameField.getText();
                String fileName = selectedFile.getName();
                
                FileType fileType = (fileName.toLowerCase().endsWith("xml")) ? FileType.XML : FileType.TXT;
                String fileMessageText = null;
                Boolean isConversionSuccessful = true;
                try {
                    newFileName = newFileName.endsWith(fileType.name().toLowerCase()) ? newFileName : newFileName + "." + fileType.name().toLowerCase();
                    File outputFile = InputPreProcessor.getOutputFile(newFileName, fileType, selectedFile);
                    fileMessageText = outputFile.getAbsolutePath() + File.separator + outputFile.getName();
                    StreamProcessor processor = FACTORY.getStreamProcessor(fileType, selectedFile, outputFile, searchText, replacementText);
                    processor.processFiles();
                } catch (FileAlreadyExistsException e) {
                    fileMessageText = "Error Occured - " + e.getMessage();
                    isConversionSuccessful = false;
                } catch (Exception e) {
                    fileMessageText = "Error Occured - " + e.getMessage();
                    isConversionSuccessful = false;
                }
                main.dispose();
                concludeFileOperation(fileMessageText, isConversionSuccessful);
            }
            
        }
        
        
        private void concludeFileOperation(String fileMessageText, Boolean isConversionSuccessful) {
            String message = null;
            if (isConversionSuccessful) {
                message = "File operation is done ! Output File is kept at - " + fileMessageText;
            } else {
                message = fileMessageText;
            }
            JLabel viewLabel = new JLabel(message);
            JPanel viewPanel = new JPanel(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(10, 40, 40, 10);
            viewPanel.add(viewLabel,constraints);
            JFrame viewFrame = new JFrame();
            viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewFrame.add(viewPanel);
            viewFrame.pack();
            viewFrame.setLocationRelativeTo(null);
            viewFrame.setVisible(true);
        }
        
    }
}
