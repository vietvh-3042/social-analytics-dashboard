package com.learning.socialanalytics.endpoint;

import com.learning.socialanalytics.constants.SoapConstants;
import javax.xml.transform.Source;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.xml.transform.StringSource;

@Endpoint
public class MockExchangeRateEndpoint {
    @PayloadRoot(namespace = SoapConstants.NAMESPACE, localPart = "GetExchangeRateRequest")
    @ResponsePayload
    public Source getExchangeRate(@RequestPayload Source request) {
        return new StringSource(
                "<ns:GetExchangeRateResponse xmlns:ns=\""
                        + SoapConstants.NAMESPACE
                        + "\"><ns:from>USD</ns:from><ns:to>USD</ns:to><ns:rate>1.0</ns:rate>"
                        + "<ns:source>local-demo</ns:source></ns:GetExchangeRateResponse>");
    }
}
