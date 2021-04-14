package com.speakeasy.server.requests;

public interface Request
{
    int LOGIN_REQUEST = 0;
    int FRIENDS_REFRESH = 1;
    int MESSAGES_REFRESH = 2;
    int MESSAGES_SEND_REQUEST = 3;
    int LOGOUT_REQUEST = 4;
    int ACCOUNT_ALTERCATION_REQUEST = 5;
    int SUCCESS = 6;
    int FAILURE = 7;
}
