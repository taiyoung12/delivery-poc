package com.barogo.java.delivery.poc.common;

import org.aspectj.apache.bcel.classfile.Code;

import lombok.Getter;

@Getter
public class Response<T> {
	private final String code;
	private final String message;
	private final T data;

	private Response(Code code, T data) {
		this.code = code.getCode();
		this.message = code.getMessage();
		this.data = data;
	}

	public static <T> Response<T> success(T data) {
		return new Response<>(CommonCode.SUCCESS, data);
	}

	public static Response<Void> success(Code responseCode) {
		return new Response<>(responseCode, null);
	}

	public static <T> Response<T> error(Code code, T data) {
		return new Response<>(code, data);
	}

	public static Response<Void> error(Code code) {
		return new Response<>(code, null);
	}
}