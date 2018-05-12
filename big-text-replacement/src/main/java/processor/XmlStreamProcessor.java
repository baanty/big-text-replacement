package processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import common.AbstractStreamProcessor;

/**
 * 
 * @author Pijush Kanti Das
 * This class will replace the desired text in the attributes of an input XMl file.
 * 
 * Stax Parser is used to read, modify and write the XML files.
 * Please refer to the Oracle documentation. 
 * 
 * @see <a href="https://docs.oracle.com/javase/tutorial/jaxp/stax/why.html" >Reason Behind Stax</a>
 * 
 * This justifies that STax is suitable to read, manipulate the XML tree in forward direction.
 * At the same time, it helps write the output XML to a stream. As the output will be in a stream, 
 * The heap will always be light weight.
 * 
 * Also, in the below program, the buffer is always flushed after processing one single event.
 * So, the heap is always light. No matter how big the incoming or outgoing XMLs are.
 *
 */

public class XmlStreamProcessor extends AbstractStreamProcessor{

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newInstance();
    
    private static final XMLInputFactory IN_FACTORY = XMLInputFactory.newInstance();

    private static final XMLOutputFactory OUT_FACTORY = XMLOutputFactory.newInstance();

    


    /**
     * @param inputFile : The input file to be used for changing the text
     * @param outputFile ; The output file for text replacement
     * @param stringToReplace ; The string which will be searched and replaced.
     * @param newString ; The new string, which will replace the old one.
     */
    public XmlStreamProcessor(File inputFile, File outputFile, String stringToReplace, String newString) {
        super(inputFile, outputFile, stringToReplace, newString);
    }




    /**
     * This method will process the XML file, which is loaded in the 
     * Constructor path of this class.
     * 
     * Stax Parser is used to read, modify and write the XML files.
     * Please refer to the Oracle documentation. 
     * 
     * @see <a href="https://docs.oracle.com/javase/tutorial/jaxp/stax/why.html" >Reason Behind Stax</a>
     * 
     * This justifies that STax is suitable to read, manipulate the XML tree in forward direction.
     * At the same time, it helps write the output XML to a stream. As the output will be in a stream, 
     * The heap will always be light weight.
     * 
     * Also, in the below program, the buffer is always flushed after processing one single event.
     * So, the heap is always light. No matter how big the incoming or outgoing XMLs are.
     */
    @SuppressWarnings("unchecked")
    public final void processFiles() {
        try {
            InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile);

            XMLEventReader eventReader = IN_FACTORY.createXMLEventReader(inputStream, "UTF-8");
            XMLEventWriter eventWriter = OUT_FACTORY.createXMLEventWriter(outputStream, "UTF-8");

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                
                case XMLStreamConstants.START_ELEMENT: {
                    
                    StartElement startElement = event.asStartElement();
                    Iterator<Attribute> attrbIterator = startElement.getAttributes();
                    
                    List<Attribute> newAttributes = new ArrayList<Attribute>();
                    
                    while (attrbIterator.hasNext()) {
                        Attribute oldAttrb = attrbIterator.next();
                        Attribute newAttribute = EVENT_FACTORY.createAttribute(oldAttrb.getName(), oldAttrb.getValue().replaceAll(stringToReplace, newString));
                        newAttributes.add(newAttribute);
                    }
                    
                    StartElement newStartElement = EVENT_FACTORY.createStartElement(startElement.getName(), newAttributes.iterator(), startElement.getNamespaces());
                    eventWriter.add(newStartElement);
                    break;
                }
                
                default: {
                    eventWriter.add(event);
                    break;
                    }
                }
                eventWriter.flush();
            }
            
            eventWriter.close();

        } catch (FileNotFoundException fileNotFOundException) {
            throw new RuntimeException("The input or output file is not found", fileNotFOundException);
        } catch (XMLStreamException xmlStreamException) {
            throw new RuntimeException("The XMl stream can not be opened for reading and writing", xmlStreamException);
        }
    }

    
}
