package com.mycompany.midtermproject2.server;

import com.mycompany.midtermproject2.proto.ClientMessageOuterClass.ClientMessage;
import com.mycompany.midtermproject2.proto.ServerMessageOuterClass.ServerMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.util.logging.Logger;
import java.util.logging.Level;

public class MidtermProjectServerHandler extends SimpleChannelInboundHandler<ClientMessage> {
    private static final Map<ChannelId, String> nicknamesMap = new ConcurrentHashMap<>();
    private static final ChannelGroup clients = new DefaultChannelGroup();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) {
        if (nicknamesMap.get(ctx.channel().id()) == null) {
            nicknamesMap.put(ctx.channel().id(), ((InetSocketAddress)ctx.channel().remoteAddress()).toString());
        }
        
        
        ServerMessage.Builder builder = ServerMessage.newBuilder();
        
        ctx.writeAndFlush(builder.build());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.getLogger(MidtermProjectServerHandler.class.getName()).log(Level.SEVERE, null, cause);
        ctx.close();
    }
}
