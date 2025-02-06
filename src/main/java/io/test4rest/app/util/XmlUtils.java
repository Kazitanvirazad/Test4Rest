package io.test4rest.app.util;

import io.test4rest.app.model.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.XML_DECLARATION;
import static io.test4rest.app.constants.http.HttpHeaders.CONTENT_TYPE;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_XML;

public class XmlUtils {
    private final static Logger log = LogManager.getLogger(XmlUtils.class);

    public static String prettifyXml(String xml) {
        try {
            final InputSource inputSource = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputSource).getDocumentElement();
            final Boolean keepDeclaration = xml.startsWith(XML_DECLARATION);

            // System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");

            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration);

            return writer.writeToString(document);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return EMPTY_STRING;
    }

    public static boolean isXmlResponse(ApiResponse response) {
        return response != null &&
                !response.getHeaders().isEmpty() &&
                response.getHeaders().containsKey(CONTENT_TYPE) &&
                APPLICATION_XML.equalsIgnoreCase(response.getHeaders().get(CONTENT_TYPE));
    }
}
