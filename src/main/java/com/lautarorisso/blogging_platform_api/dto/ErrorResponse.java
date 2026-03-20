package com.lautarorisso.blogging_platform_api.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int Status, String message, String errorCode, LocalDateTime timestamp, String path) {
}
