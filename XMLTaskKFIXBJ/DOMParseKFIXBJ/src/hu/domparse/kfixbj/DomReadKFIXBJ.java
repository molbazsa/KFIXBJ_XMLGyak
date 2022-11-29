package hu.domparse.kfixbj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class DomReadKFIXBJ {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Document doc = parseXMLFile(new File("../XMLKFIXBJ.xml"), new File("../XMLSchemaKFIXBJ.xsd"));
        printDocument(doc);

        PrintStream out = new PrintStream(new FileOutputStream("XMLKFIXBJ.txt"));
        System.setOut(out);
        printDocument(doc);
    }

    public static Document parseXMLFile(File xmlFile, File xsdFile)
    throws ParserConfigurationException, IOException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(xsdFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setSchema(schema);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        return doc;
    }

    private static void printDocument(Document doc) {
        System.out.println("Vonatjegy nyilvántartás");
        System.out.println("=======================");
        System.out.println();

        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                switch (elem.getTagName()) {
                    case "allomas" -> printAllomas(elem);
                    case "tavolsaga" -> printTavolsaga(elem);
                    case "vonat" -> printVonat(elem);
                    case "kocsi" -> printKocsi(elem);
                    case "erinti" -> printErinti(elem);
                    case "utas" -> printUtas(elem);
                    case "jegy" -> printJegy(elem);
                }
            }
        }
    }

    private static void printAllomas(Element elem) {
        System.out.println("Állomás");
        System.out.println("-------");

        String id = elem.getAttribute("aid");

        System.out.printf("ID: %s%n", id);

        String iranyitoszam = elem.getElementsByTagName("iranyitoszam").item(0).getTextContent();
        String utca = elem.getElementsByTagName("utca").item(0).getTextContent();
        String hazszam = elem.getElementsByTagName("hazszam").item(0).getTextContent();
        String nev = elem.getElementsByTagName("nev").item(0).getTextContent();

        System.out.printf("Irányítószám: %s%n", iranyitoszam);
        System.out.printf("Utca: %s%n", utca);
        System.out.printf("Házszám: %s%n", hazszam);
        System.out.printf("Név: %s%n", nev);

        System.out.println();
    }

    private static void printTavolsaga(Element elem) {
        String aid1 = elem.getAttribute("aid1");
        String aid2 = elem.getAttribute("aid2");

        String tavolsag = elem.getElementsByTagName("tavolsag").item(0).getTextContent();

        System.out.printf("%s és %s állomás távolsága: %s%n", aid1, aid2, tavolsag);

        System.out.println();
    }

    private static void printVonat(Element elem) {
        System.out.println("Vonat");
        System.out.println("-----");

        String id = elem.getAttribute("vid");

        System.out.printf("ID: %s%n", id);

        String vonalszam = elem.getElementsByTagName("vonalszam").item(0).getTextContent();
        String indulas = elem.getElementsByTagName("indulas").item(0).getTextContent();
        String erkezes = elem.getElementsByTagName("erkezes").item(0).getTextContent();
        String tavolsag = elem.getElementsByTagName("tavolsag").item(0).getTextContent();
        String helyjegyKoteles = elem.getElementsByTagName("helyjegy-koteles").item(0).getTextContent();

        System.out.printf("Vonalszám: %s%n", vonalszam);
        System.out.printf("Indulás: %s%n", indulas);
        System.out.printf("Érkezés: %s%n", erkezes);
        System.out.printf("Távolság: %s%n", tavolsag);
        System.out.printf("Helyjegy köteles: %s%n", helyjegyKoteles);

        System.out.println();
    }

    private static void printKocsi(Element elem) {
        System.out.println("Kocsi");
        System.out.println("-----");

        String vid = elem.getAttribute("vid");
        String kid = elem.getAttribute("kid");

        System.out.printf("Vonat ID: %s%n", vid);
        System.out.printf("Kocsi ID: %s%n", kid);

        String kocsiosztaly = elem.getElementsByTagName("kocsiosztaly").item(0).getTextContent();

        System.out.printf("Kocsiosztály: %s%n", kocsiosztaly);

        System.out.println();
    }

    private static void printErinti(Element elem) {
        String vid = elem.getAttribute("vid");
        String aid = elem.getAttribute("aid");

        String vegallomas = elem.getElementsByTagName("vegallomas").item(0).getTextContent();

        System.out.printf("%s vonat érinti %s állomást (végállomás: %s)%n", vid, aid, vegallomas);

        System.out.println();
    }

    private static void printUtas(Element elem) {
        System.out.println("Utas");
        System.out.println("----");

        String id = elem.getAttribute("uid");

        System.out.printf("ID: %s%n", id);

        String nev = elem.getElementsByTagName("nev").item(0).getTextContent();

        System.out.printf("Név: %s%n", nev);

        NodeList emails = elem.getElementsByTagName("email");

        for (int i = 0; i < emails.getLength(); i++) {
            String email = emails.item(0).getTextContent();
            System.out.printf("Email: %s%n", email);
        }

        System.out.println();
    }

    private static void printJegy(Element elem) {
        System.out.println("Jegy");
        System.out.println("----");

        String id = elem.getAttribute("jid");

        System.out.printf("ID: %s%n", id);

        String utas = elem.getElementsByTagName("utas").item(0).getTextContent();
        String vonat = elem.getElementsByTagName("vonat").item(0).getTextContent();
        String allomas1 = elem.getElementsByTagName("allomas1").item(0).getTextContent();
        String allomas2 = elem.getElementsByTagName("allomas2").item(0).getTextContent();
        String ar = elem.getElementsByTagName("ar").item(0).getTextContent();

        System.out.printf("Utas: %s%n", utas);
        System.out.printf("Vonat: %s%n", vonat);
        System.out.printf("Indulási állomás: %s%n", allomas1);
        System.out.printf("Érkezési állomás: %s%n", allomas2);
        System.out.printf("Ár: %s%n", ar);

        Node helyjegy = elem.getElementsByTagName("helyjegy").item(0);

        if (helyjegy != null) {
            Element helyjegyElem = (Element) helyjegy;
            printHelyjegy(helyjegyElem);
        }

        System.out.println();

    }

    private static void printHelyjegy(Element elem) {
        String indent = "  ";

        System.out.println("Helyjegy:");

        String id = elem.getAttribute("hid");

        System.out.printf("%sID: %s%n", indent, id);

        String kocsi = elem.getElementsByTagName("kocsi").item(0).getTextContent();
        String ulesszam = elem.getElementsByTagName("ulesszam").item(0).getTextContent();
        String ar = elem.getElementsByTagName("ar").item(0).getTextContent();

        System.out.printf("%sKocsi: %s%n", indent, kocsi);
        System.out.printf("%sÜlésszám: %s%n", indent, ulesszam);
        System.out.printf("%sÁr: %s%n", indent, ar);
    }
}
