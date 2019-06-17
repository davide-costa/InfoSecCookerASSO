package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphEdge.PipeGraphEdgeWithJavaArrayBlockingQueueImplementation;
import InfoSecCooker.GraphNodes.FlowControl.ComplexTaskGraphNode;
import InfoSecCooker.GraphNodes.FlowControl.ShortCircuitNodeTask;
import InfoSecCooker.GraphNodes.TaskGraphNode;

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

    //TODO the aux methods of this method need heavy code reading and debugging because it is difficult up logic
    public ComplexTaskGraphNode getComplexGraphNode()
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


        //Cut entrance edges and connect them through ShortCircuitNodeTask class instances
        ArrayList<ShortCircuitNodeTask> entranceShortCircuitNodes = getEntranceShortCircuitNodes(entranceEdges);

        //Cut outgoing edges and connect them through ShortCircuitNodeTask class instances
        ArrayList<ShortCircuitNodeTask> exitanceShortCircuitNodes = getExitanceShortCircuitNodes(outgoingEdges);

        return new ComplexTaskGraphNode(null, sourceEdges,
                destinationEdges, sources,
                sinks, nodes,
                entranceShortCircuitNodes, exitanceShortCircuitNodes);
    }

    private ArrayList<TaskGraphNode> findSinkNodes()
    {
        ArrayList<TaskGraphNode> sinks = new ArrayList<>();
        for (TaskGraphNode node : nodes)
        {
            HashMap<Integer, PipeGraphEdge> destinationEdges = node.getDestinations();
            int destinationEdgesSize = destinationEdges.size();
            for (int i = 0; i < destinationEdgesSize; i++)
            {
                PipeGraphEdge destinationEdge = destinationEdges.get(i);
                TaskGraphNode destinationNode = destinationEdge.getDestination();
                //TODO not sure if this works without an implementation of .equals on class TaskGraphNode
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(destinationNode))
                    sinks.add(destinationNode);

            }
        }

        return sinks;
    }

    private ArrayList<TaskGraphNode> findSourceNodes()
    {
        ArrayList<TaskGraphNode> sources = new ArrayList<>();
        for (TaskGraphNode node : nodes)
        {
            HashMap<Integer, PipeGraphEdge> sourceEdges = node.getSources();
            int sourceEdgesSize = sourceEdges.size();
            for (int i = 0; i < sourceEdgesSize; i++)
            {
                PipeGraphEdge sourceEdge = sourceEdges.get(i);
                TaskGraphNode sourceNode = sourceEdge.getSource();
                //TODO not sure if this works without an implementation of .equals on class TaskGraphNode
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(sourceNode))
                    sources.add(sourceNode);

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
            HashMap<Integer, PipeGraphEdge> sourcesOfNode = node.getSources();
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
            HashMap<Integer, PipeGraphEdge> destinationsOfNode = node.getDestinations();
            int destinationsSize = destinationsOfNode.size();
            for (int j = 0; j < destinationsSize; j++)
            {
                destinationEdges.add(destinationsOfNode.get(j));
            }
        }
        return convertArrayListOfPipeGraphEdgeToHashMap(destinationEdges);
    }

    private ArrayList<ShortCircuitNodeTask> getEntranceShortCircuitNodes(HashMap<Integer, PipeGraphEdge> entranceEdges)
    {
        ArrayList<ShortCircuitNodeTask> entranceShortCircuitNodes = new ArrayList<>();
        int entrancesEdgesSize = entranceEdges.size();
        for (int i = 0; i < entrancesEdgesSize; i++)
        {
            PipeGraphEdge entranceEdge = entranceEdges.get(i);

            //get the node that is fed data through this edge
            //this node belongs to the FunctionExtractor being built
            TaskGraphNode destinationNodeOfCurrentEntranceEdge = entranceEdge.getDestination();

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
            //TODO fill ports
            PipeGraphEdge downStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation();

            HashMap<Integer, PipeGraphEdge> upStreamEdgeHashMap = new HashMap<>();
            upStreamEdgeHashMap.put(0, upStreamEdge);
            HashMap<Integer, PipeGraphEdge> downStreamEdgeHashMap = new HashMap<>();
            downStreamEdgeHashMap.put(0, downStreamEdge);

            //TODO fix this aldrabation of null
            ShortCircuitNodeTask shortCircuitNode = new ShortCircuitNodeTask(null, upStreamEdgeHashMap, downStreamEdgeHashMap);

            //Reconnect graph edges appropriately
            //lets reconnect them from upstream to downstream (left to right)
            //The (upstream edge)'s upstream end (left end) is already properly connected (is connected to a node outside the ComplexTaskGraphNode being created)

            //First, connect the (upstream edge)' downstream end (right end) to the ShortCircuitNodeTask class instance
            upStreamEdge.setDestination(shortCircuitNode);

            //Then, connect the (downstream edge)'s upstream end (left end) to the ShortCircuitNodeTask class instance
            downStreamEdge.setSource(shortCircuitNode);

            //Finally, connect the (downstream edge)'s downstream end (right end) to the node already inside the ComplexTaskGraphNode being created
            downStreamEdge.setDestination(destinationNodeOfCurrentEntranceEdge);
        }
        return entranceShortCircuitNodes;
    }

    private ArrayList<ShortCircuitNodeTask> getExitanceShortCircuitNodes(HashMap<Integer, PipeGraphEdge> outgoingEdges)
    {
        ArrayList<ShortCircuitNodeTask> exitanceShortCircuitNodes = new ArrayList<>();

        int outgoingEdgesSize = outgoingEdges.size();
        for (int i = 0; i < outgoingEdgesSize; i++)
        {
            PipeGraphEdge outgoingEdge = outgoingEdges.get(i);

            //get the node that outputs data through this edge
            //this node belongs to the FunctionExtractor being built
            TaskGraphNode sourceNodeOfCurrentOutgoingEdge = outgoingEdge.getSource();

            ArrayList<PipeGraphEdge> outgoingEdgesForCurrentShortCircuitNode = new ArrayList<>();
            outgoingEdgesForCurrentShortCircuitNode.add(outgoingEdge);

            //this means the one to the left, where the info comes from in the edge being cut
            //a portuguese analogy would be "a montante do rio", or in this case "a montante da edge"
            //TODO fill ports
            PipeGraphEdge upStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation();

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

            //TODO fix this aldrabation of null
            ShortCircuitNodeTask shortCircuitNode = new ShortCircuitNodeTask(null, upStreamEdgeHashMap, downStreamEdgeHashMap);

            //Reconnect graph edges appropriately
            //lets reconnect them from upstream to downstream (left to right)

            //First, connect the (upstream edge)'s upstream end (left end) to the ShortCircuitNodeTask class instance
            upStreamEdge.setSource(sourceNodeOfCurrentOutgoingEdge);

            //Finally, connect the (upstream edge)'s downstream end (right end) to the node already inside the ComplexTaskGraphNode being created
            downStreamEdge.setDestination(shortCircuitNode);

            //Then, connect the (downstream edge)'s upstream end (left end) to the ShortCircuitNodeTask class instance
            downStreamEdge.setSource(shortCircuitNode);

            //The the (downstream edge)'s downstream end (right end) is already properly connected (is connected to a node outside the ComplexTaskGraphNode being created)
        }
        return exitanceShortCircuitNodes;
    }

}
