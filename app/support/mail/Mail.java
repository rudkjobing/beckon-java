package support.mail;

import javax.lang.model.type.NoType;
import java.util.List;

/**
 * Created by Steffen Rudkjøbing on 22/02/15.
 * © 2014 Steffen Rudkjøbing
 */
public interface Mail {

    public String getFrom();

    public void setFrom(String from);

    public List<String> getTo();

    public void setTo(List<String> to);

    public List<String> getCC();

    public void setCC(List<String> cc);

    public List<String> getBCC();

    public void setBCC(List<String> bcc);

    public String getSubject();

    public void setSubject(String subject);

    public String getHtmlBody();

    public void setHtmlBody(String htmlBody);

    public String getTextBody();

    public void setTextBody(String textBody);

    public Object compile();

}
