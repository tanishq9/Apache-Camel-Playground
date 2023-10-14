package com.example.camelmicroservicesa.routes.L8;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RestApiConsumerRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		restConfiguration().host("localhost").port(8000);
		from("timer:rest-api-consumer?period=10000")
				.setHeader("from", () -> "USD") // setting the message header, can be used later in route
				.setHeader("to", () -> "INR")
				.log("${body}")
				.to("rest:get:/currency-exchange/{from}/{to}")
				.log("${body}"); // http call response body
	}
}
