package com.mycompany.app;

import net.sf.saxon.s9api.*;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * Expanding on sample to actually be used in xml xslt transformation with SAXON-HE
 */
public class SampleExtensionFunction2 {
    public static void main(String[] args) throws SaxonApiException {
        try {
            Processor proc = new Processor(false);
            ExtensionFunction sqrt = new ExtensionFunction() {
                public QName getName() {
                    return new QName("http://lars.com/", "sqrt");
                }

                public SequenceType getResultType() {
                    return SequenceType.makeSequenceType(
                            ItemType.DOUBLE, OccurrenceIndicator.ONE
                    );
                }

                public SequenceType[] getArgumentTypes() {
                    return new SequenceType[]{
                            SequenceType.makeSequenceType(
                                    ItemType.DOUBLE, OccurrenceIndicator.ONE)};
                }

                public XdmValue call(XdmValue[] arguments) throws SaxonApiException {

                    double arg = ((XdmAtomicValue) arguments[0].itemAt(0)).getDoubleValue();
                    double result = Math.sqrt(arg);

                    return new XdmAtomicValue(result);
                }
            };

            proc.registerExtensionFunction(sqrt);

            // Load the source XML from the classpath
            InputStream xmlStream = SampleExtensionFunction2.class.getClassLoader().getResourceAsStream("hello.xml");
            XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlStream));

            // Initialize the XSLT compiler
            XsltCompiler compiler = proc.newXsltCompiler();

            // Compile the XSLT stylesheet from the classpath
            InputStream xsltStream = SampleExtensionFunction2.class.getClassLoader().getResourceAsStream("transform1.xslt");
            XsltExecutable executable = compiler.compile(new StreamSource(xsltStream));

            // Create a transformer
            XsltTransformer transformer = executable.load();

            // Set the root node of the source document
            transformer.setInitialContextNode(source);

            // Set up the output destination
            Serializer out = proc.newSerializer(System.out);
            out.setOutputProperty(Serializer.Property.METHOD, "text");
//            out.setOutputProperty(Serializer.Property.METHOD, "xml"); // output xml
//            out.setOutputProperty(Serializer.Property.INDENT, "yes");
            transformer.setDestination(out);

            // Perform the transformation
            transformer.transform();

            System.out.println("\r\nTransformation completed successfully.");
        } catch (SaxonApiException e) {
            System.err.println("Error during XSLT transformation: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
