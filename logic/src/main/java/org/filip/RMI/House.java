package org.filip.RMI;

import interfaces.IHouse;
import interfaces.IOffice;
import lombok.SneakyThrows;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class House implements IHouse
{
    private int sewage = 0;
    private boolean isPaused = false;
    private int maxSewage;

    private String name;

    public House(int maxSewage, String name)
    {
        this.maxSewage = maxSewage;
        this.name = name;
    }

    public void startSimulation()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() ->
        {
            if(!isPaused)
            {
                sewage += 10;

                if (sewage >= maxSewage)
                {
                    requestEmptying();
                    pause();
                }

                System.out.println("Poziom szamba: " + sewage);
            }

        }, 0, 3, TimeUnit.SECONDS);
    }

    @SneakyThrows
    private void requestEmptying()
    {
        var registry = LocateRegistry.getRegistry(2137);

        var office = (IOffice) registry.lookup("office");

        office.order(this, name);
    }

    @SneakyThrows
    @Override
    public int getPumpOut(int max) throws RemoteException
    {
        if (sewage <= 0) return 0;

        int pumpedOut = Math.min(sewage, max);
        sewage -= pumpedOut;

        System.out.println("Wypompowano: " + pumpedOut + " z szamba");
        Thread.sleep(3000);
        resume();
        return pumpedOut;
    }

    private void pause()
    {
        isPaused = true;
    }

    private void resume()
    {
        isPaused = false;
    }
}
