/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.midtermproject2.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 *
 * @author SC-RobLanger17
 */
public class NulTerminatorDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte readByte;
        ByteBuf message = Unpooled.buffer();
        try {
            do {
                readByte = in.readByte();
                message.writeByte(readByte);
            } while (readByte != 0x00);
        }
        catch (IndexOutOfBoundsException ex) {
            return;
        }
        
        out.add(message.readBytes)
    }
    
}
