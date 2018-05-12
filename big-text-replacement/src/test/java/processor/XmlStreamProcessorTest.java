package processor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XmlStreamProcessorTest {

    private File inputFile;

    private File outputFile;

    private String stringToReplace;

    private String newString;

    private XmlStreamProcessor xmlProcessor;

    private static final XMLInputFactory IN_FACTORY = XMLInputFactory.newInstance();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        inputFile = Paths.get(this.getClass().getClassLoader().getResource("example-config.xml").toURI()).toFile();
        outputFile = new File(inputFile.getParent() + File.separator + "example-config-output.xml");
        stringToReplace = "trace";
        newString = "error";
    }

    @After
    public void cleanUp() {
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void testProcessXmlFiles() throws XMLStreamException, FileNotFoundException {
        xmlProcessor = new XmlStreamProcessor(inputFile, outputFile, stringToReplace,
                newString);
        xmlProcessor.processFiles();

        XMLEventReader eventReader = IN_FACTORY.createXMLEventReader(new FileInputStream(outputFile), "UTF-8");

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
            case XMLStreamConstants.START_ELEMENT: {
                StartElement startElement = event.asStartElement();
                @SuppressWarnings("unchecked")
                Iterator<Attribute> attrs = startElement.getAttributes();
                while (attrs.hasNext()) {
                    Attribute attr = attrs.next();
                    if ("level".equals(attr.getName().getLocalPart())) {
                       assertEquals("error", attr.getValue());
                    }
                    if ("name".equals(attr.getName().getLocalPart())) {
                       assertEquals("error-20180101.log", attr.getValue());
                    }

                }
            }
            }
        }
    }

}
