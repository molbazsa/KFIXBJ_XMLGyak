import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String nev = "Tóth Eszter";
        String neptunkod = "AABB12";
        String szak = "vegyészmérnök";

        Student student = new Student(nev, neptunkod, szak);
        System.out.println(student.toJson());

        Document doc = parseXMLFile(new File("JSONKFIXBJ.xml"));
        Element studentElement = doc.getDocumentElement();

        nev = firstChildTextContent(studentElement, "nev");
        neptunkod = firstChildTextContent(studentElement, "neptunkod");
        szak = firstChildTextContent(studentElement, "szak");

        student = new Student(nev, neptunkod, szak);
        System.out.println(student.toJson());
    }

    public static Document parseXMLFile(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        return doc;
    }

    public static Element firstChild(Element elem, String tagName) {
        return (Element) elem.getElementsByTagName(tagName).item(0);
    }

    public static String firstChildTextContent(Element elem, String tagName) {
        return firstChild(elem, tagName).getTextContent();
    }
}
