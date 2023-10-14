package com.example.camelmicroservicesb.routes.L1;

import com.example.camelmicroservicesb.domain.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

//@Component
public class ActiveMqReceiverRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Map the JSON input to POJO
		from("activemq:my-activemq-queue")
				.log("${body}")
				.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class) // unmarshal from json structure to pojo, unmarshal is same as deserialize
				.log("${body}") // CurrencyExchange{id=1000, from='USD', to='INR', conversionMultiple=70}
				// Exchange[ExchangePattern: InOnly, BodyType: com.example.camelmicroservicesb.routes.CurrencyExchange, Body: CurrencyExchange{id=1000, from='USD', to='INR', conversionMultiple=70}]
				.to("log:received-message-log");
	}
}

