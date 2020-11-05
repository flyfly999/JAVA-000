package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;

public class HttpRequestHeaderFilter  implements HttpRequestFilter{
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        HttpHeaders headers = fullRequest.headers();
        headers.add("nio","zhaoyong");
    }
}
