package com.speakeasy.server.requests;

public interface Request
{
    int LOGIN_REQUEST = 0;
    int FRIENDS_REFRESH = 1;
    int MESSAGES_REFRESH = 2;
    int MESSAGE_SEND= 3;
    int LOGOUT = 4;
    int ACCOUNT_ALTERCATION_REQUEST = 5;
    int FRIEND_ACCEPT = 6;
    int FRIEND_REMOVE = 7;
    int FRIEND_ADD = 8;
    int SUCCESS = 18;
    int FAILURE = 19;
}
