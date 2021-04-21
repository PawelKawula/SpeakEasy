package com.speakeasy.client.net;

public interface Handler
{
    int SUCCESS = 0;
    int QUERY_FAILURE = 1;
    int DATABASE_FAILURE = 2;
    int NO_CHANGE = 3;

    byte[] serverIP = {(byte) 192, (byte) 168, 1, 120};
}
