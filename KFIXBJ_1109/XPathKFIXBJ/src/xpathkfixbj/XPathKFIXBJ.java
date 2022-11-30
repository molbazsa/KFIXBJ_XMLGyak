package xpathkfixbj;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class XPathKFIXBJ {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse("studentKFIXBJ.xml");
            document.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            // String expression = "/class/student";
            // String expression = "/class/student[@id='s2']";
            // String expression = "//student";
            // String expression = "/class/student[2]";
            // String expression = "/class/student[last()]";
            // String expression = "/class/student[last()-1]";
            // String expression = "/class/student[position()<3]";
            // String expression = "/class/*";
            // String expression = "//student[@*]";
            // String expression = "//*";
            // String expression = "/class/student[kor>20]";
            String expression = "//student/keresztnev | //student/vezeteknev";

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.printf("%nAktuális elem: %s%n", node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("student")) {
                    Element elem = (Element) node;

                    System.out.printf("Hallgató ID: %s%n", elem.getAttribute("id"));

                    System.out.printf(
                        "Keresztnév: %s%n",
                        elem.getElementsByTagName("keresztnev").item(0).getTextContent()
                    );

                    System.out.printf(
                        "Vezetéknév: %s%n",
                        elem.getElementsByTagName("vezeteknev").item(0).getTextContent()
                    );

                    System.out.printf(
                        "Becenév: %s%n",
                        elem.getElementsByTagName("becenev").item(0).getTextContent()
                    );

                    System.out.printf(
                        "Kor: %s%n",
                        elem.getElementsByTagName("kor").item(0).getTextContent()
                    );
                }
            }
        } catch (
            ParserConfigurationException
            | IOException
            | SAXException
            | XPathExpressionException exception
        ) {
            exception.printStackTrace();
        }
    }
}
