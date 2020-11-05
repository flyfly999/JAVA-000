package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.outbound.httpclient4.NamedThreadFactory;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
//import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {
	private OkHttpClient httpclient;
	private ExecutorService proxyService;
	private String backendUrl;

	public OkhttpOutboundHandler(String backendUrl) {
		this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;

		this.httpclient = new OkHttpClient.Builder()
				.connectTimeout(1L, TimeUnit.SECONDS)
				.readTimeout(1L, TimeUnit.SECONDS)
				.build();
		int cores = Runtime.getRuntime().availableProcessors() * 2;

		long keepAliveTime = 1000;
		int queueSize = 2048;

		RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
		this.proxyService = new ThreadPoolExecutor(cores, cores,
				keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
				new NamedThreadFactory("proxyService"), handler);
	}

	public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
		final String url = this.backendUrl + fullRequest.uri();
		System.out.println(url);

		proxyService.submit(()->okHttpGet(fullRequest, ctx, url));
	}

	private void okHttpGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
		Request.Builder builder = new Request.Builder().url(url);
		for (Map.Entry<String, String> head: inbound.headers()){
			builder.addHeader(head.getKey(),head.getValue());
		}

		Request request = builder.get().addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE).build();
		try{
			Response response = this.httpclient.newCall(request).execute();
			handleResponse(inbound, ctx, response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response endpointResponse) throws Exception {
		FullHttpResponse response = null;
		try {

			byte[] body = endpointResponse.body().bytes();
	//		System.out.println(new String(body));
	//		System.out.println(body.length);

			response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
			response.headers().set("Content-Type", "application/json");
			Headers responseHeaders = endpointResponse.headers();
			response.headers().setInt("Content-Length", Integer.parseInt(responseHeaders.get("Content-Length")));
		//	response.headers().add("nio", fullRequest.headers().get("nio"));


			for (int i = 0; i < responseHeaders.size(); i++){
				response.headers().add(responseHeaders.name(i),responseHeaders.value(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
			response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
			exceptionCaught(ctx, e);
		} finally {
			if (fullRequest != null) {
				if (!HttpUtil.isKeepAlive(fullRequest)) {
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
				} else {
					//response.headers().set(CONNECTION, KEEP_ALIVE);
					ctx.write(response);
				}
			}
			ctx.flush();
			//ctx.close();
		}

	}


	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}



}
