package com.example.camelmicroservicesb.routes.L3;

import com.example.camelmicroservicesb.domain.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqXMLReceiverRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("activemq:my-activemq-xml-queue")
				.log("${body}")
				.unmarshal()
				.jacksonXml(CurrencyExchange.class)
				.log("${body}") // CurrencyExchange{id=1000, from='USD', to='INR', conversionMultiple=70}
				.to("log:received-message-log"); // Exchange[ExchangePattern: InOnly, BodyType: com.example.camelmicroservicesb.domain.CurrencyExchange, Body: CurrencyExchange{id=1000, from='USD', to='INR', conversionMultiple=70}]
	}
}
