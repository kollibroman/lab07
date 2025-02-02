package org.filip.RMI;

import interfaces.ISewagePlant;

import java.rmi.RemoteException;

public class SewagePlant implements ISewagePlant
{

    @Override
    public void setPumpIn(int number, int volume) throws RemoteException {

    }

    @Override
    public int getStatus(int number) throws RemoteException {
        return 0;
    }

    @Override
    public void setPayoff(int number) throws RemoteException {

    }
}
