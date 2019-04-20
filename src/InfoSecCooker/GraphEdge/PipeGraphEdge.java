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
    private boolean countDataTransferred;
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

    public PipeGraphEdge(boolean countDataTransferred, boolean enablePacketCapture)
    {
        this.countDataTransferred = countDataTransferred;
        this.enablePacketCapture = enablePacketCapture;
    }

    public void sendData(InfoSecPacket infoSecPacket) throws InterruptedException
    {
        if (countDataTransferred)
            dataSentCount++;
        if (enablePacketCapture)
            dataSent.add(infoSecPacket);

        sendDataOverPipe(infoSecPacket);
    }

    public abstract void sendDataOverPipe(InfoSecPacket infoSecPacket) throws InterruptedException;

    public InfoSecPacket receiveData()
    {
        InfoSecPacket infoSecPacket = receiveDataFromPipe();
        if (countDataTransferred)
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
