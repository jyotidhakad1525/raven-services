package com.adobe.core.raven.service.interfaces;


import com.adobe.core.raven.dto.cgen.Cgen;

import java.util.ArrayList;

public interface CgenService {

   String fetchTagfullurl(String tagId, ArrayList<Cgen> cgen);

   String fetchSubjectline(String activityId, ArrayList<Cgen> cgen);

   String fetchPreHeader(String activityId, ArrayList<Cgen> cgen);

   int fetchCTALinkCount(String activityId, ArrayList<Cgen> cgen);

   }
