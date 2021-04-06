package com.speakeasy.server.net;

public enum Request
{
    LOGIN_REQUEST(0), FRIEND_SELECT_REQUEST(1), MESSAGES_SELECT_REQUEST(2),
    MESSAGES_SEND_REQUEST(3), LOGOUT_REQUEST(4), ACCOUNT_ALTERCATION_REQUEST(5);

    private final int value;
    private Request(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
