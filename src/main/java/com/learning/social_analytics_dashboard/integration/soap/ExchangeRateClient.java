package com.learning.social_analytics_dashboard.integration.soap;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateClient {
    private static final String REQUEST = "<GetExchangeRateRequest xmlns=\"http://social-analytics.example.com/exchange\">"
            + "<currency>%s</currency></GetExchangeRateRequest>";
    private final WebServiceTemplate webServiceTemplate;

    public ExchangeRateClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public String getRate(String endpoint, String currency) {
        Source request = new StreamSource(new StringReader(REQUEST.formatted(escape(currency))));
        StringWriter response = new StringWriter();
        webServiceTemplate.sendSourceAndReceive(endpoint, request, null,
                message -> {
                    try {
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
                        transformerFactory.newTransformer()
                                .transform(message, new StreamResult(response));
                    } catch (Exception ex) {
                        throw new IllegalStateException("Could not read exchange-rate response", ex);
                    }
                    return null;
                });
        return response.toString();
    }

    private String escape(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&apos;");
    }
}
