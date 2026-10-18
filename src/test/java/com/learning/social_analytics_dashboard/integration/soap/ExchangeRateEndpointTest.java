package com.learning.social_analytics_dashboard.integration.soap;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

@SpringBootTest
class ExchangeRateEndpointTest {
    @Autowired ApplicationContext applicationContext;

    @Test
    void returnsMockUsdExchangeRate() {
        MockWebServiceClient client = MockWebServiceClient.createClient(applicationContext);

        client.sendRequest(withPayload(new StringSource("""
                <GetExchangeRateRequest xmlns="http://social-analytics.example.com/exchange">
                    <currency>USD</currency>
                </GetExchangeRateRequest>
                """)))
                .andExpect(xpath("//*[local-name()='currency']").evaluatesTo("USD"))
                .andExpect(xpath("//*[local-name()='rate']").evaluatesTo("1.0"));
    }
}
