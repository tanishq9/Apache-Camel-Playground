package com.example.camelmicroservicesb.routes.L2;

import com.example.camelmicroservicesb.domain.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class ActiveMqReceiverWithProcessingRouter extends RouteBuilder {

	@Autowired
	private CurrencyExchangeProcessor currencyExchangeProcessor;

	@Autowired
	private CurrencyExchangeTransformer currencyExchangeTransformer;

	@Override
	public void configure() throws Exception {
		// Map the JSON input to POJO
		from("activemq:my-activemq-queue")
				.unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class) // unmarshal from json structure to pojo, unmarshal is same as deserialize
				.bean(currencyExchangeProcessor)
				.bean(currencyExchangeTransformer)
				.to("log:received-message-log");
	}
}

@Component
class CurrencyExchangeProcessor {
	// CurrencyExchange is the input to this processing stage
	public void processMessage(CurrencyExchange currencyExchange) {
		// However we are processing, but the below message i.e. pojo is getting transformed
		currencyExchange.setConversionMultiple(9L);
		System.out.println("Doing some processing on currency exchange: " + currencyExchange.getConversionMultiple());
	}
}

@Component
class CurrencyExchangeTransformer {
	// CurrencyExchange is the input to this processing stage
	// The return type can be other than CurrencyExchange as well
	public CurrencyExchange transform(CurrencyExchange currencyExchange) {
		currencyExchange.setConversionMultiple(18L);
		return currencyExchange;
	}
}
