package util;

import static org.junit.Assert.assertEquals;
import static util.InputPreProcessor.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.FileType;

public class InputPreProcessorTest {
    
    File inputFile;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        URL url = InputPreProcessor.class.getClassLoader().getResource("example-config.xml");
        inputFile = new File(url.getFile());
    }

    @Test
    public void testGetInputFile() throws FileNotFoundException {
        File textedFile = getInputFile(inputFile.getName(), FileType.XML);
        assertEquals(inputFile.getName(), textedFile.getName());
    }

    @Test
    public void testGetOutputFile() throws FileAlreadyExistsException {
        File outputFile = new File(inputFile.getParent() + File.separator + "example-config-missing-output.xml");
        File textedFile = getOutputFile("example-config-missing-output.xml", FileType.XML, inputFile);
        assertEquals(outputFile.getName(), textedFile.getName());
    }

}
