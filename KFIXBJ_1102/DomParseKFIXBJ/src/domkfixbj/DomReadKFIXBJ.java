package domkfixbj;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class DomReadKFIXBJ {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File("usersKFIXBJ.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        System.out.printf("Root element: %s%n", doc.getDocumentElement().getTagName());

        NodeList nList = doc.getDocumentElement().getChildNodes();

        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;

                String uid = elem.getAttribute("id");

                Node firstname = elem.getElementsByTagName("firstname").item(0);
                Node lastname = elem.getElementsByTagName("lastname").item(0);
                Node profession = elem.getElementsByTagName("profession").item(0);

                System.out.printf("User %s:%n", uid);
                System.out.printf("\tFirstname: %s%n", firstname.getTextContent());
                System.out.printf("\tLastname: %s%n", lastname.getTextContent());
                System.out.printf("\tProfession: %s%n", profession.getTextContent());
            }
        }
    }
}
