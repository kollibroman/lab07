package org.filip.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Tailor
{
    public static void main(String[] args)
    {
        try
        {
            var registry = LocateRegistry.createRegistry(2137);

            while (true) {}
        }

        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
