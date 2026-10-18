package com.learning.social_analytics_dashboard.integration.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

@Endpoint
public class ExchangeRateEndpoint {
    private static final String NAMESPACE = "http://social-analytics.example.com/exchange";

    @PayloadRoot(namespace = NAMESPACE, localPart = "GetExchangeRateRequest")
    @ResponsePayload
    public Source exchangeRate(@RequestPayload Source request) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            Document document = factory.newDocumentBuilder().newDocument();
            Element response = document.createElementNS(NAMESPACE, "GetExchangeRateResponse");
            append(document, response, "currency", "USD");
            append(document, response, "rate", "1.0");
            document.appendChild(response);
            return new DOMSource(document);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not build exchange-rate response", ex);
        }
    }

    private void append(Document document, Element parent, String name, String value) {
        Element element = document.createElementNS(NAMESPACE, name);
        element.setTextContent(value);
        parent.appendChild(element);
    }
}
