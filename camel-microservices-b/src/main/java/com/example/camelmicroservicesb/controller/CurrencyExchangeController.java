package com.example.camelmicroservicesb.controller;

import com.example.camelmicroservicesb.domain.CurrencyExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	@GetMapping("/currency-exchange/{from}/{to}")
	public CurrencyExchange findConversionValue(
			@PathVariable String from,
			@PathVariable String to
	) {
		return new CurrencyExchange(1L, from, to, 10L);
	}
}
