package InfoSecCooker.GraphLogic.GraphData;

import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.ArrayList;

public class InfoSecPacketFactory
{
    public static ArrayList<InfoSecGraphPacket> createPacketsFromArrayListOfInfoSecDataClasses(
            ArrayList<InfoSecGraphData> infoSecGraphDataArrayList,
            GraphNodeInformation source, GraphNodeInformation destination,
            int sourcePort, int destinationPort)
    {
        ArrayList<InfoSecGraphPacket> packets = new ArrayList<>();
        for (InfoSecGraphData data : infoSecGraphDataArrayList)
        {
            InfoSecGraphPacket packet = new InfoSecGraphPacket(data, source, destination, sourcePort, destinationPort);
            packets.add(packet);
        }

        return packets;
    }
}
