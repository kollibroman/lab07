package org.filip.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tailor
{
    private static final int REGISTRY_PORT = 2137;
    private static Registry registry;
    private static final Map<String, Remote> localServices = new HashMap<>();

    static {
        try {
            try
            {
                registry = LocateRegistry.getRegistry(REGISTRY_PORT);
                registry.list(); // Test if registry is running
            }
            catch (RemoteException e) {
                // If registry doesn't exist, create it
                registry = LocateRegistry.createRegistry(REGISTRY_PORT);
                System.out.println("Created new RMI registry on port " + REGISTRY_PORT);
            }
        } catch (RemoteException e) {
            System.err.println("Failed to initialize RMI registry: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Register a service
    public static void register(String serviceName, Remote service) throws RemoteException
    {
        try
        {
            Remote stub = UnicastRemoteObject.exportObject(service, 0);
            registry.rebind(serviceName, stub);
            localServices.put(serviceName, service);
            System.out.println("Registered service: " + serviceName);
        }

        catch (Exception e)
        {
            System.err.println("Failed to register service " + serviceName + ": " + e.getMessage());
            throw e;
        }
    }

    // Lookup a service
    public static <T extends Remote> T lookup(String host, String serviceName) throws RemoteException
    {
        try
        {
            Registry remoteRegistry = LocateRegistry.getRegistry(host, REGISTRY_PORT);
            return (T) remoteRegistry.lookup(serviceName);
        }
        catch (Exception e)
        {
            System.err.println("Failed to lookup service " + serviceName + " at " + host + ": " + e.getMessage());
            throw new RemoteException("Service lookup failed", e);
        }
    }

    // Unregister a service
    public static void unregister(String serviceName) throws RemoteException
    {
        try {
            registry.unbind(serviceName);
            Remote service = localServices.remove(serviceName);
            if (service != null) {
                UnicastRemoteObject.unexportObject(service, true);
            }
            System.out.println("Unregistered service: " + serviceName);
        } catch (Exception e) {
            System.err.println("Failed to unregister service " + serviceName + ": " + e.getMessage());
            throw new RemoteException("Service unregistration failed", e);
        }
    }

    // List all registered services
    public static String[] listServices() throws RemoteException
    {
        try {
            return registry.list();
        } catch (Exception e) {
            System.err.println("Failed to list services: " + e.getMessage());
            throw new RemoteException("Service listing failed", e);
        }
    }

    // Shutdown hook to clean up services
    public static void registerShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            for (String serviceName : new ArrayList<>(localServices.keySet()))
            {
                try {
                    unregister(serviceName);
                } catch (Exception e) {
                    System.err.println("Failed to unregister service " + serviceName + " during shutdown");
                }
            }
        }));
    }
}
