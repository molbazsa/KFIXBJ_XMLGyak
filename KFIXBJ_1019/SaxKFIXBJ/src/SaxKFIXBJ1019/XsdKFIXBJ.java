package SaxKFIXBJ1019;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XsdKFIXBJ {
    public static void main(String[] args) {
        String xsdFileName = "MB_kurzusfelvetel.xsd";
        String xmlFileName = "MB_kurzusfelvetel.xml";

        File xsdFile = Paths.get("documents", xsdFileName).toFile();
        File xmlFile = Paths.get("documents", xmlFileName).toFile();

        boolean isValid = validateXsd(xsdFile, xmlFile);
        System.out.printf("%s is %s against %s%n", xmlFileName, isValid ? "valid" : "invalid", xsdFileName);
    }

    private static boolean validateXsd(File xsdFile, File xmlFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
        } catch (IOException | SAXException e) {
            System.out.printf("Exception: %s%n", e.getMessage());
            return false;
        }

        return true;
    }
}
