package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

import common.FileType;

public class InputPreProcessor {

    private InputPreProcessor(){}
    
    public static final File getInputFile(String fileName, FileType fileType) throws FileNotFoundException {
        URL url = InputPreProcessor.class.getClassLoader().getResource(fileName);
        File inputFile = new File(url.getFile());
        if (!inputFile.exists()) {
            throw new FileNotFoundException("The Input file is not found");
        } else if (!fileName.toLowerCase().endsWith(fileType.name().toLowerCase())) {
            throw new IllegalArgumentException("The file is not acceptable type !");
        }
        return inputFile;
    }
    
    public static final File getOutputFile(String outputFileName, FileType fileType, File inputFile) throws FileAlreadyExistsException {
        File outputFile = new File(inputFile.getParent() + File.separator + outputFileName);
        if (outputFile.exists()) {
            throw new FileAlreadyExistsException("The Output file already exists");
        } else if (!outputFileName.toLowerCase().endsWith(fileType.name().toLowerCase())) {
            throw new IllegalArgumentException("The file is not acceptable type !");
        }
        return outputFile;
    }
    
    
    public static boolean isValidInputFile(File inputFile) {
        String fileName = inputFile.getName().toLowerCase();
        return (fileName.endsWith("txt") || fileName.endsWith("xml"));
    }
    
}
