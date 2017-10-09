package com.mycompany.midtermproject2.server;

import com.mycompany.midtermproject2.proto.ClientMessage;
import com.mycompany.midtermproject2.proto.ServerMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Logger;
import java.util.logging.Level;

public class MidtermProjectServerHandler extends SimpleChannelInboundHandler<ClientMessage> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) {
        ServerMessage.Builder builder = ServerMessage.newBuilder();
        ctx.writeAndFlush(builder.build());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.getLogger(MidtermProjectServerHandler.class.getName()).log(Level.SEVERE, null, cause);
        ctx.close();
    }
}
