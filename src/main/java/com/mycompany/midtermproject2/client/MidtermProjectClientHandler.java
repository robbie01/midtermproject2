package com.mycompany.midtermproject2.client;

import com.mycompany.midtermproject2.proto.ServerMessageOuterClass.ServerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

public class MidtermProjectClientHandler extends SimpleChannelInboundHandler<ServerMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerMessage msg) throws BadLocationException {
        StyledDocument doc = MidtermProjectClientMainForm.getInstance().textPane1.getStyledDocument();
        switch (msg.getType()) {
            case ERR:
                doc.insertString(doc.getLength(), String.format("Error: %s%n", msg.getCode()), doc.getStyle("err"));
                break;
            case CHAT:
                doc.insertString(doc.getLength(), String.format("%s%n", msg.getCode()), doc.getStyle("chat"));
                break;
            case INFO:
                doc.insertString(doc.getLength(), String.format("%s%n", msg.getCode()), doc.getStyle("info"));
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
