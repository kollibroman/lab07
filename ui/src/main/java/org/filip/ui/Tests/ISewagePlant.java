package org.filip.ui.Tests;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ISewagePlant extends Remote
{
    void setPumpIn(int number, int volume) throws RemoteException;
    int getStatus(int number) throws RemoteException;
    void setPayoff(int number) throws RemoteException;
    Map<Integer, Integer> getTankerWaste() throws RemoteException;
}
