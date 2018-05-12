package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import common.AbstractStreamProcessor;
import lombok.RequiredArgsConstructor;

/**
 * @author Pijush Kanti Das
 *
 */

@RequiredArgsConstructor
public class TextStreamProcessor extends AbstractStreamProcessor {
    
    /**
     * @param inputFile : The input file to be used for changing the text
     * @param outputFile ; The output file for text replacement
     * @param stringToReplace ; The string which will be searched and replaced.
     * @param newString ; The new string, which will replace the old one.
     */
    public TextStreamProcessor(File inputFile, File outputFile, String stringToReplace, String newString) {
        super(inputFile, outputFile, stringToReplace, newString);
    }

    
    public void processFiles() throws IOException {
        
        int readBufferSize = 128;
        long fileSizeInBytes = inputFile.length();
        
        if (fileSizeInBytes < 128 * 1024 * 1024 ) {
             readBufferSize = (int) fileSizeInBytes;
        } else {
             readBufferSize = 128 * 1024 * 1024;
        }
        
        byte[] bytes = new byte[readBufferSize];
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        
        @SuppressWarnings("unused")
        int red = 0;
        
        while ((red = fileInputStream.read(bytes)) > 0) {
            String redCharacters = new String(bytes);
            
            if (redCharacters != null) {
                String newLine = redCharacters.replaceAll(stringToReplace, newString);
                fileOutputStream.write(newLine.getBytes(StandardCharsets.UTF_8));
            }
            fileOutputStream.flush();
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
    
    
}


