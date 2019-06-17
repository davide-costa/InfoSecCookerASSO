package InfoSecCooker;

import InfoSecCooker.GraphNodes.GraphNodeRunner;
import InfoSecCooker.GraphNodes.GraphNodeRunnerTickWatcher;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class stores an ArrayList of allNodes, which create a graph.
 * It creates threads (GraphNodeRunner) which implement the logic of tick, cycles or heartbeats of the graph.
 * To better understand this graph, take this analogy: this graph can be seen as a processor and the multiple allNodes are its modules. The graph edges can be seen as the electric wires (tracks). Just as the electricity flows through the tracks and enters the modules, leaving them after a certain processing time, here the data flows through the edges entering the allNodes and leaving them after a certain processing time. Each complete flow of data through the graph can be seen as a processor cycle.
 */
public class InfoSecCooker implements Observer
{
    ArrayList<GraphNodeRunner> allNodes;
    ArrayList<GraphNodeRunner> sourceNodes;
    ArrayList<GraphNodeRunner> intermediateNodes;
    ArrayList<GraphNodeRunner> sinkNodes;
    int numberOfThreads;

    public InfoSecCooker(ArrayList<GraphNodeRunner> nodes, int numberOfThreads)
    {
        this.allNodes = nodes;
    }

    public void separateNodesIntoSourceIntermediateAndSink()
    {
        //TODO
    }

    public void start()
    {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        ArrayList<Object> monitors = new ArrayList<>();
        ArrayList<GraphNodeRunnerTickWatcher> watchers = new ArrayList<>();

        for(GraphNodeRunner graphNodeRunner : allNodes)
        {
            monitors.add(new Object());
            executor.execute(graphNodeRunner);
            GraphNodeRunnerTickWatcher watcher = new GraphNodeRunnerTickWatcher(monitors, graphNodeRunner);
            watchers.add(watcher);
            watcher.addObserver(this);
        }
    }

    @Override
    public void update(Observable o, Object arg)
    {
        System.out.println("Graph node " + ((GraphNodeRunnerTickWatcher)arg).getGraphNodeRunner().getTaskGraphNode().getGraphNodeInformation() + " has ticked"); //#CodeSmell

    }
}
