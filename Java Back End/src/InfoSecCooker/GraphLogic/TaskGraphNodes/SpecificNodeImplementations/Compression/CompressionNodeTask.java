package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.HashMap;

public abstract class CompressionNodeTask extends BasicTaskGraphNode
{
    public CompressionNodeTask(GraphNodeInformation graphNodeInformation,
                               RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }
}
