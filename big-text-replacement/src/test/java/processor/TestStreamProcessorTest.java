package processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.stream.XMLStreamException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStreamProcessorTest {

    private File inputFile;

    private File outputFile;

    private String stringToReplace;

    private String newString;

    private TextStreamProcessor textProcessor;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        inputFile = Paths.get(this.getClass().getClassLoader().getResource("manifesto.txt").toURI()).toFile();
        outputFile = new File(inputFile.getParent() + File.separator + "manifesto-output.txt");
        stringToReplace = "customer";
        newString = "client";
    }

    @After
    public void cleanUp() {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testProcessXmlFiles() throws XMLStreamException, IOException {
        textProcessor = new TextStreamProcessor(inputFile, outputFile, stringToReplace,
                newString);
        textProcessor.processFiles();
        
        FileReader fileReader = new FileReader(outputFile);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = null;
        int lineNumber = 1;
        while ((line = reader.readLine()) != null) {
            assertFalse(line.contains("customer"));
            if (lineNumber == 1) {
                assertEquals("Our highest priority is to satisfy the client", line);
            }
            else if (lineNumber == 6) {
                assertNotNull(line);
                assertTrue(line.startsWith("the client's competitive advantage."));
            }
            lineNumber++;
        }
        reader.close();
        fileReader.close();
    }

}
