package InfoSecCooker.GraphLogic.GraphData;

import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

public class InfoSecGraphPacket
{
    private InfoSecGraphData infoSecGraphData;
    /**
     * Equivalent to source IP Address in computer networks
     */
    private GraphNodeInformation source;
    /**
     * Equivalent to destination IP Address in computer networks
     */
    private GraphNodeInformation destination;
    /**
     * This is the output number of the node (just like source port in computer networks) where the packet came from
     */
    private int sourcePort;
    /**
     * This is the input number of the node (just like source port in computer networks) to where the packet was sent
     */
    private int destinationPort;

    public InfoSecGraphPacket(InfoSecGraphData infoSecGraphData, GraphNodeInformation source,
                              GraphNodeInformation destination, int sourcePort, int destinationPort)
    {
        this.infoSecGraphData = infoSecGraphData;
        this.source = source;
        this.destination = destination;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
    }

    public InfoSecGraphData getInfoSecGraphData()
    {
        return infoSecGraphData;
    }

    public InfoSecGraphPacket getCopy()
    {
        return new InfoSecGraphPacket(infoSecGraphData.getCopy(), source, destination, sourcePort, destinationPort);
    }
}
