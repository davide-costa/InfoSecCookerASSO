package InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class implements the tick functionality time logic of a node.
 * It calls the tick method of the TaskGraphNode every tickInterval milliSeconds.
 * It implements the tick, cycle or heartbeat functionality for each node.
 * For example, if a node is supposed to generate data every 500 milliSeconds, then 500 should be passed to the constructor.
 * If a node is supposed to be always listening for data (interrupt based) and immediately process it after receiving, 0 milliSeconds should be passed to the constructor.
 */
public class TaskRunnerController extends TimerTask
{
    /**
     * The state is IDLING the default state, when the TaskRunnerController has just been created.
     * The task remains in state IDLING while the thread as not yet been started (i.e., the run method has not yet been executed).
     * When it is waiting for time to pass to to implement the cycles logic based on time and it is sleeping, the state is SLEEPING.
     * When it has called the function tick of the TaskGraphNode and it is waiting for the function to return, the state is WORKING. For more specific state information, the state on the TaskGraphNode should be checked.
     * When the method pause of this TaskRunnerController has been called, it goes to state PAUSED.
     * When the method stopAndReset of this TaskRunnerController has been called, it goes to state STOPPED.
     */
    public enum TaskRunnerState {IDLING, SLEEPING, WORKING, PAUSED, STOPPED}

    TaskGraphNode taskGraphNode;
    Object tickNotificationMonitor;
    Object tickFinishedNotificationMonitor;
    int tickInterval;
    boolean tickImmediatelyAfterLaunch;
    boolean subtractExecutionTimeToSleepTime;
    int numberOfCyclesToExecute;
    TaskRunnerState state;
    ScheduledExecutorService taskRunnerThreadExecutor;
    ScheduledFuture<?> taskRunnerFuture;
    long nextSleepInterval;
    long lastFallASleepTimeMillis;
    AtomicLong startTimeMillis;

    TaskRunnerThread taskRunnerThread;
    Long currentTickCount;

    public TaskRunnerController(TaskGraphNode taskGraphNode, Object tickNotificationMonitor, int tickInterval, RunTimeConfigurations runTimeConfigurations,
                                int numberOfCyclesToExecute)
    {
        this(taskGraphNode, tickNotificationMonitor, tickInterval, runTimeConfigurations.tasksTickImmediatelyAfterLaunch.get(), runTimeConfigurations.tasksShouldSubtractExecutionTimeToSleepTime.get(), numberOfCyclesToExecute);

    }

    public TaskRunnerController(TaskGraphNode taskGraphNode, Object tickNotificationMonitor, int tickInterval,
                                boolean tickImmediatelyAfterLaunch, boolean subtractExecutionTimeToSleepTime,
                                int numberOfCyclesToExecute)
    {
        this.taskGraphNode = taskGraphNode;
        this.tickNotificationMonitor = tickNotificationMonitor;
        tickFinishedNotificationMonitor = new Object();
        this.tickInterval = 1000;
        this.tickImmediatelyAfterLaunch = tickImmediatelyAfterLaunch;
        this.subtractExecutionTimeToSleepTime = subtractExecutionTimeToSleepTime;
        this.numberOfCyclesToExecute = numberOfCyclesToExecute;
        state = TaskRunnerState.IDLING;
        taskRunnerThreadExecutor = Executors.newScheduledThreadPool(1);
        nextSleepInterval = 1000;
        startTimeMillis = new AtomicLong();
        taskRunnerThread = new TaskRunnerThread(taskGraphNode, tickNotificationMonitor, tickFinishedNotificationMonitor, startTimeMillis);
        currentTickCount = new Long(0);
    }

    public TaskRunnerState getState()
    {
        return state;
    }

    public TaskGraphNode getTaskGraphNode()
    {
        return taskGraphNode;
    }

