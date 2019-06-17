package InfoSecCooker.GraphLogic.GraphRunning;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RunTimeConfigurations
{
    public AtomicInteger pipeBufSize = new AtomicInteger(10);
    public AtomicBoolean enablePacketCaptureOnPipeGraphEdges = new AtomicBoolean(false);
    public AtomicBoolean enablePacketCaptureOnTaskGraphNodes = new AtomicBoolean(true);
    public AtomicBoolean discardPacketsIfPipesAreFull = new AtomicBoolean();
    public AtomicBoolean tasksTickImmediatelyAfterLaunch = new AtomicBoolean();
    public AtomicBoolean tasksShouldSubtractExecutionTimeToSleepTime = new AtomicBoolean();
    public AtomicInteger numberOfThreadsForRunningTasks = new AtomicInteger();
    public AtomicInteger informationConnectionUpdateIntervalMilliSecs = new AtomicInteger(200);

    public void copyContentsToThisInstance(RunTimeConfigurations runTimeConfigurations)
    {
        enablePacketCaptureOnPipeGraphEdges.set(runTimeConfigurations.enablePacketCaptureOnPipeGraphEdges.get());
        enablePacketCaptureOnTaskGraphNodes.set(runTimeConfigurations.enablePacketCaptureOnTaskGraphNodes.get());
        discardPacketsIfPipesAreFull.set(runTimeConfigurations.discardPacketsIfPipesAreFull.get());
        tasksTickImmediatelyAfterLaunch.set(runTimeConfigurations.tasksTickImmediatelyAfterLaunch.get());
        tasksShouldSubtractExecutionTimeToSleepTime.set(runTimeConfigurations.tasksShouldSubtractExecutionTimeToSleepTime.get());
        numberOfThreadsForRunningTasks.set(runTimeConfigurations.numberOfThreadsForRunningTasks.get());
        informationConnectionUpdateIntervalMilliSecs.set(runTimeConfigurations.informationConnectionUpdateIntervalMilliSecs.get());
    }
}
