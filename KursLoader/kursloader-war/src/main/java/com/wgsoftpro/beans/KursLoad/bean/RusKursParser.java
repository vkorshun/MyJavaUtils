package com.wgsoftpro.beans.KursLoad.bean;

import com.wgsoftpro.beans.KursLoad.WSDL.DailyInfo.Attribute;
import com.wgsoftpro.beans.KursLoad.model.RusKursItem;
import com.wgsoftpro.beans.KursLoad.model.RusKursList;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Slf4j
public class RusKursParser {

   private RusKursList rusKursList;

   public RusKursParser() {
      rusKursList = new RusKursList();
   }

   private void parseNode(Node node) {
      try {
         if (node != null) {
            if ("ValCurs".equals(node.getNodeName())) {
               String _data = node.getAttributes().getNamedItem("Date").getNodeValue();
               rusKursList.setData(new Timestamp(new SimpleDateFormat("dd.MM.yyyy").parse(_data).getTime()));
            } else if ("Valute".equals(node.getNodeName())) {
               rusKursList.getList().add(new RusKursItem());
               rusKursList.getList().get(rusKursList.getList().size() - 1).setId(node.getAttributes().getNamedItem("ID").getNodeValue());
            } else if ("NumCode".equals(node.getNodeName())) {
               rusKursList.getList().get(rusKursList.getList().size() - 1).setNumcode(node.getTextContent());
            } else if ("CharCode".equals(node.getNodeName())) {
               rusKursList.getList().get(rusKursList.getList().size() - 1).setCharcode(node.getTextContent());
            } else if ("Nominal".equals(node.getNodeName())) {
               rusKursList.getList().get(rusKursList.getList().size() - 1).setNominal(new Integer(node.getTextContent()));
            } else if ("Name".equals(node.getNodeName())) {
               rusKursList.getList().get(rusKursList.getList().size() - 1).setName(node.getTextContent());
            } else if ("Value".equals(node.getNodeName())) {
               rusKursList.getList().get(rusKursList.getList().size() - 1).setValue(
                     new BigDecimal(node.getTextContent().replace(',','.')));
            }

            if (node.hasChildNodes()) {
               NodeList nodeList = node.getChildNodes();
               for (int i = 0; i < nodeList.getLength(); ++i) {
                  parseNode(nodeList.item(i));
               }
            }
         }
      } catch (Exception ex) {
         log.error(ex.getMessage());
         throw new RuntimeException(ex);
      }
   }


   public RusKursList parseXml(String xml) {
      RusKursItem rusKursItem;

      DocumentBuilder dBuilder = null;
      try {
         dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
         parseNode(doc);
      } catch (Exception e) {
         log.error(e.getMessage());
         throw new RuntimeException(e);
      }
      return rusKursList;
   }

}