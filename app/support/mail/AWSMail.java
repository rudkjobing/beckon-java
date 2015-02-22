package support.mail;

import com.amazonaws.services.simpleemail.model.*;

import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public class AWSMail implements Mail{

    private Message message;

    private String from;
    private Destination destination;
    private Content subject;
    private Body body;

    public AWSMail(){
        this.destination = new Destination();
        this.subject = new Content();
        this.body = new Body();
        this.message = new Message().withSubject(this.subject).withBody(this.body);
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public List<String> getTo() {
        return this.destination.getToAddresses();
    }

    @Override
    public void setTo(List<String> to) {
        this.destination.setToAddresses(to);
    }

    @Override
    public List<String> getCC() {
        return this.destination.getCcAddresses();
    }

    @Override
    public void setCC(List<String> cc) {
        this.destination.setCcAddresses(cc);
    }

    @Override
    public List<String> getBCC() {
        return this.destination.getBccAddresses();
    }

    @Override
    public void setBCC(List<String> bcc) {
        this.destination.setBccAddresses(bcc);
    }

    @Override
    public String getSubject() {
        return null;
    }

    @Override
    public void setSubject(String subject) {
        this.subject.setData(subject);
    }

    @Override
    public String getHtmlBody() {
        return this.body.getHtml().getData();
    }

    @Override
    public void setHtmlBody(String htmlBody) {
        this.body.setHtml(new Content().withData(htmlBody));
    }

    @Override
    public String getTextBody() {
        return this.body.getText().getData();
    }

    @Override
    public void setTextBody(String textBody) {
        this.body.setText(new Content().withData(textBody));
    }

    @Override
    public SendEmailRequest compile() {
        SendEmailRequest r = new SendEmailRequest().
                withSource(this.from).
                withDestination(this.destination).
                withMessage(this.message);
        return r;
    }

    @Override
    public String toString(){
        return this.getFrom()
                + "\n" +
                this.getTo().toString()
                + "\n" +
                this.getTextBody();
    }
}
