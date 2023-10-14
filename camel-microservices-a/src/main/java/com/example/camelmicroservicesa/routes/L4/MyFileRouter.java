package com.example.camelmicroservicesa.routes.L4;

import org.apache.camel.builder.RouteBuilder;

//@Component
public class MyFileRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("file:files/input")
				.log("${body}") // print contents of file
				.to("file:files/output");
	}
}
