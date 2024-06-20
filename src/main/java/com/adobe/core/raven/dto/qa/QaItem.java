package com.adobe.core.raven.dto.qa;



import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

public @Data class QaItem {


	private String id; // ref from message id
	private String status;
	private String bu;
	private String segment;
	private ArrayList<CheckList> checkList;
}
