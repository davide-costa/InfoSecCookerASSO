package InfoSecCooker.GraphEdge;

import InfoSecCooker.Data.InfoSecPacket;
import InfoSecCooker.GraphNodes.TaskGraphNode;

import java.util.ArrayList;

/**
 * Since this is a DAG, this pipe only needs to transfer in one direction (djirigido)
 */
public abstract class PipeGraphEdge
{
    private TaskGraphNode source;
    private TaskGraphNode destination;
    /**
     * This represents the number the input to which this pipe is connected.
     * This is the equivalent to a network port.
     */
    private int sourcePort;
    /**
     * This represents the number the output to which this pipe is connected.
     * This is the equivalent to a network port.
     */
    private int destinationPort;

    private boolean enableCountDataTransferred;
    private boolean enablePacketCapture; //wireshark ftw ^^

    private long dataSentCount;
    private long dataReceivedCount;
    private ArrayList<InfoSecPacket> dataSent;
    private ArrayList<InfoSecPacket> dataReceived;


    public PipeGraphEdge()
    {
        this(true, true);
        dataSentCount = 0;
        dataReceivedCount = 0;
        dataSent = new ArrayList<>();
        dataReceived = new ArrayList<>();
    }

    public PipeGraphEdge(boolean enableCountDataTransferred, boolean enablePacketCapture)
    {
        this.enableCountDataTransferred = enableCountDataTransferred;
        this.enablePacketCapture = enablePacketCapture;
    }

    public void sendData(InfoSecPacket infoSecPacket) throws InterruptedException
    {
        if (enableCountDataTransferred)
            dataSentCount++;
        if (enablePacketCapture)
            dataSent.add(infoSecPacket);

        sendDataOverPipe(infoSecPacket);
    }

    public abstract void sendDataOverPipe(InfoSecPacket infoSecPacket) throws InterruptedException;

    public InfoSecPacket receiveData()
    {
        InfoSecPacket infoSecPacket = receiveDataFromPipe();
        if (enableCountDataTransferred)
            dataReceivedCount++;
        if (enablePacketCapture)
            dataReceived.add(infoSecPacket);

        return infoSecPacket;
    }

    public abstract InfoSecPacket receiveDataFromPipe();

    public TaskGraphNode getSource()
    {
        return source;
    }

    public TaskGraphNode getDestination()
    {
        return destination;
    }

    public void setSource(TaskGraphNode source)
    {
        this.source = source;
    }

    public void setDestination(TaskGraphNode destination)
    {
        this.destination = destination;
    }

    public int getSourcePort()
    {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort)
    {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort()
    {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort)
    {
        this.destinationPort = destinationPort;
    }

    public long getDataSentCount()
    {
        return dataSentCount;
    }

    public long getDataReceivedCount()
    {
        return dataReceivedCount;
    }

    public ArrayList<InfoSecPacket> getDataSent()
    {
        return dataSent;
    }

    public ArrayList<InfoSecPacket> getDataReceived()
    {
        return dataReceived;
    }
}
