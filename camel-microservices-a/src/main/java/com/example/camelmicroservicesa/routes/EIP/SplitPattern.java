package com.example.camelmicroservicesa.routes.EIP;

import java.util.List;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// In Apache Camel, a "split pattern" refers to a design pattern used to split a message into multiple parts, often to process each part independently.
// This pattern is particularly useful when dealing with messages that contain collections or batches of data and you need to process each element of the collection individually.
//@Component
public class SplitPattern extends RouteBuilder {

	@Autowired
	SplitterComponent splitterComponent;

	@Override
	public void configure() throws Exception {
		from("file:files/csv")
				.convertBodyTo(String.class)
				//.transform().body(String.class)
				//.unmarshal().csv()
				//.split(body()) --> First way to split (line is delimiter)

				//.split(body(), ",") --> Second way to split using ',' i.e. custom delimiter

				.split(method(splitterComponent)) // --> Third way is to use custom splitter, here in we can write custom logic to split
				.to("log:split-files");
	}
}

@Component
class SplitterComponent {
	public List<String> splitInput(String body) {
		// Assume some custom logic was executed on message body and the below list was returned as an outcome
		return List.of("ABC", "DEF", "GHI");
	}
}
