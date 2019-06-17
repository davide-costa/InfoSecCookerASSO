package InfoSecCooker.GraphNodes;

import InfoSecCooker.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;

/**
 * This class implements the tick functionality time logic of a node.
 * It calls the tick method of the TaskGraphNode every tickInterval milliSeconds.
 * It implements the tick, cycle or heartbeat functionality for each node.
 * For example, if a node is supposed to generate data every 500 milliSeconds, then 500 should be passed to the constructor.
 * If a node is supposed to be always listening for data (interrupt based) and immediately process it after receiving, 0 milliSeconds should be passed to the constructor.
 */
public class GraphNodeRunner implements Runnable
{
    /**
     * The state is IDLING the default state, when the GraphNodeRunner has just been created.
     * The task remains in state IDLING while the thread as not yet been started (i.e., the run method has not yet been executed).
     * When it is waiting for time to pass to to implement the cycles logic based on time and it is sleeping, the state is SLEEPING.
     * When it has called the function tick of the TaskGraphNode and it is waiting for the function to return, the state is WORKING. For more specific state information, the state on the TaskGraphNode should be checked.
     */
    enum GraphNodeRunnerState {IDLING, SLEEPING, WORKING}

    TaskGraphNode taskGraphNode;
    Object tickNotificationMonitor;
    int tickInterval;
    boolean tickImmediatelyAfterLaunch;
    boolean subtractExecutionTimeToSleepTime;
    GraphNodeRunnerState state;

    public GraphNodeRunner(TaskGraphNode taskGraphNode, Object tickNotificationMonitor, int tickInterval)
    {
        this(taskGraphNode, tickNotificationMonitor, tickInterval, true, true);
        state = GraphNodeRunnerState.IDLING;
    }

    public GraphNodeRunner(TaskGraphNode taskGraphNode, Object tickNotificationMonitor, int tickInterval,
                           boolean tickImmediatelyAfterLaunch, boolean subtractExecutionTimeToSleepTime)
    {
        this.taskGraphNode = taskGraphNode;
        this.tickNotificationMonitor = tickNotificationMonitor;
        this.tickInterval = tickInterval;
        this.tickImmediatelyAfterLaunch = tickImmediatelyAfterLaunch;
        this.subtractExecutionTimeToSleepTime = subtractExecutionTimeToSleepTime;
    }

    public TaskGraphNode getTaskGraphNode()
    {
        return taskGraphNode;
    }

    /**
     * This method implements the tick functionality time logic of a node.
     * It calls the tick method of the TaskGraphNode every tickInterval milliSeconds.
     * It implements the tick, cycle or heartbeat functionality for each node.
     * This method notifies (using the monitor passed as parameter) when a tick occurs.
     */
    @Override
    public void run()
    {
        long nextSleepInterval = tickInterval;
        while (true)
        {
            try
            {
                if (!tickImmediatelyAfterLaunch)
                {
                    state = GraphNodeRunnerState.SLEEPING;
                    Thread.sleep(nextSleepInterval);
                    nextSleepInterval = tickInterval;
                }

                long startTimeMillis = 0;
                if (subtractExecutionTimeToSleepTime)
                    startTimeMillis = System.currentTimeMillis();

                try
                {
                    state = GraphNodeRunnerState.WORKING;
                    taskGraphNode.tick();
                } catch (InfoSecCookerRuntimeException e)
                {
                    //TODO notify tick watcher of error
                    e.printStackTrace();
                }
                synchronized (tickNotificationMonitor)
                {
                    tickNotificationMonitor.notify();
                }

                long endTimeMillis;
                if (subtractExecutionTimeToSleepTime)
                {
                    endTimeMillis = System.currentTimeMillis();
                    long diff = endTimeMillis - startTimeMillis;
                    nextSleepInterval = diff;
                }

                if (tickImmediatelyAfterLaunch)
                {
                    state = GraphNodeRunnerState.SLEEPING;
                    Thread.sleep(nextSleepInterval);
                    nextSleepInterval = tickInterval;

                }


            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


}
