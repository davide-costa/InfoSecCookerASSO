package InfoSecCooker.GraphLogic.GraphBuilding;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdgeWithJavaArrayBlockingQueueImplementation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.ComplexTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.Functions.SwitchNodeTask;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.TaskGraphNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements necessary logic to perform "extract function" from the graph already built.
 * It is used when the user asks the InfoSecCooker to extract a function from a set of nodes.
 */
public class FunctionExtractor
{
    ArrayList<TaskGraphNode> nodes;

    public FunctionExtractor(ArrayList<TaskGraphNode> nodes)
    {
        this.nodes = nodes;
    }

    public ComplexTaskGraphNode getComplexGraphNode(RunTimeConfigurations runTimeConfigurations)
    {
        ArrayList<TaskGraphNode> sources = findSourceNodes();
        ArrayList<TaskGraphNode> sinks = findSinkNodes();

        //Get source edges
        HashMap<Integer, PipeGraphEdge> sourceEdges = getSourceEdges(sources);

        //Get destination edges
        HashMap<Integer, PipeGraphEdge> destinationEdges = getDestinationEdges(sinks);

        //Find entrance edges
        HashMap<Integer, PipeGraphEdge> entranceEdges = getEntranceEdges(sources);

        //Find outgoing edges
        HashMap<Integer, PipeGraphEdge> outgoingEdges = getDestinationEdges(sources);


        //Cut entrance edges and connect them through SwitchNodeTask class instances
        ArrayList<SwitchNodeTask> entranceShortCircuitNodes = getEntranceShortCircuitNodes(entranceEdges, runTimeConfigurations);

        //Cut outgoing edges and connect them through SwitchNodeTask class instances
        ArrayList<SwitchNodeTask> exitanceShortCircuitNodes = getExitanceShortCircuitNodes(outgoingEdges, runTimeConfigurations);

        return new ComplexTaskGraphNode(null, sourceEdges,
                destinationEdges, sources,
                sinks, nodes,
                entranceShortCircuitNodes, exitanceShortCircuitNodes, runTimeConfigurations);
    }

    private ArrayList<TaskGraphNode> findSinkNodes()
    {
        ArrayList<TaskGraphNode> sinks = new ArrayList<>();
        for (TaskGraphNode node : nodes)
        {
            HashMap<Integer, PipeGraphEdge> destinationEdges = node.getOutputs();
            int destinationEdgesSize = destinationEdges.size();
            for (int i = 0; i < destinationEdgesSize; i++)
            {
                PipeGraphEdge destinationEdge = destinationEdges.get(i);
                TaskGraphNode destinationNode = destinationEdge.getDestination();
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(destinationNode))
                    if (!sinks.contains(node)) //add the node to the sinks if it is not already there
                        sinks.add(node);

            }
        }

