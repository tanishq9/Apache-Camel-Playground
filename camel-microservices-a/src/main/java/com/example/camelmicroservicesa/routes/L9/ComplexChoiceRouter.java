package com.example.camelmicroservicesa.routes.L9;

import java.util.Map;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class ComplexChoiceRouter extends RouteBuilder {

	@Autowired
	private DeciderBean deciderBean;

	@Override
	public void configure() throws Exception {
		from("file:files/input")
				.routeId("files-input")
				.transform().body(String.class)
				.choice()
					.when(method(deciderBean))
						.log("Condition met")
				.end()
				.to("log:print-logs");
	}
}

@Component
class DeciderBean {
	// assume this method performs complex conditional logic
	public boolean isConditionMet(
			@Body String body,
			@Headers Map<String, Object> headers,
			@ExchangeProperties Map<String, Object> properties
	) {
		System.out.println("Message body is: " + body);
		System.out.println("Message headers is: " + headers);
		System.out.println("Exchange Properties is: " + properties);
		return true;
	}
}
