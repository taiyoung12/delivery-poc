package com.barogo.java.delivery.poc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barogo.java.delivery.poc.common.response.Response;

@RestController
@RequestMapping("/api/v1/ping")
public class PingController {

	@GetMapping
	public Response<String> ping(){
		return Response.success("pong");
	}
}