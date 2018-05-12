package common;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import processor.TextStreamProcessor;
import processor.XmlStreamProcessor;

public class ProcessorFactoryTest {

    ProcessorFactory factory;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        factory = ProcessorFactory.getInstance();
    }

    @Test
    public void testGetInstanceFactory() {
        assertNotNull(factory);
    }

    @Test
    public void testGetStreamProcessorForTextStreamProcessor() {
        StreamProcessor processor = factory.getStreamProcessor(FileType.TXT, null, null, null, null);
        assertNotNull(processor);
        assertTrue(processor instanceof TextStreamProcessor);
    }
    
    @Test
    public void testGetStreamProcessorForXmlStreamProcessor() {
        StreamProcessor processor = factory.getStreamProcessor(FileType.XML, null, null, null, null);
        assertNotNull(processor);
        assertTrue(processor instanceof XmlStreamProcessor);
    }


}
