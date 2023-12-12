package com.mycompany.app;

import net.sf.saxon.s9api.*;

/**
 * Simple example from documentation
 *
 */
public class SampleExtensionFunction
{
    public static void main( String[] args ) throws SaxonApiException {
        Processor proc = new Processor(false);
        ExtensionFunction sqrt = new ExtensionFunction() {
            public QName getName() {
                return new QName("http://math.com/", "sqrt");
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
                double arg = ((XdmAtomicValue)arguments[0].itemAt(0)).getDoubleValue();
                double result = Math.sqrt(arg);
                return new XdmAtomicValue(result);
            }
        };

        proc.registerExtensionFunction(sqrt);
        XPathCompiler comp = proc.newXPathCompiler();
        comp.declareNamespace("mf", "http://math.com/");
        comp.declareVariable(new QName("arg"));
        XPathExecutable exp = comp.compile("mf:sqrt($arg)");
        XPathSelector ev = exp.load();
        ev.setVariable(new QName("arg"), new XdmAtomicValue(2.0));
        XdmValue val = ev.evaluate();
        String result = val.toString();
        System.out.println("result: " + result);
    }
}
