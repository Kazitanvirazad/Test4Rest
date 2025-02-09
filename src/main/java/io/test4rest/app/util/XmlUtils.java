package io.test4rest.app.util;

import io.test4rest.app.model.ApiResponse;
import io.test4rest.app.model.KeyValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.Serial;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import static io.test4rest.app.constants.CommonConstants.EMPTY_STRING;
import static io.test4rest.app.constants.CommonConstants.XML_DECLARATION;
import static io.test4rest.app.constants.http.HttpHeaders.CONTENT_TYPE;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_XML;
import static io.test4rest.app.constants.http.MediaType.APPLICATION_XML_UTF8;

public class XmlUtils {
    private final static Logger log = LogManager.getLogger(XmlUtils.class);
    private final static Set<String> XML_HEADER_MAPPER = new HashSet<>() {
        @Serial
        private static final long serialVersionUID = -3689437380750558933L;

        {
            add(APPLICATION_XML.toLowerCase());
            add(APPLICATION_XML_UTF8[0].toLowerCase());
            add(APPLICATION_XML_UTF8[1].toLowerCase());
        }
    };

    public static String prettifyXml(String xml) {
        boolean ignoreDeclaration = !xml.startsWith(XML_DECLARATION);
        try {
            InputSource source = new InputSource(new StringReader(xml));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(source);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer(new StreamSource(new StringReader(prettyPrintXslt())));
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, ignoreDeclaration ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            Writer output = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(output));
            return output.toString();
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return EMPTY_STRING;
    }

    private static String prettyPrintXslt() {
        return """
                <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                    <xsl:strip-space elements="*"/>
                    <xsl:output method="xml" encoding="UTF-8"/>

                    <xsl:template match="@*|node()">
                        <xsl:copy>
                            <xsl:apply-templates select="@*|node()"/>
                        </xsl:copy>
                    </xsl:template>

                </xsl:stylesheet>""";
    }

    public static boolean isXmlResponse(ApiResponse response) {
        return response != null &&
                !response.getHeaders().isEmpty() &&
                mapXmlHeader(response);
    }

    private static boolean mapXmlHeader(ApiResponse response) {
        for (KeyValue keyValue : response.getHeaders()) {
            if (StringUtils.hasText(keyValue.getKey()) && StringUtils.hasText(keyValue.getValue())) {
                if (CONTENT_TYPE.equalsIgnoreCase(keyValue.getKey().trim()) && XML_HEADER_MAPPER.contains(keyValue.getValue().trim().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
