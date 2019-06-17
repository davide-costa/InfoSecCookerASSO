package InfoSecCooker;

import InfoSecCooker.Communication.NewClientsListener;

public class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        NewClientsListener newClientsListener = new NewClientsListener();
        newClientsListener.run();
    }
}
