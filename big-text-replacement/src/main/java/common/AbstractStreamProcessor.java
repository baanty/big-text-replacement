package common;

import java.io.File;

import lombok.RequiredArgsConstructor;

/**
 * @author Pijush Kanti Das
 *
 */

@RequiredArgsConstructor
public abstract class AbstractStreamProcessor implements StreamProcessor{
    
    
    protected AbstractStreamProcessor() throws RuntimeException{
        inputFile = null;
        outputFile = null;
        stringToReplace = null;
        newString = null;
    }

    protected final File inputFile;

    protected final File outputFile;
    
    protected final String stringToReplace;
    
    protected final String newString;

    
}


