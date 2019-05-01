package com.dufther.web.rest;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dufther.service.MailService;
import com.dufther.service.OtpService;
import com.dufther.service.TwilioSmsSender;
import com.dufther.util.SmsRequest;

/**
 */
@RestController
@RequestMapping("/api")
public class OtpController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	public OtpService otpService;
	@Autowired
	public MailService myEmailService;

	@Autowired
	private TwilioSmsSender twilioSmsSender;
	
	@GetMapping("/generateOtp")
	public String generateOtp() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		int otp = otpService.generateOTP(username);
		logger.info("OTP : " + otp);
		// Generate The Template to send OTP
//		EmailTemplate template = new EmailTemplate("SendOtp.html");
		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", username);
		replacements.put("otpnum", String.valueOf(otp));
//		String message = template.getTemplate(replacements);
//		myEmailService.sendOtpMessage("shrisowdhaman@gmail.com", "OTP -SpringBoot", message);
//		myEmailService.sendActivationEmail(user);
		return "otppage";
	}
	
	@PostMapping("/sendsms")
    @ResponseStatus(HttpStatus.OK)
    public void sendSms(@Valid @RequestBody SmsRequest smsRequest) {
		twilioSmsSender.sendSms(smsRequest);
    }
	

	@RequestMapping(value = "/validateOtp", method = RequestMethod.GET)
	public @ResponseBody String validateOtp(@RequestParam("otpnum") int otpnum) {
		final String SUCCESS = "Entered Otp is valid";
		final String FAIL = "Entered Otp is NOT valid. Please Retry!";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		logger.info(" Otp Number : " + otpnum);
		// Validate the Otp
		if (otpnum >= 0) {
			int serverOtp = otpService.getOtp(username);
			if (serverOtp > 0) {
				if (otpnum == serverOtp) {
					otpService.clearOTP(username);
					return ("Entered Otp is valid");
				} else {
					return SUCCESS;
				}
			} else {
				return FAIL;
			}
		} else {
			return FAIL;
		}
	}
}
