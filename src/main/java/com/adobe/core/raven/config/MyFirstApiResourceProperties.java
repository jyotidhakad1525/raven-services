/*************************************************************************
 * ADOBE CONFIDENTIAL ___________________
 * <p/>
 * Copyright 2022 Adobe Systems Incorporated All Rights Reserved.
 * <p/>
 * NOTICE: All information contained herein is, and remains the property of Adobe Systems
 * Incorporated and its suppliers, if any. The intellectual and technical concepts contained herein
 * are proprietary to Adobe Systems Incorporated and its suppliers and are protected by all
 * applicable intellectual property laws, including trade secret and copyright laws. Dissemination
 * of this information or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Adobe Systems Incorporated.
 **************************************************************************/

package com.adobe.core.raven.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.adobe.core.raven")
public class MyFirstApiResourceProperties {
  private String message;
  public String getMessage() {return message;}
  public void setMessage(String msg) {message = msg;}
}
