package InfoSecCooker.GraphEdge;

import InfoSecCooker.Outputs.InfoSecData;

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
    public void sendData(InfoSecData infoSecData) throws InterruptedException
    {
        arrayBlockingQueue.put(infoSecData);
    }

    @Override
    public InfoSecData receiveData()
    {
        return (InfoSecData) arrayBlockingQueue.poll();
    }

}
