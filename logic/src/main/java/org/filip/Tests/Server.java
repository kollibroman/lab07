package org.filip.Tests;

import interfaces.IOffice;
import interfaces.ISewagePlant;
import org.filip.RMI.Office;
import org.filip.RMI.SewagePlant;
import org.filip.RMI.Tailor;

public class Server
{
    public static void main(String[] args)
    {
        try {
            Tailor.registerShutdownHook();

            IOffice office = new Office();
            ISewagePlant sewagePlant = new SewagePlant();

            // Rejestracja usług w RMI
            Tailor.register("Office", office);
            Tailor.register("SewagePlant", sewagePlant);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
