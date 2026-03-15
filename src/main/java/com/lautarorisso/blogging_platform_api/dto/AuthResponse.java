package com.lautarorisso.blogging_platform_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "username", "message", "jwt", "status" })
public record AuthResponse(String jwt, String username, String message, boolean status) {
}
