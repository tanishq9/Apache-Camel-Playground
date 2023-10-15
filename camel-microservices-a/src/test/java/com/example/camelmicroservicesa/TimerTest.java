package com.example.camelmicroservicesa;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
public class TimerTest {

	@Autowired
	CamelContext camelContext;

	// This annotation allows you to inject endpoints into your test classes for the purpose of testing and verifying the behavior of your existing Camel routes.
	// The injected MockEndpoint in your test class is typically used to capture and verify the results of the routes that you are testing. It's not a new route but a testing component that helps you set expectations and validate the behavior of your existing Camel routes when running unit tests.
	@EndpointInject("mock:result")
	protected MockEndpoint mockEndpoint;

	@Autowired
	ProducerTemplate producerTemplate;

	@Test
	void mockToEndpoint() throws Exception {
		String expectedBody = "My constant message";
		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(camelContext, "timerRouteTest", routeBuilder -> {
			routeBuilder.weaveByToUri("log:*").replace().to(mockEndpoint);
		});

		camelContext.start();
		mockEndpoint.assertIsSatisfied();
	}

	@Test
	void mockFromAndToEndpoint() throws Exception {
		String expectedBody = "My constant message";
		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		// changing route definition using advice
		AdviceWith.adviceWith(camelContext, "timerRouteTest", routeBuilder -> {
			routeBuilder.replaceFromWith("direct:startTimerTest");
			routeBuilder.weaveByToUri("log:*").replace().to(mockEndpoint);
		});

		camelContext.start();
		// trigger the direct route
		producerTemplate.sendBody("direct:startTimerTest", "ABCD");
		mockEndpoint.assertIsSatisfied();
	}
}
