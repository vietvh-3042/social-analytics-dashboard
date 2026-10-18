package com.learning.social_analytics_dashboard.integration.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@EnableWs
public class SoapWebServiceConfig {
    @Bean
    ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "exchange-rate")
    DefaultWsdl11Definition exchangeRateWsdl(SimpleXsdSchema exchangeRateSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("ExchangeRatePort");
        definition.setLocationUri("/ws");
        definition.setTargetNamespace("http://social-analytics.example.com/exchange");
        definition.setSchema(exchangeRateSchema);
        return definition;
    }

    @Bean
    SimpleXsdSchema exchangeRateSchema() {
        return new SimpleXsdSchema(new ClassPathResource("exchange-rate.xsd"));
    }

    @Bean
    WebServiceTemplate exchangeRateWebServiceTemplate() {
        return new WebServiceTemplate();
    }
}
