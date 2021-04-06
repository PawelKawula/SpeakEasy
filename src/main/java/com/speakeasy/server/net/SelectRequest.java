package com.speakeasy.server.net;

import com.speakeasy.core.models.Credentials;

public abstract class SelectRequest
{
    private Credentials credentials;

    public SelectRequest(Credentials credentials)
    {
        this.credentials = credentials;
    }

    public Credentials getCredentials()
    {
        return credentials;
    }

}
