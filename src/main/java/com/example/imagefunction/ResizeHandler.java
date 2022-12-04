package com.example.imagefunction;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import org.springframework.cloud.function.adapter.azure.FunctionInvoker;

public class ResizeHandler extends FunctionInvoker<ResizeOptions, String> {

	@FunctionName("resize")
	public HttpResponseMessage execute(
			@HttpTrigger(name = "request", methods = HttpMethod.POST, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<ResizeOptions> request,
			ExecutionContext context) {
		ResizeOptions options = request.getBody();
		context.getLogger().info("Resizing " + options.getUrl() + " with ratio=" + options.getRatio() + ", width=" + options.getWidth() + ", height=" + options.getHeight());
		return request
				.createResponseBuilder(HttpStatus.OK)
				.body(handleRequest(options, context))
				.header("Content-Type", "text/plain")
				.build();
	}

}
