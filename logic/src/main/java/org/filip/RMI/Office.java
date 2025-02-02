package org.filip.RMI;

import interfaces.IHouse;
import interfaces.IOffice;
import interfaces.ITanker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Office extends UnicastRemoteObject implements IOffice
{
    protected Office(int port) throws RemoteException
    {
        super(port);
    }

    @Override
    public int register(ITanker r, String name) throws RemoteException
    {
        return 0;
    }

    @Override
    public int order(IHouse house, String name) throws RemoteException
    {
        return 0;
    }

    @Override
    public void setReadyToServe(int number) throws RemoteException
    {

    }
}
