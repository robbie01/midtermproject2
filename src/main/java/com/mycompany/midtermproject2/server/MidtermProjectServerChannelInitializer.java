package com.mycompany.midtermproject2.server;

import com.mycompany.midtermproject2.proto.ClientMessageOuterClass.ClientMessage;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class MidtermProjectServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) {
	ChannelPipeline p = ch.pipeline();

	p.addLast(new ProtobufVarint32FrameDecoder()); // INCOMING: decode Varint32 at beginning of message to get message length
	p.addLast(new ProtobufDecoder(ClientMessage.getDefaultInstance())); // INCOMING: interpret message as ClientMessage

	p.addLast(new ProtobufVarint32LengthFieldPrepender()); // OUTGOING: add Varint32 to beginning of message
	p.addLast(new ProtobufEncoder()); // OUTGOING: encode protobuf object as bytes

	p.addLast(new MidtermProjectServerHandler()); // IN/OUT: main server logic
    }
}
