package com.example.camelmicroservicesb.routes.L4;

import com.example.camelmicroservicesb.domain.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

//@Component
public class KafkaReceiverRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("kafka:myKafkaTopic")
				.log("${body}")
				.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
				.to("log:kafka-message");
	}
}
