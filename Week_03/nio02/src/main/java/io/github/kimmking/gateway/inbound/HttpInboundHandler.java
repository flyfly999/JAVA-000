package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.HttpRequestHeaderFilter;

import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private OkhttpOutboundHandler handler;

    private HttpRequestFilter filter;
    
    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
      //  handler = new HttpOutboundHandler(this.proxyServer);
     //   System.out.println("HttpInboundHandler::" + proxyServer);
        handler = new OkhttpOutboundHandler(this.proxyServer);
        filter = new HttpRequestHeaderFilter();
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
        //    System.out.println("channelRead流量接口请求开始，时间为{}");
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
         //   String uri = fullRequest.uri();

//            if (uri.contains("/test")) {
//                handlerTest(fullRequest, ctx);
//            }
         //   requestFilter.filter(fullRequest, ctx);
            filter.filter(fullRequest,ctx);
            handler.handle(fullRequest, ctx);
    
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }



}
