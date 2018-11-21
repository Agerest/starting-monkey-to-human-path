package RPIS61.Kres.wdad.data.managers;

import RPIS61.Kres.wdad.utils.PreferencesManagerConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PreferencesManager {

    private static PreferencesManager instance;
    public static final String XML_PATH = "src\\RPIS61\\Kres\\wdad\\resources\\configuration\\appconfig.xml";
    private Document document;
    private XPath path;
    private Properties properties;

    private PreferencesManager() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(XML_PATH);
            XPathFactory xpathfactory = XPathFactory.newInstance();
            this.path = xpathfactory.newXPath();
            this.properties = new Properties();
            readProperties();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static PreferencesManager getInstance() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private void saveXML() {
        try {
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(new File(XML_PATH)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public void setCreateRegistry(String createRegistry) {
        document.getElementsByTagName("createregistry").item(0).setTextContent(createRegistry);
        saveXML();
    }

    @Deprecated
    public String getCreateRegistry() {
        return document.getElementsByTagName("createregistry").item(0).getTextContent();
    }

    @Deprecated
    public void setRegistryAddress(String registryAddress) {
        document.getElementsByTagName("registryaddress").item(0).setTextContent(registryAddress);
        saveXML();
    }

    @Deprecated
    public String getRegistryAddress() {
        return document.getElementsByTagName("registryaddress").item(0).getTextContent();
    }

    @Deprecated
    public void setRegistryPort(int registryPort) {
        document.getElementsByTagName("registryport").item(0).setTextContent(Integer.toString(registryPort));
        saveXML();
    }

    @Deprecated
    public int getRegistryPort() {
        return Integer.parseInt(document.getElementsByTagName("registryport").item(0).getTextContent());
    }

    @Deprecated
    public void setPolicyPath(String policyPath) {
        document.getElementsByTagName("policypath").item(0).setTextContent(policyPath);
        saveXML();
    }

    @Deprecated
    public String getPolicyPath() {
        return document.getElementsByTagName("policypath").item(0).getTextContent();
    }

    @Deprecated
    public void setUseCodeBaseOnly(String useCodeBaseOnly) {
        document.getElementsByTagName("usecodebaseonly").item(0).setTextContent(useCodeBaseOnly);
        saveXML();
    }

    @Deprecated
    public String getUseCodeBaseOnly() {
        return document.getElementsByTagName("usecodebaseonly").item(0).getTextContent();
    }

    @Deprecated
    public void setClassProvider(String classProvider) {
        document.getElementsByTagName("classprovider").item(0).setTextContent(classProvider);
        saveXML();
    }

    @Deprecated
    public String getClassProvider() {
        return document.getElementsByTagName("classprovider").item(0).getTextContent();
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperties(Properties prop) {
        prop.stringPropertyNames().forEach(property -> setProperty(property,prop.getProperty(property)));
    }

    public Properties getProperties() {
        return properties;
    }

    private void readProperties() {
        String[] keys = {PreferencesManagerConstants.classprovider,PreferencesManagerConstants.createregistry,
                PreferencesManagerConstants.policypath, PreferencesManagerConstants.registryaddress,
                PreferencesManagerConstants.usecodebaseonly, PreferencesManagerConstants.registryport,
                PreferencesManagerConstants.classname, PreferencesManagerConstants.DBName,
                PreferencesManagerConstants.drivertype, PreferencesManagerConstants.pass,
                PreferencesManagerConstants.hostName, PreferencesManagerConstants.port,
                PreferencesManagerConstants.user};
        for(String key : keys){
            try {
                properties.setProperty(key,path.evaluate(key,document));
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        }
    }

    public void addBindedObject(String name, String className) {
        Element element = document.createElement("bindedobject");
        element.setAttribute("name",name);
        element.setAttribute("class", className);
        document.getElementsByTagName("server").item(0).appendChild(element);
        saveXML();
    }

    public void removeBindedObject(String name) {
        NodeList nodeList = document.getElementsByTagName("bindedobject");
        Element element;
        for(int i=0;i<nodeList.getLength();i++){
            element = (Element) nodeList.item(i);
            if(element.getAttribute("name").equals(name)){
                element.getParentNode().removeChild(element);
            }
        }
        saveXML();
    }

}
