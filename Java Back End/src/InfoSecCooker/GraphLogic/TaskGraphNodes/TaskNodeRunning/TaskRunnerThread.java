package InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning;

import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.Collections.CollectionsException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ExpectedEdgeOnNodeInputButNotFound;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InfoSecCookerRuntimeException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.NullDataReceivedFromGraphNodeAsInput;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class TaskRunnerThread extends TimerTask
{
    TaskGraphNode taskGraphNode;
    Object tickNotificationMonitor;
    Object tickFinishedNotificationMonitor;
    AtomicLong startTimeMillis;
    TaskRunnerController taskRunnerController;

    public TaskRunnerThread(TaskGraphNode taskGraphNode, Object tickNotificationMonitor, Object tickFinishedNotificationMonitor, AtomicLong startTimeMillis, TaskRunnerController taskRunnerController)
    {
        this.taskGraphNode = taskGraphNode;
        this.tickNotificationMonitor = tickNotificationMonitor;
        this.tickFinishedNotificationMonitor = tickFinishedNotificationMonitor;
        this.startTimeMillis = startTimeMillis;
        this.taskRunnerController = taskRunnerController;
    }

    @Override
    public void run()
    {
        startTimeMillis.set(System.currentTimeMillis());
        System.out.println("Node with id " + taskGraphNode.getGraphNodeInformation().getId() + " ticking at nano time: " + System.nanoTime() + "    milli time: " + System.currentTimeMillis());
        synchronized (tickNotificationMonitor)
        {
            tickNotificationMonitor.notify();
            taskRunnerController.setStateAsWorking();
        }
        try
        {
            taskGraphNode.tick();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (CollectionsException e)
        {
            e.printStackTrace();
        } catch (NullDataReceivedFromGraphNodeAsInput nullDataReceivedFromGraphNodeAsInput)
        {
            nullDataReceivedFromGraphNodeAsInput.printStackTrace();
        } catch (ExpectedEdgeOnNodeInputButNotFound expectedEdgeOnNodeInputButNotFound)
        {
            expectedEdgeOnNodeInputButNotFound.printStackTrace();
        } catch (InfoSecCookerRuntimeException e)
        {
            e.printStackTrace();
        }
        synchronized (tickFinishedNotificationMonitor)
        {
            tickFinishedNotificationMonitor.notify();
        }
    }
}
