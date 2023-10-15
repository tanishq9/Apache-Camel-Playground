package com.example.camelmicroservicesa.routes.L1;

import java.time.LocalDateTime;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyFirstTimerRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// in configure method, we will create the route
		// camel is an integration framework

		// from("timer:first-timer") // timer
		//		.to("log:first-timer"); // log

		// timer will generate the below message every second
		// Exchange[ExchangePattern: InOnly, BodyType: null, Body: [Body is null]]

		from("timer:first-timer") // timer
				.routeId("timerRouteTest")
				.transform().constant("My constant message")
				//.transform().constant("Time now is: " + LocalDateTime.now()) // the constant created here will be used throughout
				.to("log:first-timer22"); // log

		// Route: route1 started and consuming from: timer://first-timer
		// Total 1 routes, of which 1 are started
		// Exchange[ExchangePattern: InOnly, BodyType: String, Body: My constant message]
	}
}
