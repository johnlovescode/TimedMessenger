package com.johnlovescode.timedmessenger;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class Message
{
    private long id;
    private Date timeToBeSent;
    private String subject;
    private String[] contactNames;
    private String[] contactNumber;
    private String[] messageText;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Date getTimeToBeSent()
    {
        //TODO check the output of toString for correctness
        timeToBeSent.toString();
        return timeToBeSent;
    }

    public void setTimeToBeSent(Date timeToBeSent)
    {
        this.timeToBeSent = timeToBeSent;
    }

    public void setTimeToBeSent(String timeToBeSent)
    {
        //TODO check formatting/parsing correctness
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.timeToBeSent = sdf.parse(timeToBeSent,new ParsePosition(0));
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String[] getContactNames()
    {
        return contactNames;
    }

    public void setContactNames(String[] contactNames)
    {
        this.contactNames = contactNames;
    }

    public String[] getContactNumber()
    {
        return contactNumber;
    }

    public void setContactNumber(String[] contactNumber)
    {
        this.contactNumber = contactNumber;
    }

    public String[] getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String messageText)
    {
        if(messageText!=null)
        {
            ArrayList<String> messageArray = new ArrayList<>();
            char[] temp = messageText.toCharArray();
            int numberOfMessages =temp.length/160;
            if(temp.length%160>0){numberOfMessages++;}
            for(int i = 0;i<numberOfMessages;i++)
            {
                messageArray.add(Arrays.copyOfRange(temp,i*160,i*160+160).toString());
            }
            this.messageText = messageArray.toArray(new String[0]);
        }
    }

    public void setMessageText(String[] messageText)
    {
        this.messageText = messageText;
    }

    public Message(long id, Date timeToBeSent, String subject, String[] contactNames, String[] contactNumber, String messageText)
    {
        this.id = id;
        this.timeToBeSent = timeToBeSent;
        this.subject = subject;
        this.contactNames = contactNames;
        this.contactNumber = contactNumber;
        setMessageText(messageText);
    }
    //Empty constructor
    public Message(){};


}
