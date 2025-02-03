package org.filip.ui.RMI;

import interfaces.ISewagePlant;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class SewagePlant implements ISewagePlant
{
    private Map<Integer, Integer> tankerWaste = new HashMap<>();

    public SewagePlant() throws RemoteException {}

    @Override
    public void setPumpIn(int number, int volume) throws RemoteException
    {
        tankerWaste.put(number, tankerWaste.getOrDefault(number, 0) + volume);
        System.out.println("SewagePlant: Received " + volume + " units of waste from tanker " + number);
    }

    @Override
    public int getStatus(int number) throws RemoteException
    {
        return tankerWaste.getOrDefault(number, 0);
    }

    @Override
    public void setPayoff(int number) throws RemoteException
    {
        if(tankerWaste.containsKey(number))
        {
            tankerWaste.put(number, 0);
            System.out.println("Set tanker nr. " + number + " to " + tankerWaste.get(number));
        }

        else
        {
            throw new RemoteException();
        }
    }
}
