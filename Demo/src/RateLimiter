package com.rfms.service;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

	private static Map<String, Integer> Rate = new HashMap<String, Integer>();
	private static Map<String, Long> TIME = new HashMap<String, Long>();

	public static void Limit(String token) throws java.lang.Exception {
		// If User Already send a request
		if (Rate.containsKey(token)) {
			if (Rate.get(token) == 1
					&& ((TIME.get(token) + RateLimiterProperties.LOW_IN_TIME) - System.currentTimeMillis()) >= 0) {
				// After Limit Cross
				throw new Exception("TOO_MANY_REQUESTS");
			} else if ((TIME.get(token) + RateLimiterProperties.LOW_IN_TIME) < System.currentTimeMillis()) {
				// when user does not send too many request at a time
				TIME.put(token, System.currentTimeMillis());
				Rate.put(token, RateLimiterProperties.LOW);
			}else {
			// User Click again and again in limited time
			Rate.put(token, Rate.get(token) - 1);
			}
		}
		// For new Login
		TIME.putIfAbsent(token, System.currentTimeMillis());
		Rate.putIfAbsent(token, RateLimiterProperties.LOW);

	}

}
