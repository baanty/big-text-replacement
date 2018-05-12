package common;

import java.io.File;

import processor.TextStreamProcessor;
import processor.XmlStreamProcessor;

public class ProcessorFactory {
    
    
    private ProcessorFactory(){}
    
    private static ProcessorFactory _FACTORY;
    
    public static final ProcessorFactory getInstance() {
        if (_FACTORY == null) {
            _FACTORY = new ProcessorFactory();
        }
        return _FACTORY;
    }
    
    public final StreamProcessor getStreamProcessor(FileType fileType,
                                                    File inputFile,
                                                    File outputFile,
                                                    String stringToReplace,
                                                    String newString) {
        StreamProcessor streamProcessor = null;
        switch (fileType) {
            case XML : {
                streamProcessor = new XmlStreamProcessor(inputFile, outputFile, stringToReplace, newString);
                break;
            }
            case TXT : {
                streamProcessor = new TextStreamProcessor(inputFile, outputFile, stringToReplace, newString);
                break;
            }
        }
        return streamProcessor;
    }

}
