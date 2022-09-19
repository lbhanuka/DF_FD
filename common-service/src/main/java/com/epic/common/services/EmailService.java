package com.epic.common.services;

import com.epic.common.models.EmailRequestBean;
import com.epic.common.util.NumberWordConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.HashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    public String emailFrom;

    @Value("${config.email.logo.path}")
    public String emailLogoPath;

    public ResponseEntity<?> sendEmail(EmailRequestBean request) throws MessagingException, IOException, URISyntaxException {
        String emailType = request.getEmailType();

        if(emailType.equals("FD_CREATION_SUCCESS_CUSTOMER")){
            String subject = "e-Receipt for genie Digital Fixed Deposit by Dialog Finance";
            String body = "Dear Customer ,<br><br>" +
                    "Thank you for opening your Fixed Deposit with Dialog Finance.<br><br>" +
                    "Please find your Fixed Deposit e-receipt attached below.<br><br>" +
                    "To view your invoice, simply enter your password in the following order:<br><br>" +
                    "Date of Birth : DDMMYYYY<br><br> " +
                    "<b>If you have any concerns regarding your e-Receipt, please contact us via financialservice@dialog.lk or on our customer service hotline 011-4317317</b><br>";

            this.sendMessageWithAttachment(request.getEmailTo(),subject,body, request.getParameters());
        } else if (emailType.equals("FD_CREATION_FAILURE_IT")){
            String subject = "FAILED : Dialog Finance Digital Fixed Deposit Creation " + request.getParameters().get("customerNic").toUpperCase();
            String body = this.prepareEmailBody(emailType, request.getParameters());
            this.sendSimpleMessage(request.getEmailTo(),subject,body);
        }
        ResponseEntity<?> response = new ResponseEntity<>(HttpStatus.OK);
        return response;
    }

    private String prepareEmailBody(String emailType, HashMap<String, String> parameters) {
        String body = "";
        if(emailType.equals("FD_CREATION_FAILURE_IT")){
            String amount = parameters.get("depositAmount");
            double convertedNumber = Double.parseDouble(amount);
            NumberFormat formatter = NumberFormat.getInstance();
            body = "Genie FD creation failed with following details\n\n" +
                    "CIF : " + parameters.get("CIF") + "\n" +
                    "NIC : " + parameters.get("customerNic") + "\n" +
                    "Funding Savings Account : " + parameters.get("savingsAccount") + "\n" +
                    "Applicable Product Type : " + parameters.get("productType") + "\n" +
                    "Scheme Code : " + parameters.get("schemeCode") + "\n" +
                    "Mobile Number : " + parameters.get("mobileNumber") + "\n" +
                    "Customer Name : " + parameters.get("customerName") + "\n" +
                    "Deposit Amount : " + formatter.format(convertedNumber) + " LKR\n" +
                    "Interest Rate : " + parameters.get("interestRate") + "%\n" +
                    "Period : " + parameters.get("depositPeriod") + " Months\n" +
                    "Interest Payable Mode : " + parameters.get("interestPayableMode") + "\n" +
                    "Interest Earned at Maturity/ Approx. Monthly Interest : " + parameters.get("monthlyOrMaturityInterest") + "\n";
        }
        return body;
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

        helper.setFrom(emailFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.generateEmailAttachment(outputStream, parameters);
        final InputStreamSource attachment = new ByteArrayResource(outputStream.toByteArray());

        helper.addAttachment("e-receipt.pdf", attachment);

        emailSender.send(message);
    }

    //private String prepareEmailBody (HashMap<String,String> body, );

    private void generateEmailAttachment(ByteArrayOutputStream outputStream, HashMap<String,String> parameters) throws IOException, URISyntaxException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        AccessPermission accessPermission = new AccessPermission();
        accessPermission.setCanPrint(true);
        accessPermission.setCanModify(false);
        StandardProtectionPolicy standardProtectionPolicy
                = new StandardProtectionPolicy("q(n@#$icrmz@nsrb654ku%zwy", parameters.get("dateOfBirth"), accessPermission);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        String nameLine1 = "",nameLine2 = "",nameLine3 = "";
        String addressLine1 = "",addressLine2 = "",addressLine3 = "";
        String amountLine1 = "",amountLine2 = "",amountLine3 = "";
        float extraNameSpace=0,extraAddressSpace=0,extraAmountSpace=0;

        String fullName = parameters.get("customerName");
        if(fullName != null && fullName.length() > 42){
            int l1 = fullName.indexOf(" ", 42);
            nameLine1 = fullName.substring(0,l1);
            nameLine2 = fullName.substring(l1);
            extraNameSpace = 20;
            if(nameLine2 != null && nameLine2.length() > 42){
                int l2 = nameLine2.indexOf(" ", 42);
                String temp = nameLine2;
                nameLine2 = temp.substring(0,l2);
                nameLine3 = temp.substring(l2);
                extraNameSpace = 40;
            }
        } else {
            nameLine1 = fullName;
        }

        String fullAddr = parameters.get("customerAddress");
        if(fullAddr != null && fullAddr.length() > 42){
            int l1 = fullAddr.indexOf(" ", 42);
            addressLine1 = fullAddr.substring(0,l1);
            addressLine2 = fullAddr.substring(l1);
            extraAddressSpace = 20;
            if(addressLine2 != null && addressLine2.length() > 42){
                int l2 = addressLine2.indexOf(" ", 42);
                String temp = addressLine2;
                addressLine2 = temp.substring(0,l2);
                addressLine3 = temp.substring(l2);
                extraAddressSpace = 40;
            }
        } else {
            addressLine1 = fullAddr;
        }

        String amount = parameters.get("depositAmount");
        double convertedNumber = Double.parseDouble(amount);
        String fullAmountInLetters = NumberWordConverter.getMoneyIntoWords(convertedNumber);

        if(fullAmountInLetters != null && fullAmountInLetters.length() > 48){
            int l1 = fullAmountInLetters.indexOf(" ", 48);
            amountLine1 = fullAmountInLetters.substring(0,l1);
            amountLine2 = fullAmountInLetters.substring(l1);
            extraAmountSpace = 20;
            if(amountLine2 != null && amountLine2.length() > 48){
                int l2 = amountLine2.indexOf(" ", 48);
                String temp = amountLine2;
                amountLine2 = temp.substring(0,l2);
                amountLine3 = temp.substring(l2);
                extraAmountSpace = 40;
            }
        } else {
            amountLine1 = fullAmountInLetters;
        }


        PDImageXObject image
                = PDImageXObject.createFromFile(emailLogoPath, document);
        contentStream.drawImage(image, 400, 610, 160, 160);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(430, 620);
        contentStream.showText("No. 475, Union Place,");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(450, 605);
        contentStream.showText("Colombo 02");
        contentStream.endText();
        contentStream.beginText();
        contentStream.setNonStrokingColor(255,000,000);
        contentStream.newLineAtOffset(450, 590);
        contentStream.showText("P.B. 765 PQ");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
        contentStream.setNonStrokingColor(0,0,0);
        contentStream.newLineAtOffset(60, 570);
        contentStream.showText("DIALOG FINANCE PLC");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 550);
        contentStream.showText("FIXED DEPOSIT E-CERTIFICATE");
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        /*contentStream.beginText();

        //Setting the position for the line
        contentStream.newLineAtOffset(420, 590);
        contentStream.showText("Date: ");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(480, 590);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        contentStream.showText(LocalDate.now().format(formatter));
        contentStream.endText();*/
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 500);
        contentStream.showText("Name");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 500);
        contentStream.showText(": " + nameLine1);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 480);
        contentStream.showText(" " + nameLine2);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 460);
        contentStream.showText(" " + nameLine3);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 480-extraNameSpace);
        contentStream.showText("NIC Number");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 480-extraNameSpace);
        contentStream.showText(": " + parameters.get("customerNic").toUpperCase());
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 460-extraNameSpace);
        contentStream.showText("Address");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 460-extraNameSpace);
        contentStream.showText(": " + addressLine1);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 440-extraNameSpace);
        contentStream.showText(" " + addressLine2);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 420-extraNameSpace);
        contentStream.showText(" " + addressLine3);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 440-extraNameSpace-extraAddressSpace);
        contentStream.showText("Deposit Account Number");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 440-extraNameSpace-extraAddressSpace);
        contentStream.showText(": " + parameters.get("fdAccountNumber"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 420-extraNameSpace-extraAddressSpace);
        contentStream.showText("Deposit Amount (in figures)");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(220, 420-extraNameSpace-extraAddressSpace);
        NumberFormat numberFormatter = NumberFormat.getInstance();
        contentStream.showText(": Rs." + numberFormatter.format(convertedNumber));
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(60, 400-extraNameSpace-extraAddressSpace);
        contentStream.showText("Deposit Amount (in words):");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(220, 400-extraNameSpace-extraAddressSpace);
        contentStream.showText(": " + amountLine1);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 380-extraNameSpace-extraAddressSpace);
        contentStream.showText(" " + amountLine2);
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 360-extraNameSpace-extraAddressSpace);
        contentStream.showText(" " + amountLine3);
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(60, 380-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Deposit Period");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 380-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText(": " + parameters.get("depositPeriod") + " Months");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 360-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Interest Rate");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 360-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText(": " + parameters.get("interestRate") + " %");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 340-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Interest Payable");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 340-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText(": " + parameters.get("monthlyMaturity"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 320-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Account Open Date");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 320-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText(": " + parameters.get("openDate"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 300-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Account Maturity Date");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(220, 300-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText(": " + parameters.get("maturityDate"));
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 280-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Maturity Value");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(220, 280-extraNameSpace-extraAddressSpace-extraAmountSpace);
        amount = parameters.get("maturityAmount");
        convertedNumber = Double.parseDouble(amount);
        numberFormatter = NumberFormat.getInstance();
        contentStream.showText(": Rs." + numberFormatter.format(convertedNumber));
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 10);
        contentStream.newLineAtOffset(60, 230-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("Important:-");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
        contentStream.newLineAtOffset(60, 210-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("This is a system-generated receipt and therefore does not bear a serial number or require a signature.");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 190-extraNameSpace-extraAddressSpace-extraAmountSpace);
        contentStream.showText("This is issued for depositor reference only and may not reflect any subsequent lien placed against the deposit (if any)." );
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(60, 175-extraNameSpace-extraAddressSpace-extraAmountSpace);

        contentStream.showText("Therefore, Dialog Finance PLC does not recommend the reliance of this receipt for external use.");
        contentStream.endText();
        contentStream.close();

        document.protect(standardProtectionPolicy);
        document.save(outputStream);
        document.close();
    }
}
