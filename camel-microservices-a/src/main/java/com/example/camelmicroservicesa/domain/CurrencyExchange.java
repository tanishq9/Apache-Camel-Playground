package com.example.camelmicroservicesa.domain;

// {  "id": 1000,  "from": "USD",  "to": "INR",  "conversionMultiple": 70}
public class CurrencyExchange {
	private Long id;
	private String from;
	private String to;
	private Long conversionMultiple;

	public CurrencyExchange() {
	}

	public CurrencyExchange(Long id, String from, String to, Long conversionMultiple) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setConversionMultiple(Long conversionMultiple) {
		this.conversionMultiple = conversionMultiple;
	}

	public Long getId() {
		return id;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public Long getConversionMultiple() {
		return conversionMultiple;
	}

	@Override
	public String toString() {
		return "CurrencyExchange{" +
				"id=" + id +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", conversionMultiple=" + conversionMultiple +
				'}';
	}
}
