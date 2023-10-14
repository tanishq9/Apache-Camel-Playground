package com.example.camelmicroservicesa.routes.L3;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

// An alternative to using bean for processing (by returning void from bean method), is to make use of .process() method.
// @Component
public class CustomProcessorForMessageProcessing extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("timer:first-time")
				.setBody().simple("dummyPayload")
				.log("${body}")
				.process(new SimpleProcessor())
				.log("${body}") // there should be no change after processing
				.to("log:first-time");
	}

}

class SimpleProcessor implements Processor {
	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Printing exchange object: " + exchange.toString());
		System.out.println("Printing message body using exchange object: " + exchange.getMessage().getBody());
	}
}
