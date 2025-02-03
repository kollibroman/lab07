package org.filip.ui.RMI;

import interfaces.IHouse;
import interfaces.ITanker;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.filip.ui.Tests.IOffice;
import org.filip.ui.Tests.ISewagePlant;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

@Getter
public class Tanker extends UnicastRemoteObject implements ITanker
{
    private int maxCapacity;
    private int currentLoad = 0;
    private int id;

    public Tanker(int maxCapacity) throws RemoteException
    {
        this.maxCapacity = maxCapacity;
    }

    public void registerAtOffice(IOffice office, String name) throws RemoteException
    {
        int tankerId = office.register(this, name);

        id = tankerId;

        System.out.println("Tanker: Registered at the office with ID " + tankerId);
    }

    @Override
    @SneakyThrows
    public void setJob(IHouse house) throws RemoteException
    {
        System.out.println("Tanker: Received job from the office.");
        int volume = house.getPumpOut(30);

        ISewagePlant sewagePlant = Tailor.lookup("localhost", "SewagePlant");
        IOffice office = Tailor.lookup("localhost", "Office");
        sewagePlant.setPumpIn(id, volume);
        System.out.println("sent to sewage plant");

        Thread.sleep(new Random().nextInt(5000, 8000));
        office.setReadyToServe(id);
        System.out.println("ready to serve");
    }

    public void emptyAtSewagePlant(ISewagePlant sewagePlant, int tankerId) throws RemoteException
    {
        sewagePlant.setPumpIn(tankerId, currentLoad);
        System.out.println("Tanker: Emptied " + currentLoad + " units of waste at the sewage plant.");
        currentLoad = 0;
    }
}
