package org.filip.ui;

import org.filip.ui.RMI.Office;
import org.filip.ui.RMI.SewagePlant;
import org.filip.ui.RMI.Tailor;
import org.filip.ui.Tests.IOffice;
import org.filip.ui.Tests.ISewagePlant;

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
