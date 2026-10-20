package com.learning.socialanalytics.config;

import com.learning.socialanalytics.constants.SoapConstants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapConfig {
    @Bean
    ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext context) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/soap/*");
    }

    @Bean
    XsdSchema analyticsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap/analytics.xsd"));
    }

    @Bean(name = SoapConstants.SERVICE_NAME)
    DefaultWsdl11Definition analyticsWsdl(XsdSchema analyticsSchema) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("MockExchangeRatePort");
        definition.setLocationUri("/soap");
        definition.setTargetNamespace(SoapConstants.NAMESPACE);
        definition.setSchema(analyticsSchema);
        return definition;
    }
}
