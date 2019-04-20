package InfoSecCooker.WireShark;

import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphNodes.GraphNodeInformation;

public class PacketCaptureEntry
{
    InfoSecPacket packet;
    PacketTimeStamp timeStamp;
    NetworkCardDescription networkCardWhereWasCaptured;

    public PacketCaptureEntry(InfoSecPacket packet, PacketTimeStamp timeStamp, NetworkCardDescription networkCardWhereWasCaptured)
    {
        this.packet = packet;
        this.timeStamp = timeStamp;
        this.networkCardWhereWasCaptured = networkCardWhereWasCaptured;
    }

    public InfoSecPacket getPacket()
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