    /**
     * This method should be called in order for the TaskRunnerController to start performing its work.
     * It will start calling the tick method only after this method has been called.
     * If this method is accidentally called when already running it will have no effect.
     * It can only be called after the constructor call or a call to method stopAndReset.
     */
    public void start()
    {
        if (state != TaskRunnerState.IDLING && state != TaskRunnerState.STOPPED)
            return;

        if (!tickImmediatelyAfterLaunch)
        {
            sleepUntilNextCycle(tickInterval);
        }
        else
        {
            this.run();
        }
    }

    /**
     * This method allows to pause the execution of the TaskRunnerController.
     * If this method is accidentally called when already running it will have no effect.
     * For example if the tick interval is 500 ms and it has been a sleep for only 300 ms when this method is called, the next time it is resumed, it will sleep for 200 ms before beginning computation.
     * If this method is accidentally called when not yet started or already paused, it will have no effect.
     */
    public void pause()
    {
        if (state == TaskRunnerState.IDLING || state == TaskRunnerState.PAUSED)
            return;

        long currentTime = System.currentTimeMillis();
        nextSleepInterval = currentTime - lastFallASleepTimeMillis;

        cancelAndResetTimer();
        state = TaskRunnerState.PAUSED;
    }

    /**
     * This method resumes the execution of the TaskRunnerController. Should be called after a pause.
     * If this method is accidentally called when it is not paused, it will have no effect.
     */
    public void resume()
    {
        if (state != TaskRunnerState.PAUSED)
            return;

        sleepUntilNextCycle(nextSleepInterval);
        this.run();
    }

    /**
     * This method stops and resets the execution of the TaskRunnerController.
     * Stopping means that the remaining time (in case it is sleeping) to finish the sleeping will not be taken into account when the execution is resumed.
     * For example if the tick interval is 500 ms and it has been a sleep for only 300 ms when this method is called, the next time it is resumed, it will sleep for the whole 500 ms before beginning computation.
     * If this method is accidentally called when it is not yet started or already stopped, it will have no effect.
     */
    public void stopAndReset()
    {
        if (state == TaskRunnerState.STOPPED || state == TaskRunnerState.IDLING)
            return;

        cancelAndResetTimer();
        state = TaskRunnerState.STOPPED;
        nextSleepInterval = tickInterval;
    }

    public Long getCurrentTickCount()
    {
        return currentTickCount;
    }

    /**
     * This method implements the tick functionality time logic of a node.
     * It calls the tick method of the TaskGraphNode.
     * It implements the tick, cycle or heartbeat functionality for each node.
     * This method notifies (using the monitor passed as parameter) when a tick occurs.
     */
    @Override
    public void run()
    {
        if (numberOfCyclesToExecute != 0)
        {
            performCycle();

            for (int i = 1; i < numberOfCyclesToExecute; i++)
            {
                if (state == TaskRunnerState.WORKING || state == TaskRunnerState.SLEEPING)
                {
                    sleepUntilNextCycle(nextSleepInterval);
                }

                performCycle();
            }


        }
        else
        {
            while (true)
            {
                performCycle();

                if (state == TaskRunnerState.WORKING || state == TaskRunnerState.SLEEPING)
                {
                    sleepUntilNextCycle(nextSleepInterval);
                }
            }
        }
    }

    private void performCycle()
    {
        try
        {
            synchronized (tickFinishedNotificationMonitor)
            {
                tickFinishedNotificationMonitor.wait();
            }
            currentTickCount++;
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        long endTimeMillis;
        if (subtractExecutionTimeToSleepTime)
        {
            endTimeMillis = System.currentTimeMillis();
            long diff = endTimeMillis - startTimeMillis.get();
            nextSleepInterval = diff;
        }
    }

    private void sleepUntilNextCycle(long tickInterval)
    {
        state = TaskRunnerState.SLEEPING;
        lastFallASleepTimeMillis = System.currentTimeMillis();
        try
        {
            Thread.sleep(200);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        taskRunnerFuture = taskRunnerThreadExecutor.schedule(taskRunnerThread, tickInterval, TimeUnit.MILLISECONDS);
    }

    private void cancelAndResetTimer()
    {
        taskRunnerFuture.cancel(false);
    }
}
