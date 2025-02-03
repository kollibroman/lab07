package org.filip.ui.RMI;

import interfaces.IHouse;
import interfaces.ITanker;
import lombok.Getter;
import org.filip.ui.helper.TankerDetails;
import org.filip.ui.Tests.IOffice;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Office  implements IOffice
{
    @Getter
    private Map<TankerDetails, ITanker> tankers = new HashMap<>();

    @Getter
    private Map<String, LocalDateTime> houseOrders = new ConcurrentHashMap<>();
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
        System.out.println("Office: Received order from " + name);
        houseOrders.put(name, LocalDateTime.now());

        if (tankers.isEmpty())
        {
            System.out.println("Office: No tankers available to handle the order.");
            trackHouseOrder(name); // Track the order even if it can't be processed immediately
            return 0; // Order rejected
        }

        System.out.println("Office: Checking for available tankers to handle the order.");
        System.out.println("Office: Found " + tankers.size() + " tankers.");

        var availableTanker = tankers.entrySet().stream()
                .filter(entry -> entry.getKey().isReadyToServe())
                .findFirst();

        if (availableTanker.isPresent()) {
            ITanker tanker = availableTanker.get().getValue();
            tanker.setJob(house);
            trackHouseOrder(name);
            System.out.println("Office: Assigned tanker to handle order from " + name);
            return 1; // Order accepted
        }
        else
        {
            System.out.println("Office: No ready tankers available to handle the order.");
            trackHouseOrder(name); // Track the order even if it can't be processed immediately
            return 0; // Order rejected
        }
    }

    @Override
    public void setReadyToServe(int number) throws RemoteException
    {
        var details = tankers.keySet().stream().filter(t -> t.getTankerNumber() == number).findFirst().get();

        details.setReadyToServe(true);

        System.out.println("Office: Tanker " + number + " reported ready to serve.");
    }

    public void trackHouseOrder(String houseName)
    {
        houseOrders.put(houseName, LocalDateTime.now());
    }
}
