package ru.project.jbd.security.payload.response;

import java.util.List;

public record AutheticatedResponse (
    Long id,
	String username,
	List<String> roles,
	String authToken,
	String refreshToken
) {}
