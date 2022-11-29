package hu.domparse.kfixbj;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static hu.domparse.kfixbj.DomQueryKFIXBJ.*;

public class DomModifyKFIXBJ {
    public static void main(String[] args)
    throws ParserConfigurationException, TransformerException, IOException, SAXException {
        Document doc = DomReadKFIXBJ.parseXMLFile(new File("../XMLKFIXBJ.xml"), new File("../XMLSchemaKFIXBJ.xsd"));

        // Állomások távolságának megkétszerezése
        modify1(doc);

        // Jegyek árának megemelése
        modify2(doc);

        // Kocsi hozzáadása
        modify3(doc);

        // Állomás hozzárendelése vonathoz
        modify4(doc);

        // Email cím hozzáadása utashoz
        modify5(doc);

        writeXMLFile(doc, new File("../XMLKFIXBJ.mod.xml"));
    }

    private static void modify1(Document doc) {
        NodeList tavolsagaElements = doc.getElementsByTagName("tavolsaga");

        for (int i = 0; i < tavolsagaElements.getLength(); i++) {
            Element tavolsaga = (Element) tavolsagaElements.item(i);

            Element tavolsagElement = firstChild(tavolsaga, "tavolsag");
            int tavolsag = Integer.parseInt(tavolsagElement.getTextContent());
            tavolsagElement.setTextContent(Integer.valueOf(tavolsag * 2).toString());
        }
    }

    private static void modify2(Document doc) {
        NodeList jegyElements = doc.getElementsByTagName("jegy");

        for (int i = 0; i < jegyElements.getLength(); i++) {
            Element jegy = (Element) jegyElements.item(i);

            Element arElement = firstChild(jegy, "ar");
            int ar = Integer.parseInt(arElement.getTextContent());
            arElement.setTextContent(Integer.valueOf(ar + 100).toString());
        }
    }

    private static void modify3(Document doc) {
        Element vonatjegyek = doc.getDocumentElement();

        Element kocsi = doc.createElement("kocsi");
        kocsi.setAttribute("vid", "v3");
        kocsi.setAttribute("kid", "k79");

        Element kocsiosztaly = doc.createElement("kocsiosztaly");
        kocsiosztaly.setTextContent("1");
        kocsi.appendChild(kocsiosztaly);

        vonatjegyek.insertBefore(kocsi, firstChild(vonatjegyek, "kocsi"));
    }

    private static void modify4(Document doc) {
        Element vonatjegyek = doc.getDocumentElement();

        Element erinti = doc.createElement("erinti");
        erinti.setAttribute("vid", "v3");
        erinti.setAttribute("aid", "a4");

        Element vegallomas = doc.createElement("vegallomas");
        vegallomas.setTextContent("true");
        erinti.appendChild(vegallomas);

        vonatjegyek.insertBefore(erinti, firstChild(vonatjegyek, "erinti"));
    }

    private static void modify5(Document doc) {
        // Utas azonosítója
        String uid = "u3";
        Element utas = getElementById(doc, "utas", "uid", uid);

        // Utas új email címe
        String newEmail = "boros.mate@email.com";
        Element email = doc.createElement("email");
        email.setTextContent(newEmail);
        utas.insertBefore(email, firstChild(utas, "email"));
    }

    private static void writeXMLFile(Document doc, File outputFile) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource domSource = new DOMSource(doc);

        StreamResult console = new StreamResult(System.out);
        StreamResult file = new StreamResult(outputFile);

        transformer.transform(domSource, console);
        transformer.transform(domSource, file);
    }
}
