package InfoSecCooker.GraphLogic.PipeGraphEdge;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Since this is a DAG, this pipe only needs to transfer in one direction (djirigido)
 */
public abstract class PipeGraphEdge
{
    private static AtomicLong curr_id = new AtomicLong(0);

    long id;
    private TaskGraphNode source;
    private TaskGraphNode destination;
    /**
     * This represents the number the output to which this pipe is connected.
     * This is the equivalent to a network port.
     */
    private int sourcePort;
    /**
     * This represents the number the input to which this pipe is connected.
     * This is the equivalent to a network port.
     */
    private int destinationPort;

    private boolean enableCountDataTransferred;
    private boolean enablePacketCapture; //wireshark ftw ^^

    private long dataSentCount;
    private long dataReceivedCount;
    private ArrayList<InfoSecGraphPacket> dataSent;
    private ArrayList<InfoSecGraphPacket> dataReceived;

    RunTimeConfigurations runTimeConfigurations;

    public PipeGraphEdge(RunTimeConfigurations runTimeConfigurations)
    {
        this(runTimeConfigurations, runTimeConfigurations.enablePacketCaptureOnPipeGraphEdges.get(), runTimeConfigurations.enablePacketCaptureOnPipeGraphEdges.get());
    }

    public PipeGraphEdge(RunTimeConfigurations runTimeConfigurations, boolean enableCountDataTransferred, boolean enablePacketCapture)
    {
        id = curr_id.incrementAndGet();
        dataSentCount = 0;
        dataReceivedCount = 0;
        dataSent = new ArrayList<>();
        dataReceived = new ArrayList<>();
        this.enableCountDataTransferred = enableCountDataTransferred;
        this.enablePacketCapture = enablePacketCapture;
        this.runTimeConfigurations = runTimeConfigurations;
    }

    public void sendData(InfoSecGraphPacket infoSecGraphPacket) throws InterruptedException
    {
        if (enableCountDataTransferred)
            dataSentCount++;
        if (enablePacketCapture)
            dataSent.add(infoSecGraphPacket);

        sendDataOverPipe(infoSecGraphPacket);
    }

    public abstract void sendDataOverPipe(InfoSecGraphPacket infoSecGraphPacket) throws InterruptedException;

    public InfoSecGraphPacket receiveData()
    {
        InfoSecGraphPacket infoSecGraphPacket = receiveDataFromPipe();
        if (enableCountDataTransferred)
            dataReceivedCount++;
        if (enablePacketCapture)
            dataReceived.add(infoSecGraphPacket);

        return infoSecGraphPacket;
    }

    public abstract InfoSecGraphPacket receiveDataFromPipe();

    public long getId()
    {
        return id;
    }

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

    public ArrayList<InfoSecGraphPacket> getDataSent()
    {
        return dataSent;
    }

    public ArrayList<InfoSecGraphPacket> getDataReceived()
    {
        return dataReceived;
    }
}
