package com.adobe.core.raven.service.interfaces;


import com.auxilii.msgparser.Message;

public interface MsgParserService {

   Message parseMsgFile(String path);

   }
