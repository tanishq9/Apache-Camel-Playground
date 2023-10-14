package com.example.camelmicroservicesa.routes.L6;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqXMLSenderRouter extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		from("file:files/xml")
				.log("${body}")
				.to("activemq:my-activemq-xml-queue");
	}
}
