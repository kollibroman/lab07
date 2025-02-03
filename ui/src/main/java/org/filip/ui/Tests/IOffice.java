package org.filip.ui.Tests;

import interfaces.IHouse;
import interfaces.ITanker;
import org.filip.ui.helper.TankerDetails;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IOffice extends Remote
{
    int register(ITanker r, String name) throws RemoteException;
    int order(IHouse house, String name) throws RemoteException;
    void setReadyToServe(int number) throws RemoteException;
    Map<TankerDetails, ITanker> getTankers() throws RemoteException;
    Map<String, java.time.LocalDateTime> getHouseOrders() throws RemoteException;
}