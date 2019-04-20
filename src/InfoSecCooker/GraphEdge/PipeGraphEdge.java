package InfoSecCooker.GraphEdge;

import InfoSecCooker.GraphNodes.TaskGraphNode;
import InfoSecCooker.Outputs.InfoSecData;

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
    private ArrayList<InfoSecData> dataSent;
    private ArrayList<InfoSecData> dataReceived;


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

    public void sendData(InfoSecData infoSecData) throws InterruptedException
    {
        if (countDataTransferred)
            dataSentCount++;
        if (enablePacketCapture)
            dataSent.add(infoSecData);

        sendDataOverPipe(infoSecData);
    }

    public abstract void sendDataOverPipe(InfoSecData infoSecData) throws InterruptedException;

    public InfoSecData receiveData()
    {
        InfoSecData infoSecData = receiveDataFromPipe();
        if (countDataTransferred)
            dataReceivedCount++;
        if (enablePacketCapture)
            dataReceived.add(infoSecData);

        return infoSecData;
    }

    public abstract InfoSecData receiveDataFromPipe();

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

    public ArrayList<InfoSecData> getDataSent()
    {
        return dataSent;
    }

    public ArrayList<InfoSecData> getDataReceived()
    {
        return dataReceived;
    }
}
