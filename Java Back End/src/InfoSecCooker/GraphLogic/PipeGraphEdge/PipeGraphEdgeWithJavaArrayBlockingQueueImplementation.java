package InfoSecCooker.GraphLogic.PipeGraphEdge;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphPacket;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;

import java.util.concurrent.ArrayBlockingQueue;

public class PipeGraphEdgeWithJavaArrayBlockingQueueImplementation extends PipeGraphEdge
{
    ArrayBlockingQueue arrayBlockingQueue;

    public PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(RunTimeConfigurations runTimeConfigurations, int bufferSize)
    {
        super(runTimeConfigurations);
        this.arrayBlockingQueue = new ArrayBlockingQueue(bufferSize);
    }

    public PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(RunTimeConfigurations runTimeConfigurations)
    {
        this(runTimeConfigurations, runTimeConfigurations.pipeBufSize.get());
    }

    @Override
    public void sendDataOverPipe(InfoSecGraphPacket infoSecGraphPacket) throws InterruptedException
    {
        Boolean discardPacketsIfPipesAreFull = runTimeConfigurations.discardPacketsIfPipesAreFull.get();
        if (discardPacketsIfPipesAreFull)
            if (arrayBlockingQueue.remainingCapacity() <= 1)
                arrayBlockingQueue.poll();

        arrayBlockingQueue.put(infoSecGraphPacket);
    }

    @Override
    public InfoSecGraphPacket receiveDataFromPipe()
    {
        try
        {
            return (InfoSecGraphPacket) arrayBlockingQueue.take();
        } catch (InterruptedException e)
        {
            return null;
        }
    }

}
