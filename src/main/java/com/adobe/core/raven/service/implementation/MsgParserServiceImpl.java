package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.service.interfaces.MsgParserService;

import java.io.IOException;

import com.auxilii.msgparser.*;

public class MsgParserServiceImpl implements MsgParserService {

    public Message parseMsgFile(String path){
        MsgParser msgParser = new MsgParser();
        Message msg = null;
        try {
            msg = msgParser.parseMsg(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
