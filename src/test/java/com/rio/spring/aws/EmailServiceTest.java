package com.rio.spring.aws;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.rio.spring.aws.AwsSESEmailService;
import com.rio.spring.aws.MailException;
import com.rio.spring.aws.SimpleMailMessage;

public class EmailServiceTest extends AbstractServiceTest {
	private AwsSESEmailService mailSender;
	private AmazonSimpleEmailService emailService;

	private SendEmailResult mockEmailResult;

	@Before
	public void setup() {
		mailSender = Mockito.spy(AwsSESEmailService.class);
		emailService = Mockito.mock(AmazonSimpleEmailService.class);
		Mockito.doReturn(emailService).when(mailSender).getEmailService();
		mockEmailResult = new SendEmailResult().withMessageId("123456");

		ResponseMetadata metaData = Mockito.spy(new ResponseMetadata(new HashMap<String, String>()));
		Mockito.doReturn("mock-request-id").when(metaData).getRequestId();

		mockEmailResult.setSdkResponseMetadata(metaData);
	}

	@Test
	public void testSendSimpleEmail() throws MailException {
		SimpleMailMessage simpleMailMessage = createSimpleMailMessage();

		ArgumentCaptor<SendEmailRequest> request = ArgumentCaptor.forClass(SendEmailRequest.class);
		when(emailService.sendEmail(request.capture())).thenReturn(mockEmailResult);

		mailSender.send(simpleMailMessage);

		SendEmailRequest sendEmailRequest = request.getValue();
		assertEquals(simpleMailMessage.getFrom(), sendEmailRequest.getSource());
		assertEquals(simpleMailMessage.getTo()[0], sendEmailRequest.getDestination().getToAddresses().get(0));
		assertEquals(simpleMailMessage.getSubject(), sendEmailRequest.getMessage().getSubject().getData());
		assertEquals(simpleMailMessage.getText(), sendEmailRequest.getMessage().getBody().getHtml().getData());
		assertEquals(0, sendEmailRequest.getDestination().getCcAddresses().size());
		assertEquals(0, sendEmailRequest.getDestination().getBccAddresses().size());
	}

	@Test
	public void testSendSimpleMailWithCCandBCC() throws Exception {

		SimpleMailMessage simpleMailMessage = createSimpleMailMessage();
		simpleMailMessage.setBcc("bcc@domain.com");
		simpleMailMessage.setCc("cc@domain.com");

		ArgumentCaptor<SendEmailRequest> request = ArgumentCaptor.forClass(SendEmailRequest.class);
		Mockito.when(emailService.sendEmail(request.capture())).thenReturn(mockEmailResult);

		mailSender.send(simpleMailMessage);

		SendEmailRequest sendEmailRequest = request.getValue();
		assertEquals(simpleMailMessage.getFrom(), sendEmailRequest.getSource());
		assertEquals(simpleMailMessage.getTo()[0], sendEmailRequest.getDestination().getToAddresses().get(0));
		assertEquals(simpleMailMessage.getSubject(), sendEmailRequest.getMessage().getSubject().getData());
		assertEquals(simpleMailMessage.getText(), sendEmailRequest.getMessage().getBody().getHtml().getData());
		assertEquals(simpleMailMessage.getBcc()[0], sendEmailRequest.getDestination().getBccAddresses().get(0));
		assertEquals(simpleMailMessage.getCc()[0], sendEmailRequest.getDestination().getCcAddresses().get(0));
	}

	private SimpleMailMessage createSimpleMailMessage() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("email@email.com");
		simpleMailMessage.setTo("test@test.com");
		simpleMailMessage.setSubject("This is a test on SES Email Sending");
		simpleMailMessage.setText("Message Body");
		return simpleMailMessage;
	}
}
