package org.filip.RMI;

import interfaces.IHouse;
import interfaces.IOffice;
import interfaces.ITanker;
import org.filip.helper.TankerDetails;

import java.rmi.RemoteException;
import java.util.*;

public class Office  implements IOffice
{
    private Map<TankerDetails, ITanker> tankers = new HashMap<>();
    private int nextTankerId = 0;

    public Office() throws RemoteException {}

    @Override
    public int register(ITanker r, String name) throws RemoteException
    {
        tankers.put(new TankerDetails(nextTankerId, false), r);
        System.out.println("Office: Registered tanker " + name + " with ID " + nextTankerId);
        return nextTankerId++;
    }

    @Override
    public int order(IHouse house, String name) throws RemoteException
    {
        if (tankers.isEmpty())
        {
            System.out.println("Office: No tankers available to handle the order.");
            return 0; // Zamówienie odrzucone
        }

        ITanker tanker = tankers.values().iterator().next();
        tanker.setJob(house);
        System.out.println("Office: Assigned tanker to handle order from " + name);
        return 1; // Zamówienie przyjęte
    }

    @Override
    public void setReadyToServe(int number) throws RemoteException
    {
        var details = tankers.keySet().stream().filter(t -> t.getTankerNumber() == number).findFirst().get();

        details.setReadyToServe(true);

        System.out.println("Office: Tanker " + number + " reported ready to serve.");
    }
}
