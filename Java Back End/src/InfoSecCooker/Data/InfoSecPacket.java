package InfoSecCooker.Data;

import InfoSecCooker.GraphNodes.GraphNodeInformation;

import java.io.Serializable;

public class InfoSecPacket
{
    private InfoSecData infoSecData;
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

    public InfoSecPacket(InfoSecData infoSecData, GraphNodeInformation source,
                         GraphNodeInformation destination, int sourcePort, int destinationPort)
    {
        this.infoSecData = infoSecData;
        this.source = source;
        this.destination = destination;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
    }

    public InfoSecData getInfoSecData()
    {
        return infoSecData;
    }

    public InfoSecPacket getCopy()
    {
        return new InfoSecPacket(infoSecData.getCopy(), source, destination, sourcePort, destinationPort);
    }

    @Override
    public String toString()
    {
        //TODO
        return null;
    }
}
