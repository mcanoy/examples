package com.rhc;

import io.vertx.core.AbstractVerticle;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class VertxApp extends AbstractVerticle {

	@Override
	public void start() {
		LocalDateTime time = LocalDateTime.now( ZoneId.of( ZoneId.SHORT_IDS.get("PST")));

		vertx.createHttpServer()
                        
				.requestHandler(req -> req.response()
				        .putHeader("content-type", "text/html")
				        .end(String.format("Hello World from Vert.x Pod <strong>%s!</strong>", System.getenv("HOSTNAME"))))
				        .listen(8082);
	}

}
