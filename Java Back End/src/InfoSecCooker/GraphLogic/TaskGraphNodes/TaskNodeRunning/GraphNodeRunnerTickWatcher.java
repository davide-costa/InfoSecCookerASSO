package InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning;

import java.util.Observable;

/**
 * This class is more for debugging
 */
public class GraphNodeRunnerTickWatcher extends Observable implements Runnable
{
    Object monitor;
    TaskRunnerController taskRunnerController;
    TaskRunningRegistry taskRunningRegistry;
    TaskRunningRegistry taskRunningRegistrySinceEver;

    public GraphNodeRunnerTickWatcher(Object monitor, TaskRunnerController taskRunnerController, TaskRunningRegistry taskRunningRegistry, TaskRunningRegistry taskRunningRegistrySinceEver)
    {
        this.monitor = monitor;
        this.taskRunnerController = taskRunnerController;
        this.taskRunningRegistry = taskRunningRegistry;
        this.taskRunningRegistrySinceEver = taskRunningRegistrySinceEver;
    }

    public TaskRunnerController getTaskRunnerController()
    {
        return taskRunnerController;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                synchronized (monitor)
                {
                    monitor.wait();
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            TickEntry tickEntry = new TickEntry(taskRunnerController.getTaskGraphNode().getGraphNodeInformation(), System.nanoTime(), System.currentTimeMillis());
            try
            {
                taskRunningRegistry.registerTickEntry(tickEntry);
                taskRunningRegistrySinceEver.registerTickEntry(tickEntry);
            } catch (InterruptedException e)
            {
                System.out.println("Error registering tick entry");
                e.printStackTrace();
            }
        }

    }
}
