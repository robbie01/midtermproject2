package com.mycompany.midtermproject2.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MidtermProjectServer {
    public static void main(String[] args) {
        // the "nio" in some class names here refers to the implementation of their respective abstract classes
        // in this case, it uses java's builtin NIO, a cross platform library for concurrently handling large numbers of connections

        EventLoopGroup serverGroup = new NioEventLoopGroup(1); // the listener itself does not need to scale
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // ServerBootstrap abstracts away server initialization and handling so we don't have to set it up ourselves
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(serverGroup, workerGroup)
                     .channel(NioServerSocketChannel.class) // Use an NIO server implementation
                     .handler(new LoggingHandler(LogLevel.INFO)) // Automatically log all events so we know what's going on
                     .childHandler(new MidtermProjectServerChannelInitializer()); // Handle each child with our custom pipeline setup

            // Bind the server to port 3000 and wait for it to close
            bootstrap.bind(3000).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // server was interrupted, e.g. with Ctrl+C (possibly intentionally)
            e.printStackTrace();
        } finally {
            // In case there's a fatal error in the server, try to shut everything down as best as possible
            serverGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}