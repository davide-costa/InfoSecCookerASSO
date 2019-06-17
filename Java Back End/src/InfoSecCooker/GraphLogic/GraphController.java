package InfoSecCooker.GraphLogic;

import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.AttemptToCreateUnknownNode;
import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.GraphBuildingException;
import InfoSecCooker.GraphLogic.GraphBuilding.Exceptions.ReferenceToUnExistantNodeInTheGraph;
import InfoSecCooker.GraphLogic.GraphBuilding.MainGraphBuilder;
import InfoSecCooker.GraphLogic.GraphBuilding.TaskFactory;
import InfoSecCooker.GraphLogic.GraphRunning.GraphRunner;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphWireShark.IncomingAndOutgoingPacketRegistry;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskNodeRunning.TickEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphController
{
    enum GraphControllerState {BUILDING_GRAPH, RUNNING_GRAPH, GRAPH_PAUSED, GRAPH_STOPPED}


    RunTimeConfigurations runTimeConfigurations;
    MainGraphBuilder mainGraphBuilder;
    GraphRunner graphRunner;

    ArrayList<MainGraphBuilder> mainGraphBuilderHistory;
    ArrayList<GraphRunner> graphRunnerHistory;

    GraphControllerState state;

    TaskFactory taskFactory;

    long sessionId;

    public GraphController(long sessionId)
    {
        this.runTimeConfigurations = new RunTimeConfigurations();
        mainGraphBuilderHistory = new ArrayList<>();
        graphRunnerHistory = new ArrayList<>();
        state = GraphControllerState.BUILDING_GRAPH;
        this.sessionId = sessionId;
        this.taskFactory = new TaskFactory(runTimeConfigurations);
    }

    public void applyNewRunTimeConfiguration(RunTimeConfigurations runTimeConfigurations)
    {
        //this can't be a simple this.runTimeConfigurations = runTimeConfigurations because we want the instance to be changed internally, not just its pointer;
        //that way all nodes and edges that already have references to it, will see the reference updated
        this.runTimeConfigurations.copyContentsToThisInstance(runTimeConfigurations);
    }

    public Long getSessionId()
    {
        return sessionId;
    }

    public ArrayList<String> getTaskNamesList()
    {
        return taskFactory.getTaskNamesList();
    }

    public void startNewGraph()
    {
        if (mainGraphBuilder != null)
        {
            if (graphRunner != null)
            {
                if (graphRunner.getState() == GraphRunner.GraphState.RUNNING)
                    graphRunner.pause();

                graphRunnerHistory.add(graphRunner);
            }

            mainGraphBuilderHistory.add(mainGraphBuilder);
        }

        mainGraphBuilder = new MainGraphBuilder(taskFactory, runTimeConfigurations);

        state = GraphControllerState.BUILDING_GRAPH;
    }

    public long addNewTaskNode(String taskName) throws GraphBuildingException
    {
        return mainGraphBuilder.addNewTaskNode(taskName);
    }

    public long addNewSourceTaskNode(String taskName, String additionalInfo) throws AttemptToCreateUnknownNode
    {
        return mainGraphBuilder.addNewSourceTaskNode(taskName, additionalInfo);
    }

    public long addNewSinkTaskNode(String taskName, String additionalInfo) throws AttemptToCreateUnknownNode
    {
        return mainGraphBuilder.addNewSinkTaskNode(taskName, additionalInfo);
    }

    public long addEdgeFromNode1ToNode2OutputToInput(long node1Id, long node2Id,
                                                     int outputNumber, int inputNumber) throws GraphBuildingException
    {
        return mainGraphBuilder.addEdgeFromNode1ToNode2OutputToInput(node1Id, node2Id, outputNumber, inputNumber);
    }

    public void removeTaskNode(long taskNodeId) throws ReferenceToUnExistantNodeInTheGraph
    {
        mainGraphBuilder.removeTaskNode(taskNodeId);
    }

    public void removeEdge(long edgeId) throws GraphBuildingException
    {
        mainGraphBuilder.removeEdge(edgeId);
    }

    public void setTickIntervalOfNode(long nodeId, int tickInterval) throws ReferenceToUnExistantNodeInTheGraph
    {
        mainGraphBuilder.setTickIntervalOfNode(nodeId, tickInterval);
    }

    public void setNumberOfCyclesOfNode(long nodeId, int numberOfCycles) throws ReferenceToUnExistantNodeInTheGraph
    {
        mainGraphBuilder.setNumberOfCyclesOfNode(nodeId, numberOfCycles);
    }

    public void setBufferSizeOfNode(long taskNodeId, int bufferSize) throws ReferenceToUnExistantNodeInTheGraph
    {
        mainGraphBuilder.setBufferSizeOfNode(taskNodeId, bufferSize);
    }

    public boolean finishGraphBuildingAndLaunchGraph()
    {
        if (mainGraphBuilder.isGraphFullyBuiltAndWithoutErrors())
        {
            launchGraph();
            return true;
        }
        else
            return false;
    }

    //BRACE FOR IMPACT
    public boolean finishGraphBuildingAndLaunchGraphDespiteErrors()
    {
        if (!mainGraphBuilder.isGraphFullyBuiltAndWithoutErrors())
            runTimeConfigurations.discardPacketsIfPipesAreFull.set(true);

        launchGraph();

        return mainGraphBuilder.isGraphFullyBuiltAndWithoutErrors();
    }

    private boolean launchGraph()
    {
        if (state != GraphControllerState.BUILDING_GRAPH)
            return false;

        //indexes should be used to associate elements of these ArrayLists
        ArrayList<TaskGraphNode> nodes = mainGraphBuilder.getNodes();
        HashMap<Long, Integer> nodeTickIntervals = mainGraphBuilder.getNodeTickIntervals();
        HashMap<Long, Integer> nodeNumberOfCycles = mainGraphBuilder.getNodeNumberOfCycles();
        ArrayList<Integer> tickIntervals = new ArrayList<>();
        ArrayList<Integer> numbersOfCycles = new ArrayList<>();
        for(TaskGraphNode node : nodes)
        {
            Long nodeId = node.getGraphNodeInformation().getId();
            Integer tickInterval = nodeTickIntervals.get(nodeId);
            Integer numberOfCycles = nodeNumberOfCycles.get(nodeId);
            if (tickInterval == null)
                tickInterval = 0;

            tickIntervals.add(tickInterval);
            numbersOfCycles.add(numberOfCycles);
        }

        graphRunner = new GraphRunner(nodes, tickIntervals, sessionId, numbersOfCycles);
        graphRunner.start(runTimeConfigurations);

        state = GraphControllerState.RUNNING_GRAPH;
        return true;
    }

    public void goBackToGraphBuilding()
    {
        stopAndReset();
        state = GraphControllerState.BUILDING_GRAPH;
    }

    public boolean pauseGraph()
    {
        if (state != GraphControllerState.RUNNING_GRAPH)
            return false;

        graphRunner.pause();
        state = GraphControllerState.GRAPH_PAUSED;

        return true;
    }

    public boolean resumeGraph()
    {
        if (state != GraphControllerState.GRAPH_PAUSED)
            return false;

        graphRunner.resume();
        state = GraphControllerState.RUNNING_GRAPH;

        return true;
    }

    public boolean stopAndReset()
    {
        if (state != GraphControllerState.RUNNING_GRAPH && state != GraphControllerState.GRAPH_PAUSED)
            return false;

        graphRunner.stopAndReset();
        state = GraphControllerState.GRAPH_STOPPED;

        return true;
    }

    public List<TickEntry> getAndClearTickRegistry() throws InterruptedException
    {
        if (state == GraphControllerState.RUNNING_GRAPH || state == GraphControllerState.GRAPH_PAUSED)
            return graphRunner.getAndClearTickRegistry();

        return null;
    }

    public List<TickEntry> getTickRegistySinceEver() throws InterruptedException
    {
        if (state == GraphControllerState.RUNNING_GRAPH || state == GraphControllerState.GRAPH_PAUSED)
            return graphRunner.getTickRegistySinceEver();

        return null;
    }

    public HashMap<Long, String> getTaskNodesStates()
    {
        if (state == GraphControllerState.RUNNING_GRAPH || state == GraphControllerState.GRAPH_PAUSED)
            return graphRunner.getTaskNodesStates();

        return null;
    }

    public HashMap<Long, IncomingAndOutgoingPacketRegistry> getPacketCaptureRegistryByNode()
    {
        if (state == GraphControllerState.RUNNING_GRAPH || state == GraphControllerState.GRAPH_PAUSED)
            return graphRunner.getPacketCaptureRegistryByNode();

        return null;
    }

    public HashMap<Long, Long> getCurrentTickCountOfAllNodes() throws InterruptedException
    {
        if (state == GraphControllerState.RUNNING_GRAPH || state == GraphControllerState.GRAPH_PAUSED)
            return graphRunner.getCurrentTickCountOfAllNodes();

        return null;
    }
}
