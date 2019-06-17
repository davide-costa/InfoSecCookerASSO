package InfoSecCooker.GraphEdge;

import InfoSecCooker.Data.InfoSecPacket;

import java.util.concurrent.ArrayBlockingQueue;

public class PipeGraphEdgeWithJavaArrayBlockingQueueImplementation extends PipeGraphEdge
{
    ArrayBlockingQueue arrayBlockingQueue;

    public PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(int bufferSize)
    {
        this.arrayBlockingQueue = new ArrayBlockingQueue(bufferSize);
    }

    public PipeGraphEdgeWithJavaArrayBlockingQueueImplementation()
    {
        this(10);
    }

    @Override
    public void sendDataOverPipe(InfoSecPacket infoSecPacket) throws InterruptedException
    {
        arrayBlockingQueue.put(infoSecPacket);

    }

    @Override
    public InfoSecPacket receiveDataFromPipe()
    {
        return (InfoSecPacket) arrayBlockingQueue.poll();
    }

}
