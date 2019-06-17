package InfoSecCooker.GraphLogic.GraphWireShark;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;

public class PacketCaptureEntry
{
    InfoSecGraphPacket packet;
    PacketTimeStamp timeStamp;
    NetworkCardDescription networkCardWhereWasCaptured;

    public PacketCaptureEntry(InfoSecGraphPacket packet, PacketTimeStamp timeStamp, NetworkCardDescription networkCardWhereWasCaptured)
    {
        this.packet = packet;
        this.timeStamp = timeStamp;
        this.networkCardWhereWasCaptured = networkCardWhereWasCaptured;
    }

    public InfoSecGraphPacket getPacket()
    {
        return packet;
    }

    public PacketTimeStamp getTimeStamp()
    {
        return timeStamp;
    }

    public NetworkCardDescription getNetworkCardWhereWasCaptured()
    {
        return networkCardWhereWasCaptured;
    }
}
