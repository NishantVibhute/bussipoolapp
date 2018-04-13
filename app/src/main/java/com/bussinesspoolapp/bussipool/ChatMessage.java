package com.bussinesspoolapp.bussipool;

/**
 * Created by Nishant on 2/27/2018.
 */

public class ChatMessage {
    public String name;
    public boolean left;
    public String message;
            public String dateTime;
            public String shortForm;
            public String replyTo;

            public String userName;


    public ChatMessage(boolean left, String message, String name,String dateTime,
                       String shortForm,String replyTo,String userName) {
        super();
        this.left = left;
        this.message = message;
        this.name=name;
        this.dateTime=dateTime;
        this.shortForm = shortForm;
        this.replyTo = replyTo;
        this.userName = userName;

    }
}
