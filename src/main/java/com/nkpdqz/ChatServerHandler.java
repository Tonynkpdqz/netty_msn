package com.nkpdqz;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel inComing = ctx.channel();
        System.out.println(inComing.remoteAddress()+" in");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel inComing = ctx.channel();
        System.out.println(inComing.remoteAddress()+" down");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel inComing = ctx.channel();
        System.out.println(inComing.remoteAddress() + " exception");
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel inComing = ctx.channel();
        for (Channel ch:channelGroup) {
            if (ch != inComing){
                ch.writeAndFlush("welcome: "+inComing.remoteAddress() + "in room\n");
            }
            channelGroup.add(inComing);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel out = ctx.channel();
        for (Channel ch:channelGroup
             ) {
            if (ch!=out){
                ch.writeAndFlush("bye! "+out.remoteAddress()+ "\n");
            }
            channelGroup.remove(out);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel inComing = channelHandlerContext.channel();
        for (Channel channel:channelGroup
             ) {
            if (channel!=inComing){
                channel.writeAndFlush("user: "+inComing.remoteAddress()+" says: " + s + "\n");
            }else {
                channel.writeAndFlush("me:" + s + "\n");
            }
        }
    }
}
