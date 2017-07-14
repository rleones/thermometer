package com.fincad.thermometer.service;

import com.fincad.thermometer.model.Threshold;

/**
 * Created by roberto on 21/06/17.
 */
public interface MailService {

   void sendNotification(Threshold threshold) throws Exception;

}