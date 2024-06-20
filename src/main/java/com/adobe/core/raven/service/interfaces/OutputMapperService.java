package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.cgen.Cgen;
import com.adobe.core.raven.dto.qa.Link;

import java.util.ArrayList;

public interface OutputMapperService {

   void mapValidationOutput(String label, String type, String name, String state, ArrayList<Link> links, String stepId, String messageId);
   void mapValidationOutput(String label, String type, String name, String state, ArrayList<Link> links, String stepId, String messageId,String reshare);

   }
