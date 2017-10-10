package com.mycompany.midtermproject2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class MidtermProjectClientMainForm {
    public JTextPane textPane1; // consistency? never heard of it
    private JPanel panel1;
    private JTextField textField1;
    private JButton sendButton;
    private JButton changeButton;

    // i'm using a singleton because it's 2:30 am and i want to watch the world burn
    private static MidtermProjectClientMainForm _instance = new MidtermProjectClientMainForm();
    private MidtermProjectClientMainForm() {
        StyleConstants.setForeground(textPane1.getStyledDocument().addStyle("chat", null), Color.black);
        StyleConstants.setForeground(textPane1.getStyledDocument().addStyle("info", null), Color.black);
        StyleConstants.setForeground(textPane1.getStyledDocument().addStyle("err", null), Color.black);
    }

    public static MidtermProjectClientMainForm getInstance() {
        return _instance;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MidtermProjectClient"); // create the window
        frame.setContentPane(MidtermProjectClientMainForm.getInstance().panel1); // set the window to our custom form
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // who cares
        frame.setMinimumSize(new Dimension(500, 500)); // the windows must be at leas 500x500
        frame.pack(); // something that makes it look better
        frame.setVisible(true); // show the darn window


        // the following code is nearly identical to the server code
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true) // make sure the connection doesn't time out easily
                    .handler(new MidtermProjectClientChannelInitializer());

            bootstrap.connect("127.0.0.1",3000);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
