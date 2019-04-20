package InfoSecCooker.GraphEdge;

import InfoSecCooker.Data.InfoSecData;

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
    public void sendDataOverPipe(InfoSecData infoSecData) throws InterruptedException
    {
        arrayBlockingQueue.put(infoSecData);

    }

    @Override
    public InfoSecData receiveDataFromPipe()
    {
        return (InfoSecData) arrayBlockingQueue.poll();
    }

}
