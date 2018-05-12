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

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class XmlStreamProcessor {
    
    @SuppressWarnings("unused")
    private XmlStreamProcessor() throws RuntimeException{
        inputXmlFile = null;
        outputXmlFile = null;
        stringToReplaceInAtrribute = null;
        newStringINAtrribute = null;
    }

    private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newInstance();
    
    private static final XMLInputFactory IN_FACTORY = XMLInputFactory.newInstance();

    private static final XMLOutputFactory OUT_FACTORY = XMLOutputFactory.newInstance();

    private final File inputXmlFile;

    private final File outputXmlFile;
    
    private final String stringToReplaceInAtrribute;
    
    private final String newStringINAtrribute;

    /**
     * This method will process the XML file, which is loaded in the 
     * Constructor path of this class.
     */
    @SuppressWarnings("unchecked")
    public final void processXmlFiles() {
        try {
            InputStream inputStream = new FileInputStream(inputXmlFile);
            OutputStream outputStream = new FileOutputStream(outputXmlFile);

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
                        Attribute newAttribute = EVENT_FACTORY.createAttribute(oldAttrb.getName(), oldAttrb.getValue().replaceAll(stringToReplaceInAtrribute, newStringINAtrribute));
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
