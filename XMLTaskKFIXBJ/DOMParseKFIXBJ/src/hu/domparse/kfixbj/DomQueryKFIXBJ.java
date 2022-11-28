package hu.domparse.kfixbj;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class DomQueryKFIXBJ {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Document doc = DomReadKFIXBJ.parseXMLFile(new File("../XMLKFIXBJ.xml"));

        // Utazás lekérdezése jegy ID-ja alapján
        query1(doc, "j1");

        // Mely vonat érint adott állomást
        query2(doc, "a3");
    }

    public static void query1(Document doc, String jid) {
        String indent = "  ";

        Element jegy = getElementById(doc, "jegy", "jid", jid);

        String uid = jegy.getElementsByTagName("utas").item(0).getTextContent();
        Element utas = getElementById(doc, "utas", "uid", uid);
        String utasNev = utas.getElementsByTagName("nev").item(0).getTextContent();

        String aid1 = jegy.getElementsByTagName("allomas1").item(0).getTextContent();
        String aid2 = jegy.getElementsByTagName("allomas2").item(0).getTextContent();
        Element allomas1 = getElementById(doc, "allomas", "aid", aid1);
        Element allomas2 = getElementById(doc, "allomas", "aid", aid2);
        String allomasNev1 = allomas1.getElementsByTagName("nev").item(0).getTextContent();
        String allomasNev2 = allomas2.getElementsByTagName("nev").item(0).getTextContent();

        System.out.printf("%s (ID: %s) utazása:%n", utasNev, uid);
        System.out.printf("%sIndulási állomás:%s%n", indent, allomasNev1);
        System.out.printf("%sÉrkezési állomás:%s%n", indent, allomasNev2);

        System.out.println();
    }

    public static void query2(Document doc, String aid) {
        Element allomas = getElementById(doc, "allomas", "aid", aid);
        String allomasNev = allomas.getElementsByTagName("nev").item(0).getTextContent();

        NodeList erintiElements = doc.getElementsByTagName("erinti");

        for (int i = 0; i < erintiElements.getLength(); i++) {
            Element erinti = (Element) erintiElements.item(i);

            if (erinti.getAttribute("aid").equals(aid)) {
                String vid = erinti.getAttribute("vid");
                boolean vegallomas = Boolean.parseBoolean(
                        erinti.getElementsByTagName("vegallomas").item(0).getTextContent()
                );
                System.out.printf(
                        "%s vonat érinti %s állomást %s%n",
                        vid, allomasNev,
                        vegallomas ? "(végállomás)" : ""
                );
            }
        }

        System.out.println();
    }

    public static Element getElementById(Document doc, String tagName, String attribute, String id) {
        NodeList nodeList = doc.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                if (elem.getTagName().equals(tagName) && elem.getAttribute(attribute).equals(id)) {
                    return elem;
                }
            }
        }

        return null;
    }
}
