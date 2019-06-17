package InfoSecCooker.GraphLogic.GraphRunning;

import InfoSecCooker.GraphLogic.GraphWireShark.IncomingAndOutgoingPacketRegistry;
import InfoSecCooker.GraphLogic.GraphWireShark.PacketRegistry;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.GraphNodeRunnerTickWatcher;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TaskRunnerController;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TaskRunningRegistry;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TickEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphRunner
{
    public enum GraphState {IDLING, RUNNING, PAUSED, STOPPED}

    //All this 3 array lists should be associated by index
    ArrayList<TaskGraphNode> taskGraphNodes;
    ArrayList<Integer> taskTickIntervals;
    ArrayList<Integer> taskNumberOfCycles;
    ArrayList<GraphNodeRunnerTickWatcher> tickWatchers;

    ArrayList<TaskRunnerController> runners;
    GraphState state;
    ExecutorService runnersThreadPool;
    ExecutorService tickWatchersThreadPool;
    TaskRunningRegistry taskRunningRegistry;
    TaskRunningRegistry taskRunningRegistrySinceEver;

    long sessionId;

    public GraphRunner(ArrayList<TaskGraphNode> taskGraphNodes,
                       ArrayList<Integer> taskTickIntervals,
                       long sessionId,
                       ArrayList<Integer> taskNumberOfCycles)
    {
        this.taskGraphNodes = taskGraphNodes;
        this.taskTickIntervals = taskTickIntervals;
        state = GraphState.IDLING;
        this.sessionId = sessionId;
        this.taskNumberOfCycles = taskNumberOfCycles;
        taskRunningRegistry = new TaskRunningRegistry();
        taskRunningRegistrySinceEver = new TaskRunningRegistry();
        runnersThreadPool = Executors.newFixedThreadPool(20);
        tickWatchersThreadPool = Executors.newFixedThreadPool(20);
    }

    public GraphState getState()
    {
        return state;
    }

    public void start(RunTimeConfigurations runTimeConfigurations)
    {
        if (state != GraphState.IDLING)
            return;

        tickWatchers = new ArrayList<>();
        runners = new ArrayList<>();
        createTaskRunnersAndTickWatchers(runTimeConfigurations);

        launchTaskRunners();
        state = GraphState.RUNNING;
    }

    public void pause()
    {
        if (state != GraphState.RUNNING)
            return;

        for (TaskRunnerController runner : runners)
        {
            runner.pause();
        }

        state = GraphState.PAUSED;
    }

    public void resume()
    {
        if (state != GraphState.PAUSED)
            return;

        for (TaskRunnerController runner : runners)
        {
            runner.resume();
        }

        state = GraphState.RUNNING;
    }

    public void stopAndReset()
    {
        if (state != GraphState.RUNNING)
            return;

        for (TaskRunnerController runner : runners)
        {
            runner.stopAndReset();
        }

        state = GraphState.STOPPED;
    }

    private void createTaskRunnersAndTickWatchers(RunTimeConfigurations runTimeConfigurations)
    {
        for (int i = 0; i < taskGraphNodes.size(); i++)
        {
            TaskGraphNode taskGraphNode = taskGraphNodes.get(i);
            Object tickNotificationMonitor = new Object();
            Integer tickInterval = taskTickIntervals.get(i);
            Integer numberOfCycles = taskNumberOfCycles.get(i);

            TaskRunnerController taskRunnerController = new TaskRunnerController(taskGraphNode, tickNotificationMonitor, tickInterval, runTimeConfigurations, numberOfCycles);
            runners.add(taskRunnerController);
            runnersThreadPool.execute(taskRunnerController);

            GraphNodeRunnerTickWatcher tickWatcher = new GraphNodeRunnerTickWatcher(tickNotificationMonitor, taskRunnerController, taskRunningRegistry, taskRunningRegistrySinceEver);
            tickWatchersThreadPool.execute(tickWatcher);
            tickWatchers.add(tickWatcher);
        }
    }

    private void launchTaskRunners()
    {
        for (TaskRunnerController runner : runners)
        {
            runner.start();
        }
    }

    public List<TickEntry> getAndClearTickRegistry() throws InterruptedException
    {
        return taskRunningRegistry.getAndClearRegistry();
    }

    public List<TickEntry> getTickRegistySinceEver() throws InterruptedException
    {
        return taskRunningRegistrySinceEver.getRegistry();
    }

    public HashMap<Long, String> getTaskNodesStates()
    {
        HashMap<Long, String> nodeStates = new HashMap<>();
        for (TaskRunnerController runner : runners)
        {
            TaskRunnerController.TaskRunnerState state = runner.getState();
            TaskGraphNode node = runner.getTaskGraphNode();
            String stateStr;
            if (state.equals(TaskRunnerController.TaskRunnerState.IDLING))
                stateStr = "IDLING";
            else if(state.equals(TaskRunnerController.TaskRunnerState.SLEEPING))
                stateStr = "SLEEPING";
            else if(state.equals(TaskRunnerController.TaskRunnerState.PAUSED))
                stateStr = "PAUSED";
            else if(state.equals(TaskRunnerController.TaskRunnerState.STOPPED))
                stateStr = "STOPPED";
            else if(state.equals(TaskRunnerController.TaskRunnerState.WORKING))
            {
                TaskGraphNode.TaskGraphNodeState nodeState = node.getState();
                if (nodeState.equals(TaskGraphNode.TaskGraphNodeState.IDLING))
                    stateStr = "IDLING";
                else if(nodeState.equals(TaskGraphNode.TaskGraphNodeState.WAITING_FOR_INPUTS))
                    stateStr = "WAITING_FOR_INPUTS";
                else if(nodeState.equals(TaskGraphNode.TaskGraphNodeState.COMPUTING))
                    stateStr = "COMPUTING";
                else if(nodeState.equals(TaskGraphNode.TaskGraphNodeState.OUTPUTING))
                    stateStr = "OUTPUTING";
                else
                    stateStr = "UNKNOWN";

            }
            else
                stateStr = "UNKNOWN";

            nodeStates.put(node.getGraphNodeInformation().getId(), stateStr);
        }

        return nodeStates;
    }

    public HashMap<Long, IncomingAndOutgoingPacketRegistry> getPacketCaptureRegistryByNode()
    {
        HashMap<Long, IncomingAndOutgoingPacketRegistry> packetDump = new HashMap<>();
        for (TaskGraphNode taskGraphNode : taskGraphNodes)
        {
            Long id = taskGraphNode.getGraphNodeInformation().getId();
            PacketRegistry incomingPacketRegistry = taskGraphNode.getIncomingPacketRegistry();
            PacketRegistry outgoingPacketRegistry = taskGraphNode.getOutgoingPacketRegistry();
            IncomingAndOutgoingPacketRegistry incomingAndOutgoingPacketRegistry = new IncomingAndOutgoingPacketRegistry(incomingPacketRegistry, outgoingPacketRegistry);
            packetDump.put(id, incomingAndOutgoingPacketRegistry);
        }

        return packetDump;
    }

    public HashMap<Long, Long> getCurrentTickCountOfAllNodes() throws InterruptedException
    {
        HashMap<Long, Long> tickCounts = new HashMap<>();
        for (TaskRunnerController taskRunnerController : runners)
        {
            Long id = taskRunnerController.getTaskGraphNode().getGraphNodeInformation().getId();
            Long tickCount = taskRunnerController.getCurrentTickCount();
            tickCounts.put(id, tickCount);
        }

        return tickCounts;
    }
}
