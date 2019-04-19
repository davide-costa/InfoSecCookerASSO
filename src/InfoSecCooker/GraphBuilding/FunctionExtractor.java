package InfoSecCooker.GraphBuilding;

import InfoSecCooker.GraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphEdge.PipeGraphEdgeWithJavaArrayBlockingQueueImplementation;
import InfoSecCooker.GraphNodes.ComplexTaskGraphNode;
import InfoSecCooker.GraphNodes.ShortCircuitNodeTask;
import InfoSecCooker.GraphNodes.TaskGraphNode;

import java.util.ArrayList;

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
        ArrayList<TaskGraphNode> sources = new ArrayList<>();
        ArrayList<TaskGraphNode> sinks = new ArrayList<>();

        findSourceNodes(sources);
        findSinkNodes(sinks);

        //Get source edges
        ArrayList<PipeGraphEdge> sourceEdges = getSourceEdges(sources);

        //Get destination edges
        ArrayList<PipeGraphEdge> destinationEdges = getDestinationEdges(sinks);

        //Find entrance edges
        ArrayList<PipeGraphEdge> entranceEdges = getEntranceEdges(sources);

        //Find outgoing edges
        ArrayList<PipeGraphEdge> outgoingEdges = getDestinationEdges(sources);


        //Cut entrance edges and connect them through ShortCircuitNodeTask class instances
        ArrayList<ShortCircuitNodeTask> entranceShortCircuitNodes = getEntranceShortCircuitNodes(entranceEdges);

        //Cut outgoing edges and connect them through ShortCircuitNodeTask class instances
        ArrayList<ShortCircuitNodeTask> exitanceShortCircuitNodes = getExitanceShortCircuitNodes(outgoingEdges);

        return new ComplexTaskGraphNode(null, sourceEdges,
                destinationEdges, sources,
                sinks, nodes,
                entranceShortCircuitNodes, exitanceShortCircuitNodes);
    }

    private void findSinkNodes(ArrayList<TaskGraphNode> sources)
    {
        for (TaskGraphNode node : nodes)
        {
            ArrayList<PipeGraphEdge> destinationEdges = node.getDestinations();
            for (PipeGraphEdge sourceEdge : destinationEdges)
            {
                TaskGraphNode destinationNode = sourceEdge.getDestination();
                //TODO not sure if this works without an implementation of .equals on class TaskGraphNode
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(destinationNode))
                    sources.add(destinationNode);

            }
        }
    }

    private void findSourceNodes(ArrayList<TaskGraphNode> sources)
    {
        for (TaskGraphNode node : nodes)
        {
            ArrayList<PipeGraphEdge> sourceEdges = node.getSources();
            for (PipeGraphEdge sourceEdge : sourceEdges)
            {
                TaskGraphNode sourceNode = sourceEdge.getSource();
                //TODO not sure if this works without an implementation of .equals on class TaskGraphNode
                //this allows to check if the node is connected backwards to the outside of this ComplexTaskGraphNode
                //if yes, then it is a source node
                if (!nodes.contains(sourceNode))
                    sources.add(sourceNode);

            }
        }
    }

    private ArrayList<PipeGraphEdge> getSourceEdges(ArrayList<TaskGraphNode> sources)
    {
        ArrayList<PipeGraphEdge> sourceEdges = new ArrayList<>();
        for (TaskGraphNode node : sources)
        {
            sourceEdges.addAll(node.getSources());
        }
        return sourceEdges;
    }

    private ArrayList<PipeGraphEdge> getEntranceEdges(ArrayList<TaskGraphNode> sources)
    {
        ArrayList<PipeGraphEdge> entranceEdges = getSourceEdges(sources);
        return entranceEdges;
    }

    private ArrayList<PipeGraphEdge> getDestinationEdges(ArrayList<TaskGraphNode> sinks)
    {
        ArrayList<PipeGraphEdge> destinationEdges = new ArrayList<>();
        for (TaskGraphNode node : sinks)
        {
            destinationEdges.addAll(node.getDestinations());
        }
        return destinationEdges;
    }

    private ArrayList<ShortCircuitNodeTask> getEntranceShortCircuitNodes(ArrayList<PipeGraphEdge> entranceEdges)
    {
        ArrayList<ShortCircuitNodeTask> entranceShortCircuitNodes = new ArrayList<>();
        for (PipeGraphEdge entranceEdge : entranceEdges)
        {
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
            PipeGraphEdge downStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation();

            ArrayList<PipeGraphEdge> upStreamEdgeArray = new ArrayList<>();
            upStreamEdgeArray.add(upStreamEdge);
            ArrayList<PipeGraphEdge> downStreamEdgeArray = new ArrayList<>();
            upStreamEdgeArray.add(downStreamEdge);

            //TODO fix this aldrabation of null
            ShortCircuitNodeTask shortCircuitNode = new ShortCircuitNodeTask(null, upStreamEdgeArray, downStreamEdgeArray);

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

    private ArrayList<ShortCircuitNodeTask> getExitanceShortCircuitNodes(ArrayList<PipeGraphEdge> outgoingEdges)
    {
        ArrayList<ShortCircuitNodeTask> exitanceShortCircuitNodes = new ArrayList<>();
        for (PipeGraphEdge outgoingEdge : outgoingEdges)
        {
            //get the node that outputs data through this edge
            //this node belongs to the FunctionExtractor being built
            TaskGraphNode sourceNodeOfCurrentOutgoingEdge = outgoingEdge.getSource();

            ArrayList<PipeGraphEdge> outgoingEdgesForCurrentShortCircuitNode = new ArrayList<>();
            outgoingEdgesForCurrentShortCircuitNode.add(outgoingEdge);

            //this means the one to the left, where the info comes from in the edge being cut
            //a portuguese analogy would be "a montante do rio", or in this case "a montante da edge"
            PipeGraphEdge upStreamEdge = new PipeGraphEdgeWithJavaArrayBlockingQueueImplementation();

            //create new edge (because cutting doesn't actually work like literally cutting on code, we need to make it happen)
            //the new edge is the downstream edge
            //this means the one to the right, where the info goes to in the edge being cut
            //a portuguese analogy would be "a jusante do rio", or in this case "a jusante da edge"
            //let the downstream edge be the original one
            PipeGraphEdge downStreamEdge = outgoingEdge;

            ArrayList<PipeGraphEdge> upStreamEdgeArray = new ArrayList<>();
            upStreamEdgeArray.add(upStreamEdge);
            ArrayList<PipeGraphEdge> downStreamEdgeArray = new ArrayList<>();
            upStreamEdgeArray.add(downStreamEdge);

            //TODO fix this aldrabation of null
            ShortCircuitNodeTask shortCircuitNode = new ShortCircuitNodeTask(null, upStreamEdgeArray, downStreamEdgeArray);

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
