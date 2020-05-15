package io.badri.app.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.badri.app.entity.Email;

@RestController
public class EmailController {

	@Autowired
	private Email email;

	@RequestMapping(value = "/sendemail")
	public String sendEmail() {
		email.sendEmail();
		return "Email sent successfully";
	}

}
