package com.interswitch.bookstore.responses;

public record ApiResponse<T>(int code, String message, T data) {
}