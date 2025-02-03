package org.filip.ui;

import interfaces.ISewagePlant;
import org.filip.ui.RMI.Office;
import org.filip.ui.RMI.SewagePlant;
import org.filip.ui.RMI.Tailor;
import org.filip.ui.Tests.IOffice;

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
