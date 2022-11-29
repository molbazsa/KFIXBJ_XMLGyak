package hu.domparse.kfixbj;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class DomQueryKFIXBJ {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Document doc = DomReadKFIXBJ.parseXMLFile(new File("../XMLKFIXBJ.xml"), new File("../XMLSchemaKFIXBJ.xsd"));

        String jid;
        String aid;
        String vid;

        // Utazás lekérdezése jegy ID-ja alapján
        jid = "j1";
        query1(doc, jid);

        // Mely vonat érint adott állomást
        aid = "a3";
        query2(doc, aid);

        // Adott állomást mely napokon érint vonat
        aid = "a2";
        query3(doc, aid);

        // Adott vonat menetideje
        vid = "v3";
        query4(doc, vid);

        // Állomások, amelyek 165 km-nél közelebb vannak
        query5(doc);
    }

    private static void query1(Document doc, String jid) {
        String indent = "  ";

        Element jegy = getElementById(doc, "jegy", "jid", jid);

        String uid = firstChildTextContent(jegy, "utas");
        Element utas = getElementById(doc, "utas", "uid", uid);
        String utasNev = firstChildTextContent(utas, "nev");

        String aid1 = firstChildTextContent(jegy, "allomas1");
        String aid2 = firstChildTextContent(jegy, "allomas2");
        Element allomas1 = getElementById(doc, "allomas", "aid", aid1);
        Element allomas2 = getElementById(doc, "allomas", "aid", aid2);
        String allomasNev1 = firstChildTextContent(allomas1, "nev");
        String allomasNev2 = firstChildTextContent(allomas2, "nev");

        System.out.printf("%s (ID: %s) utazása:%n", utasNev, uid);
        System.out.printf("%sIndulási állomás: %s%n", indent, allomasNev1);
        System.out.printf("%sÉrkezési állomás: %s%n", indent, allomasNev2);

        System.out.println();
    }

    private static void query2(Document doc, String aid) {
        Element allomas = getElementById(doc, "allomas", "aid", aid);
        String allomasNev = firstChildTextContent(allomas, "nev");

        NodeList erintiElements = doc.getElementsByTagName("erinti");

        for (int i = 0; i < erintiElements.getLength(); i++) {
            Element erinti = (Element) erintiElements.item(i);

            if (erinti.getAttribute("aid").equals(aid)) {
                String vid = erinti.getAttribute("vid");
                boolean vegallomas = Boolean.parseBoolean(firstChildTextContent(erinti, "vegallomas"));

                System.out.printf(
                    "%s vonat érinti %s állomást %s%n",
                    vid, allomasNev,
                    vegallomas ? "(végállomás)" : ""
                );
            }
        }

        System.out.println();
    }

    private static void query3(Document doc, String aid) {
        Element allomas = getElementById(doc, "allomas", "aid", aid);
        String allomasNev = firstChildTextContent(allomas, "nev");

        List<LocalDate> dates = new ArrayList<>();
        NodeList erintiElements = doc.getElementsByTagName("erinti");

        for (int i = 0; i < erintiElements.getLength(); i++) {
            Element erinti = (Element) erintiElements.item(i);

            if (erinti.getAttribute("aid").equals(aid)) {
                String vid = erinti.getAttribute("vid");
                Element vonat = getElementById(doc, "vonat", "vid", vid);
                LocalDateTime dateTime = LocalDateTime.parse(firstChildTextContent(vonat, "indulas"));
                LocalDate date = dateTime.toLocalDate();

                if (!dates.contains(date)) {
                    dates.add(date);
                    System.out.printf(
                        "%s állomást érinti vonat %tY.%tm.%td. napon%n",
                        allomasNev, date, date, date
                    );
                }
            }
        }

        System.out.println();
    }

    private static void query4(Document doc, String vid) {
        Element vonat = getElementById(doc, "vonat", "vid", vid);

        LocalDateTime indulas = LocalDateTime.parse(firstChildTextContent(vonat, "indulas"));
        LocalDateTime erkezes = LocalDateTime.parse(firstChildTextContent(vonat, "erkezes"));
        Duration menetido = Duration.between(indulas, erkezes);

        System.out.printf(
            "%s vonat menetideje: %02d:%02d:%02d%n",
            vid, menetido.toHoursPart(), menetido.toMinutesPart(), menetido.toSecondsPart()
        );

        System.out.println();
    }

    private static void query5(Document doc) {
        NodeList tavolsagaElements = doc.getElementsByTagName("tavolsaga");

        for (int i = 0; i < tavolsagaElements.getLength(); i++) {
            Element tavolsaga = (Element) tavolsagaElements.item(i);
            int tavolsag = Integer.parseInt(firstChildTextContent(tavolsaga, "tavolsag"));

            if (tavolsag < 165) {
                String aid1 = tavolsaga.getAttribute("aid1");
                String aid2 = tavolsaga.getAttribute("aid2");
                Element allomas1 = getElementById(doc, "allomas", "aid", aid1);
                Element allomas2 = getElementById(doc, "allomas", "aid", aid2);
                String allomasNev1 = firstChildTextContent(allomas1, "nev");
                String allomasNev2 = firstChildTextContent(allomas2, "nev");

                System.out.printf("%s - %s: %d km%n", allomasNev1, allomasNev2, tavolsag);
            }
        }

        System.out.println();
    }

    public static Element getElementById(Document doc, String tagName, String attribute, String id) {
        NodeList elements = doc.getElementsByTagName(tagName);

        for (int i = 0; i < elements.getLength(); i++) {
            Element elem = (Element) elements.item(i);

            if (elem.getAttribute(attribute).equals(id)) {
                return elem;
            }
        }

        return null;
    }

    public static Element firstChild(Element elem, String tagName) {
        return (Element) elem.getElementsByTagName(tagName).item(0);
    }

    public static String firstChildTextContent(Element elem, String tagName) {
        return firstChild(elem, tagName).getTextContent();
    }
}
