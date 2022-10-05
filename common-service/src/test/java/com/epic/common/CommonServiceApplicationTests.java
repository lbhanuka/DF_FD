package com.epic.common;

import com.epic.common.services.EmailService;
import com.epic.common.util.NumberWordConverter;
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
	void sendEmailTest() throws MessagingException {

		String[] to = {"bhanuka_t@epiclanka.net"};
		String subject = "DF EMAIL TEST";
		String text = "THIS IS A TEST";

		emailService.sendSimpleMessage(to,subject,text);
	}

	@Test
	void sendEmailWithAttachmentTest() throws MessagingException, IOException, URISyntaxException {

		String[] to = {"bhanuka_t@epiclanka.net"};
		String subject = "DF EMAIL TEST";
		String text = "THIS IS A TEST";

		emailService.sendMessageWithAttachment(to,subject,text, new HashMap<>());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void numberToWord(){
		//String a = "132.23";
		double b = 123.23;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
		b = 1;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
		b = 123.00;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
		b = 1132132232.23;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
		b = 123;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
		b = 1233243244;
		System.out.println("OWN word------------------>" + NumberWordConverter.getMoneyIntoWords(b));
	}

}
