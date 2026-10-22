package com.makesquare.server.auth.dto;

/**
 * The body the API returns when a request is refused.
 *
 * @param code a stable, machine-readable identifier for the failure
 * @param message a human-readable explanation
 */
public record ApiError(String code, String message) {
}
