package com.speakeasy.core.models;

public class Credentials
{
    private final String userName;
    private final String password;

    public Credentials(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean equals(Object otherObject)
    {
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (getClass() != otherObject.getClass())
            return false;

        Credentials other = (Credentials) otherObject;

        return userName.equals(other.userName) && password.equals(other.password);
    }
}
