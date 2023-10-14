package com.example.camelmicroservicesa.routes.L9;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ChoiceRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file://files/input")
				.routeId("Files-Input-Route") // Assign id to route, could be useful in debugging
				.transform().body(String.class)
				.choice()
					.when(simple("${file:ext} ends with 'xml'")) // check for file extension using simple language
						.log("XML file")
					.when(simple("${body} contains 'USD'"))
						.log("Not an XML file but contains USD")
					.otherwise()
						.log("Not an XML file")
				.end()
				.log("${body}")
				.log("${messageHistory} ${headers}")
				.to("log:logs");
	}
}
