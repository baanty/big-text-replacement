package handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class XmlHandler extends DefaultHandler {
    
    private final File inputXmlFile;
    
    private final File outputXmlFile;
    
    private final String originalText;
    
    private final String newText;
    
    private BufferedOutputStream bufferedOutStream;
    
    private StringBuilder tempElement = new StringBuilder();

    /**
     * @param inputXmlFile
     * @param outputXmlFile
     * @throws FileNotFoundException 
     */
    public XmlHandler(File inputXmlFile, File outputXmlFile, String originalText, String newText) throws FileNotFoundException {
        super();
        this.inputXmlFile = inputXmlFile;
        this.outputXmlFile = outputXmlFile;
        this.originalText = originalText;
        this.newText = newText;
        FileOutputStream fileOutputSteream = new FileOutputStream(outputXmlFile); 
        bufferedOutStream = new BufferedOutputStream(fileOutputSteream, 0);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tempElement.append("");
        int numberOfAttributes = attributes.getLength();
        for (int attrbIndex = 0 ; attrbIndex < numberOfAttributes ; attrbIndex++ ) {
            String attributeValue = attributes.getValue(attrbIndex);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            bufferedOutStream.write(0);
            bufferedOutStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tempElement = new StringBuilder();
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String elementString = new String(ch, start, length);
        tempElement.append(elementString);
    }
    
    @Override
    public void endDocument() throws SAXException {
        try {
            bufferedOutStream.close();
        } catch (IOException ioException) {
           throw new RuntimeException(ioException);            
        }
    }

}
