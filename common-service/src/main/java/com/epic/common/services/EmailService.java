package com.epic.common.services;

import com.epic.common.models.EmailRequestBean;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    public String emailFrom;

    public ResponseEntity<?> sendEmail(EmailRequestBean request) throws MessagingException, IOException, URISyntaxException {
        String emailType = request.getEmailType();

        if(emailType.equals("FD_CREATION_SUCCESS_CUSTOMER")){
            String subject = "Your Dialog Finance Digital Fixed Deposit has been successfully created";
            this.sendMessageWithAttachment(request.getEmailTo(),subject,"", request.getParameters());
        }
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }

    public void sendSimpleMessage( String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMessageWithAttachment(
            String to, String subject, String text, HashMap<String,String> parameters) throws MessagingException, IOException, URISyntaxException {

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("lbhanuka@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.generateEmailAttachment(outputStream, parameters);
        final InputStreamSource attachment = new ByteArrayResource(outputStream.toByteArray());

        helper.addAttachment("invoice.pdf", attachment);

        emailSender.send(message);
    }

    private void generateEmailAttachment(ByteArrayOutputStream outputStream, HashMap<String,String> parameters) throws IOException, URISyntaxException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        File file = new ClassPathResource("data/logo.png").getFile();
        Path path = file.toPath();
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        PDImageXObject image
                = PDImageXObject.createFromFile(path.toAbsolutePath().toString(), document);
        contentStream.drawImage(image, 40, 550);

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();

        //Setting the position for the line
        contentStream.newLineAtOffset(420, 590);
        contentStream.showText("Date: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(480, 590);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        contentStream.showText(LocalDate.now().format(formatter));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 500);
        contentStream.showText("Name: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 500);
        contentStream.showText(parameters.get("customerName"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 475);
        contentStream.showText("NIC Number: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 475);
        contentStream.showText(parameters.get("customerNic"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 450);
        contentStream.showText("Deposit Account Number: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 450);
        contentStream.showText(parameters.get("fdAccountNumber"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 425);
        contentStream.showText("Deposit Amount (LKR) : ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 425);
        contentStream.showText(parameters.get("depositAmount"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 400);
        contentStream.showText("Deposit Period in Months: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 400);
        contentStream.showText(parameters.get("depositPeriod"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 375);
        contentStream.showText("Interest Rate: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 375);
        contentStream.showText(parameters.get("interestRate"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 350);
        contentStream.showText("Interest Payable (Monthly/ Maturity):");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 350);
        contentStream.showText(parameters.get("monthlyMaturity"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 325);
        contentStream.showText("Account Open Date: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 325);
        contentStream.showText(parameters.get("openDate"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 300);
        contentStream.showText("Account Maturity Date: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 300);
        contentStream.showText(parameters.get("maturityDate"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 275);
        contentStream.showText("Maturity Value (LKR) :");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(320, 275);
        contentStream.showText(parameters.get("maturityAmount"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contentStream.newLineAtOffset(60, 200);
        contentStream.showText("This is a system-generated receipt and therefore does not bear a serial number or require a signature.");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 175);
        contentStream.showText("This is issued for depositor reference only and may not reflect any subsequent lien placed against the deposit (if any)." );
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 160);

        contentStream.showText("Therefore, Dialog Finance PLC does not recommend the reliance of this receipt for external use.");
        contentStream.endText();
        contentStream.close();

        document.save(outputStream);
        document.close();
    }
}
