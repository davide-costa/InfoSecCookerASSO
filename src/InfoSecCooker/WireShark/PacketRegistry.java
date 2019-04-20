package InfoSecCooker.WireShark;

import InfoSecCooker.Data.InfoSecPacket;

import java.util.ArrayList;

public class PacketRegistry
{
    private ArrayList<PacketCaptureEntry> packets;

    public void registerPacketCaptured(InfoSecPacket packet, NetworkCardDescription networkCardWhereWasCaptured)
    {
        PacketTimeStamp timeStamp = new PacketTimeStamp(); //default constructor uses current time
        PacketCaptureEntry packetCaptureEntry = new PacketCaptureEntry(packet, timeStamp, networkCardWhereWasCaptured);
        packets.add(packetCaptureEntry);
    }
}
