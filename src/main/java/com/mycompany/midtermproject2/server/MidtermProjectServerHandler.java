/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.midtermproject2.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 *
 * @author SC-RobLanger17
 */
public class MidtermProjectServerHandler extends ChannelInboundHandlerAdapter {
    static AtomicInteger count = new AtomicInteger(0);
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        count.incrementAndGet();
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        count.decrementAndGet();
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(((ByteBuf) msg).nioBuffer());
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        byte messageType = unpacker.unpackByte();
        switch (messageType) {
            case (byte)0xAA: // double a number
                int number = unpacker.unpackInt();
                packer.packByte((byte)0xAA)
                      .packInt(number * 2)
                      .flush();
                ctx.write
                break;
            case (byte)0xBB: // capitalize a string
                String toCapitalize = unpacker.unpackString();
                break;
            default:
                throw new RuntimeException("oof");
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Logger.getLogger(MidtermProjectServerHandler.class.getName()).log(Level.SEVERE, null, cause);
        ctx.close();
    }
}
