package com.mycompany.midtermproject2.server;

import com.mycompany.midtermproject2.proto.ClientMessageOuterClass.ClientMessage;
import com.mycompany.midtermproject2.proto.ServerMessageOuterClass.ServerMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// This class does not call its superclass's methods because it is designed to be at the end of the pipeline.
public class MidtermProjectServerHandler extends SimpleChannelInboundHandler<ClientMessage> {
    // making these static is a bad idea, but since we're only dealing with one server it's fine for now
    private static final Map<ChannelId, String> nicknamesMap = new ConcurrentHashMap<>(); // Concurrent map for thread safety
    private static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // Defaults are fine here

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // once a client is connected, add it to the clients group and assign it a default nickname based on its address
        clients.add(ctx.channel());
        nicknamesMap.put(ctx.channel().id(), ctx.channel().remoteAddress().toString());
        clients.writeAndFlush(ServerMessage.newBuilder()
                .setType(ServerMessage.MsgType.INFO)
                .setBody(String.format("<%s> has joined the server", nicknamesMap.get(ctx.channel().id())))
                .build());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientMessage msg) {
        ServerMessage.Builder builder = ServerMessage.newBuilder(); // start composing a reply

        boolean broadcast = false; // messages are only sent to the sender by default
        switch (msg.getType()) {
            case CHAT:
                if (!msg.hasBody()) {
                    // CHAT messages should have a body
                    builder.setType(ServerMessage.MsgType.ERR)
                           .setCode(ServerMessage.ErrType.BAD_REQUEST);
                } else {
                    // Send the message to everyone in the format "<username> message"
                    broadcast = true;
                    builder.setType(ServerMessage.MsgType.CHAT)
                            .setBody(String.format("<%s> %s", nicknamesMap.get(ctx.channel().id()), msg.getBody()));
                }
                break;
            case NICK:
                if (!msg.hasBody()) {
                    // NICK messages should have a body
                    builder.setType(ServerMessage.MsgType.ERR)
                           .setCode(ServerMessage.ErrType.BAD_REQUEST);
                } else {
                    if (nicknamesMap.containsValue(msg.getBody())) {
                        // Client tried to request a nickname that is already in use
                        builder.setType(ServerMessage.MsgType.ERR)
                               .setCode(ServerMessage.ErrType.NICK_IN_USE);
                    } else {
                        // Send a message to everyone in the format "<old_username> is now <new_username>"
                        broadcast = true;
                        builder.setType(ServerMessage.MsgType.INFO)
                               .setBody(String.format("<%s> is now <%s>", nicknamesMap.put(ctx.channel().id(), msg.getBody()), msg.getBody()));
                    }
                }
                break;
        }

        if (broadcast) {
            // Send the message to all clients
            clients.writeAndFlush(builder.build());
        } else {
            // Send the message only to the requesting client
            ctx.writeAndFlush(builder.build());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // Client disconnected
        clients.remove(ctx.channel());
        nicknamesMap.remove(ctx.channel().id());
        clients.writeAndFlush(ServerMessage.newBuilder()
                .setType(ServerMessage.MsgType.INFO)
                .setBody(String.format("<%s> has left the server", nicknamesMap.get(ctx.channel().id())))
                .build());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Uh oh, something bad happened
        cause.printStackTrace();
        ctx.close();
    }
}
