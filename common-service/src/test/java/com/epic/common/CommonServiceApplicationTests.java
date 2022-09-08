package com.epic.common;

import com.epic.common.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@SpringBootTest
class CommonServiceApplicationTests {

	@Autowired
	EmailService emailService;

	@Test
	void sendEmailTest() {

		String to = "bhanuka_t@epiclanka.net";
		String subject = "DF EMAIL TEST";
		String text = "THIS IS A TEST";

		emailService.sendSimpleMessage(to,subject,text);
	}

	@Test
	void sendEmailWithAttachmentTest() throws MessagingException, IOException, URISyntaxException {

		String to = "bhanuka_t@epiclanka.net";
		String subject = "DF EMAIL TEST";
		String text = "THIS IS A TEST";

		emailService.sendMessageWithAttachment(to,subject,text, new HashMap<>());
	}

	@Test
	void contextLoads() {
	}

}
