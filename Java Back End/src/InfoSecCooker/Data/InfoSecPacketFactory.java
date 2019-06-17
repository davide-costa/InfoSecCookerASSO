package InfoSecCooker.Data;

import InfoSecCooker.GraphNodes.GraphNodeInformation;

import java.util.ArrayList;

public class InfoSecPacketFactory
{
    public static ArrayList<InfoSecPacket> createPacketsFromArrayListOfInfoSecDataClasses(
            ArrayList<InfoSecData> infoSecDataArrayList,
            InfoSecData infoSecData, GraphNodeInformation source,
            GraphNodeInformation destination, int sourcePort, int destinationPort)
    {
        ArrayList<InfoSecPacket> packets = new ArrayList<>();
        for (InfoSecData data : infoSecDataArrayList)
        {
            InfoSecPacket packet = new InfoSecPacket(data, source, destination, sourcePort, destinationPort);
            packets.add(packet);
        }

        return packets;
    }
}
