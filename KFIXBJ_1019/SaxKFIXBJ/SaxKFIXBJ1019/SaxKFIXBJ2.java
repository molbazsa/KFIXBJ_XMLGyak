package SaxKFIXBJ1019;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxKFIXBJ2 {
  public static void main(String[] args) {
    try {
      SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
      SAXParser saxParser = saxParserFactory.newSAXParser();
      SaxHandler handler = new SaxHandler();
      saxParser.parse(new File("MB_kurzusfelvetel.xml"), handler);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }
}

class SaxHandler extends DefaultHandler {
  private int indent = 0;

  private String formatAttributes(Attributes attributes) {
    int attrLength = attributes.getLength();

    if (attrLength == 0) {
      return "";
    }

    StringBuilder sb = new StringBuilder(", {");

    for (int i = 0; i < attrLength; i++) {
      sb.append(attributes.getLocalName(i) + "=" + attributes.getValue(i));

      if (i < attrLength - 1) {
        sb.append(", ");
      }
    }

    sb.append("}");
    return sb.toString();
  }

  private void indent() {
    for (int i = 0; i < indent; i++) {
      System.out.print("  ");
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    indent++;
    indent();

    switch (qName) {
      case "targy":
      case "nap":
      case "tol":
      case "ig":
      case "oraado":
      case "oktato":
        System.out.print(qName + formatAttributes(attributes) + " start" + " ");
        break;

      default:
        System.out.println(qName + formatAttributes(attributes) + " start");
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    switch (qName) {
      case "targy":
      case "nap":
      case "tol":
      case "ig":
      case "oraado":
      case "oktato":
        break;

      default:
        indent();
        break;
    }

    indent--;
    System.out.println(qName + " end");
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    String chars = new String(ch, start, length).trim();
    if (!chars.isEmpty()) {
      System.out.print(chars + " ");
    }
  }
}
