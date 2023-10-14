package com.example.camelmicroservicesa.routes.L5;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqSenderRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// timer, put message in regular interval to queue
		/*from("timer:active-mq-timer?period=10000") // configuring time period as 10s instead of 1s default
				.transform().constant("My message for ActiveMQ")
				.log("${body}")
				.to("activemq:my-activemq-queue");*/

		from("file:files/json")
				.log("${body}")
				.to("activemq:my-activemq-queue");
	}
}
