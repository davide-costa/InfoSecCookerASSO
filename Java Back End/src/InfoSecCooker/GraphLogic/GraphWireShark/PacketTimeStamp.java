package InfoSecCooker.GraphLogic.GraphWireShark;

public class PacketTimeStamp
{
    long milliSecondsTimeStamp;
    long nanoSecondsTimeStamp;

    public PacketTimeStamp()
    {
        milliSecondsTimeStamp = System.currentTimeMillis();
        nanoSecondsTimeStamp = System.nanoTime();
    }
}
