package org.filip.ui.RMI;

import interfaces.IHouse;
import lombok.Getter;
import lombok.SneakyThrows;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.filip.ui.Tests.IOffice;

@Getter
public class House extends UnicastRemoteObject implements IHouse
{
    private int capacity;
    private AtomicInteger currentVolume;
    private Boolean isPaused = false;
    private int id;

    public House(int capacity, int id) throws RemoteException
    {
        this.capacity = capacity;
        this.currentVolume = new AtomicInteger(0);
        this.id = id;
    }

    public void startSimulation()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() ->
        {
            if(!isPaused)
            {
                currentVolume.addAndGet(10);
                System.out.println("Poziom szamba: " + currentVolume.get());
                if (currentVolume.get() >= capacity)
                {
                    pause();
                    requestEmptying();

                }
            }

        }, 0, 3, TimeUnit.SECONDS);
    }

    public   void pause()
    {
        isPaused = true;
    }

    public void resume()
    {
        isPaused = false;
    }

    @SneakyThrows
    @Override
    public int getPumpOut(int max) throws RemoteException
    {
        if (currentVolume.get() <= 0) return 0;

        int pumpedOut = Math.min(currentVolume.get(), capacity);

        for (int i = 0; i <= pumpedOut; i++)
        {
            currentVolume.decrementAndGet();
        }

        System.out.println("Wypompowano: " + pumpedOut + " z szamba");
        Thread.sleep(3000);
        resume();
        return pumpedOut;
    }

    @SneakyThrows
    private void requestEmptying()
    {
        System.out.println("House: Requesting emptying of the sewage tank.");
        var r = LocateRegistry.getRegistry(2137);

        IOffice office = (IOffice) r.lookup("Office");
        int response = office.order(this, "House" + id);

        System.out.println("House: Requested emptying of the sewage tank. Response: " + response);
    }
}
