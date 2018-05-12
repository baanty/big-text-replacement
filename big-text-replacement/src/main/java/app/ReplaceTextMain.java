package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

import common.FileType;
import common.ProcessorFactory;
import common.StreamProcessor;
import util.InputPreProcessor;

public class ReplaceTextMain {
    
    static final ProcessorFactory FACTORY = ProcessorFactory.getInstance();

    public static void main(String[] args) {
        
        if (args == null || args.length != 5) {
            System.out.println("Invalid Input. Program will exit");
            return;
        }
        String fileTypeString = args[0].toUpperCase();
        String stringToReplace = args[1];
        String newString = args[2];
        String inputFileString = args[3];
        String outputFileString = args[4];

        if ( !fileTypeString.equals(FileType.TXT.name()) && !fileTypeString.equals(FileType.XML.name()) ) {
            System.out.println("Invalid Input. Unacceptable File Type");
            return;
        }
        FileType fileType = FileType.valueOf(inputFileString);
        try {
            File inputFile = InputPreProcessor.getInputFile(inputFileString, fileType);
            File outputFile = InputPreProcessor.getOutputFile(outputFileString, fileType, inputFile);
            StreamProcessor processor = FACTORY.getStreamProcessor(fileType, inputFile, outputFile, stringToReplace, newString);
            processor.processFiles();
        } catch (FileNotFoundException e) {
            System.out.println("Input File does not exist. Program will exit.");
            return;
        } catch (FileAlreadyExistsException e) {
            System.out.println("The output file already exits. Program will exit.");
            return;
        } catch (Exception e) {
            System.out.println("Unknown Exception occured. Program will exit.");
            return;
        }
    }

}
