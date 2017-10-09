package com.mycompany.midtermproject2.server;

import com.mycompany.midtermproject2.proto.ClientMessage;

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

	p.addLast(new ProtobufVarint32FrameDecoder());
	p.addLast(new ProtobufDecoder(ClientMessage.getDefaultInstance()));

	p.addLast(new ProtobufVarint32LengthFieldPrepender());
	p.addLast(new ProtobufEncoder());

	p.addLast(new MidtermProjectServerHandler());
    }
}
