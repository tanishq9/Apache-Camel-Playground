package com.example.camelmicroservicesa.routes.EIP;

import com.example.camelmicroservicesa.domain.CurrencyExchange;
import java.util.ArrayList;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

//@Component
public class AggregationPattern extends RouteBuilder {
	@Override
	public void configure() throws Exception {
		// Messages => Aggregate messages and send to some endpoint
		// We can aggregate by particular value in message, and we can also specify a completion criteria

		from("file:files/json")
				.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class) // Unmarshalling each message as JSON
				.aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
				.completionSize(3)
				//.completionTimeout(10000)
				.to("log:aggregate-json");
	}
}


class ArrayListAggregationStrategy implements AggregationStrategy {
	//1,2,3

	//null, 1 => [1]
	//[1], 2 => [1,2]
	//[1,2], 3 => [1,2,3]

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Object newBody = newExchange.getIn().getBody();
		ArrayList<Object> list = null;
		if (oldExchange == null) {
			list = new ArrayList<Object>();
			list.add(newBody);
			newExchange.getIn().setBody(list);
			return newExchange;
		} else {
			list = oldExchange.getIn().getBody(ArrayList.class);
			list.add(newBody);
			return oldExchange;
		}
	}
}