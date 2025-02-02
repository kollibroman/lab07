package org.filip.RMI;

import interfaces.IHouse;
import interfaces.ITanker;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Tanker extends UnicastRemoteObject implements ITanker
{
    protected Tanker() throws RemoteException {
    }

    @Override
    public void setJob(IHouse house) throws RemoteException
    {

    }
}
