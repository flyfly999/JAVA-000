package io.github.kimmking.gateway.outbound.okhttp;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import java.util.Objects;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {
	private OkHttpClient httpClient;

	private String backendUrl;

	public static final String NIO_KEY = "nio";

	public OkhttpOutboundHandler(String backendUrl) {
		this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;


	//	System.out.println("OkhttpOutboundHandler" + backendUrl);



		this.httpClient = new OkHttpClient.Builder()
				.connectTimeout(1, TimeUnit.SECONDS)
				.readTimeout(1, TimeUnit.SECONDS)
				.build();

	}

	public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx) {
		final String url = this.backendUrl + fullRequest.uri();
	//	System.out.println("handle:::" + url);
		okHttpGet(fullRequest, ctx, url);
	}

	private void okHttpGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
		Request request = new Request
				.Builder()
				.url(url)
				.header(OkhttpOutboundHandler.NIO_KEY, inbound.headers().get(OkhttpOutboundHandler.NIO_KEY))
				.build();
		this.httpClient
				.newCall(request)
				.enqueue(new Callback() {
					@Override
					public void onFailure(@NotNull Call call, @NotNull IOException e) {

					}

					@Override
					public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
						try {
							handleResponse(inbound, ctx, response);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response endPointResponse) throws Exception {
		FullHttpResponse response = null;

		try {

			response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(endPointResponse.body().bytes()));

			System.out.println("hhh" + response.toString());
			response.headers()
					.set("Content-Type", "application/json")
					.setInt("Content-Length", Integer.parseInt(Objects.requireNonNull(endPointResponse.header("Content-Length"))));

		} catch (IOException e) {
			e.printStackTrace();
			response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
			exceptionCaught(ctx, e);
		} finally {
			if (fullRequest != null) {
				if (!HttpUtil.isKeepAlive(fullRequest)) {
					ctx.write(response).addListener(ChannelFutureListener.CLOSE);
				} else {
					ctx.write(response);
				}
			}

			ctx.flush();
		}
	}



	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}



}