        return sinks;
    }

    private ArrayList<TaskGraphNode> findSourceNodes()
    {
        ArrayList<TaskGraphNode> sources = new ArrayList<>();
        for (TaskGraphNode node : nodes)
        {
            HashMap<Integer, PipeGraphEdge> sourceEdges = node.getInputs();
            int sourceEdgesSize = sourceEdges.size();
            for (int i = 0; i < sourceEdgesSize; i++)
            {
                PipeGraphEdge sourceEdge = sourceEdges.get(i);
                TaskGraphNode sourceNode = sourceEdge.getSource();
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(sourceNode))
                    if (!sources.contains(node)) //add the node to the sources if it is not already there
                        sources.add(node);

            }
        }

        return sources;
    }

    private HashMap<Integer, PipeGraphEdge> convertArrayListOfPipeGraphEdgeToHashMap(ArrayList<PipeGraphEdge> pipeGraphEdges)
    {
        HashMap<Integer, PipeGraphEdge> pipeGraphEdgeHashMap = new HashMap<>();
        int i = 0;
        for (PipeGraphEdge pipeGraphEdge : pipeGraphEdges)
        {
            pipeGraphEdgeHashMap.put(i, pipeGraphEdge);
            i++;
        }

        return pipeGraphEdgeHashMap;
    }

    private HashMap<Integer, PipeGraphEdge> getSourceEdges(ArrayList<TaskGraphNode> sourceNodes)
    {
        ArrayList<PipeGraphEdge> sourceEdges = new ArrayList<>();
        int sourcesNodesSize = sourceNodes.size();
        for (int i = 0; i < sourcesNodesSize; i++)
        {
            TaskGraphNode node = sourceNodes.get(i);
            HashMap<Integer, PipeGraphEdge> sourcesOfNode = node.getInputs();
            int sourcesSize = sourcesOfNode.size();
            for (int j = 0; j < sourcesSize; j++)
            {
                sourceEdges.add(sourcesOfNode.get(j));
            }
        }
        return convertArrayListOfPipeGraphEdgeToHashMap(sourceEdges);
    }

    private HashMap<Integer, PipeGraphEdge> getEntranceEdges(ArrayList<TaskGraphNode> sources)
    {
        HashMap<Integer, PipeGraphEdge> entranceEdges = getSourceEdges(sources);
        return entranceEdges;
    }

    private HashMap<Integer, PipeGraphEdge> getDestinationEdges(ArrayList<TaskGraphNode> sinks)
    {
        ArrayList<PipeGraphEdge> destinationEdges = new ArrayList<>();
        int sinksSize = sinks.size();
        for (int i = 0; i < sinksSize; i++)
        {
            TaskGraphNode node = sinks.get(i);
            HashMap<Integer, PipeGraphEdge> destinationsOfNode = node.getOutputs();
            int destinationsSize = destinationsOfNode.size();
            for (int j = 0; j < destinationsSize; j++)
            {
                destinationEdges.add(destinationsOfNode.get(j));
            }
        }
        return convertArrayListOfPipeGraphEdgeToHashMap(destinationEdges);
    }

    private ArrayList<SwitchNodeTask> getEntranceShortCircuitNodes(HashMap<Integer, PipeGraphEdge> entranceEdges, RunTimeConfigurations runTimeConfigurations)
    {
        ArrayList<SwitchNodeTask> entranceShortCircuitNodes = new ArrayList<>();
        int entrancesEdgesSize = entranceEdges.size();
        for (int i = 0; i < entrancesEdgesSize; i++)
        {
            PipeGraphEdge entranceEdge = entranceEdges.get(i);

            //get the node that is fed data through this edge
            //this node belongs to the FunctionExtractor being built
            TaskGraphNode destinationNodeOfCurrentEntranceEdge = entranceEdge.getDestination();
            //Get the port of the node to which the edge is connected
            int destinationPortOfNodeOfCurrentEntranceEdge = entranceEdge.getDestinationPort();

            //this means the one to the left, where the info comes from in the edge being cut
            //a portuguese analogy would be "a montante do rio", or in this case "a montante da edge"
            TaskGraphNode upStreamNode = entranceEdge.getSource();

            //this means the one to the right, where the info goes to in the edge being cut
            //a portuguese analogy would be "a jusante do rio", or in this case "a jusante da edge"
            TaskGraphNode downStreamNode = entranceEdge.getDestination();

            ArrayList<PipeGraphEdge> entranceEdgesForCurrentShortCircuitNode = new ArrayList<>();
            entranceEdgesForCurrentShortCircuitNode.add(entranceEdge);

            //this means the one to the left, where the info comes from in the edge being cut
            //a portuguese analogy would be "a montante do rio", or in this case "a montante da edge"
            //let the upstream edge be the original one
            PipeGraphEdge upStreamEdge = entranceEdge;

            //create new edge (because cutting doesn't actually work like literally cutting on code, we need to make it happen)
            //the new edge is the downstream edge
            //this means the one to the right, where the info goes to in the edge being cut
            //a portuguese analogy would be "a jusante do rio", or in this case "a jusante da edge"
            PipeGraphEdge downStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(runTimeConfigurations);

            HashMap<Integer, PipeGraphEdge> upStreamEdgeHashMap = new HashMap<>();
            upStreamEdgeHashMap.put(0, upStreamEdge);
            HashMap<Integer, PipeGraphEdge> downStreamEdgeHashMap = new HashMap<>();
            downStreamEdgeHashMap.put(0, downStreamEdge);

            GraphNodeInformation graphNodeInformation = new GraphNodeInformation("SwitchNode", "SwitchNode");
            SwitchNodeTask shortCircuitNode = new SwitchNodeTask(graphNodeInformation,
                    upStreamEdgeHashMap, downStreamEdgeHashMap, runTimeConfigurations);

            //Reconnect graph edges appropriately
            //lets reconnect them from upstream to downstream (left to right)
            //The (upstream edge)'s upstream end (left end) is already properly connected (is connected to a node outside the ComplexTaskGraphNode being created)

            //First, connect the (upstream edge)' downstream end (right end) to the SwitchNodeTask class instance
            upStreamEdge.setDestination(shortCircuitNode);
            upStreamEdge.setDestinationPort(0);

            //Then, connect the (downstream edge)'s upstream end (left end) to the SwitchNodeTask class instance
            downStreamEdge.setSource(shortCircuitNode);
            downStreamEdge.setSourcePort(0);

            //Finally, connect the (downstream edge)'s downstream end (right end) to the node already inside the ComplexTaskGraphNode being created
            downStreamEdge.setDestination(destinationNodeOfCurrentEntranceEdge);
            downStreamEdge.setDestinationPort(destinationPortOfNodeOfCurrentEntranceEdge);

        }
        return entranceShortCircuitNodes;
    }

    private ArrayList<SwitchNodeTask> getExitanceShortCircuitNodes(HashMap<Integer, PipeGraphEdge> outgoingEdges, RunTimeConfigurations runTimeConfigurations)
    {
        ArrayList<SwitchNodeTask> exitanceShortCircuitNodes = new ArrayList<>();

        int outgoingEdgesSize = outgoingEdges.size();
        for (int i = 0; i < outgoingEdgesSize; i++)
        {
            PipeGraphEdge outgoingEdge = outgoingEdges.get(i);

            //get the node that outputs data through this edge
            //this node belongs to the FunctionExtractor being built
            TaskGraphNode sourceNodeOfCurrentOutgoingEdge = outgoingEdge.getSource();
            int sourcePortOfNodeOfCurrentOutgoingEdge = outgoingEdge.getSourcePort();

            ArrayList<PipeGraphEdge> outgoingEdgesForCurrentShortCircuitNode = new ArrayList<>();
            outgoingEdgesForCurrentShortCircuitNode.add(outgoingEdge);

            //this means the one to the left, where the info comes from in the edge being cut
            //a portuguese analogy would be "a montante do rio", or in this case "a montante da edge"
            PipeGraphEdge upStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation(runTimeConfigurations);

            //create new edge (because cutting doesn't actually work like literally cutting on code, we need to make it happen)
            //the new edge is the downstream edge
            //this means the one to the right, where the info goes to in the edge being cut
            //a portuguese analogy would be "a jusante do rio", or in this case "a jusante da edge"
            //let the downstream edge be the original one
            PipeGraphEdge downStreamEdge = outgoingEdge;

            HashMap<Integer, PipeGraphEdge> upStreamEdgeHashMap = new HashMap<>();
            upStreamEdgeHashMap.put(0, upStreamEdge);
            HashMap<Integer, PipeGraphEdge> downStreamEdgeHashMap = new HashMap<>();
            downStreamEdgeHashMap.put(0, downStreamEdge);

            GraphNodeInformation graphNodeInformation = new GraphNodeInformation("SwitchNode", "SwitchNode");
            SwitchNodeTask shortCircuitNode = new SwitchNodeTask(graphNodeInformation, upStreamEdgeHashMap,
                    downStreamEdgeHashMap, runTimeConfigurations);

            //Reconnect graph edges appropriately
            //lets reconnect them from upstream to downstream (left to right)

            //First, connect the (upstream edge)'s upstream end (left end) to the SwitchNodeTask class instance
            upStreamEdge.setSource(sourceNodeOfCurrentOutgoingEdge);
            upStreamEdge.setSourcePort(sourcePortOfNodeOfCurrentOutgoingEdge);

            //Finally, connect the (upstream edge)'s downstream end (right end) to the node already inside the ComplexTaskGraphNode being created
            upStreamEdge.setDestination(shortCircuitNode);
            upStreamEdge.setDestinationPort(0);

            //Then, connect the (downstream edge)'s upstream end (left end) to the SwitchNodeTask class instance
            downStreamEdge.setSource(shortCircuitNode);
            downStreamEdge.setSourcePort(0);

            //The the (downstream edge)'s downstream end (right end) is already properly connected (is connected to a node outside the ComplexTaskGraphNode being created)
        }
        return exitanceShortCircuitNodes;
    }

}
