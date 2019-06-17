package InfoSecCooker.GraphNodes;

import InfoSecCooker.GraphNodes.GraphNodeRunner;

import java.util.Observable;

/**
 * This class is more for debugging
 */
public class GraphNodeRunnerTickWatcher extends Observable implements Runnable
{
    Object monitor;
    GraphNodeRunner graphNodeRunner;

    public GraphNodeRunnerTickWatcher(Object monitor, GraphNodeRunner graphNodeRunner)
    {
        this.monitor = monitor;
        this.graphNodeRunner = graphNodeRunner;
    }

    public GraphNodeRunner getGraphNodeRunner()
    {
        return graphNodeRunner;
    }

    @Override
    public void run()
    {
        try
        {
            monitor.wait();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        notifyObservers(graphNodeRunner);
    }
}
