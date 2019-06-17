package InfoSecCooker.GraphLogic.GraphWireShark;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;

import java.util.ArrayList;

public class PacketRegistry
{
    private ArrayList<PacketCaptureEntry> packets = new ArrayList<>();

    public void registerPacketCaptured(InfoSecGraphPacket packet, NetworkCardDescription networkCardWhereWasCaptured)
    {
        PacketTimeStamp timeStamp = new PacketTimeStamp(); //default constructor uses current time
        PacketCaptureEntry packetCaptureEntry = new PacketCaptureEntry(packet, timeStamp, networkCardWhereWasCaptured);
        packets.add(packetCaptureEntry);
    }
}
