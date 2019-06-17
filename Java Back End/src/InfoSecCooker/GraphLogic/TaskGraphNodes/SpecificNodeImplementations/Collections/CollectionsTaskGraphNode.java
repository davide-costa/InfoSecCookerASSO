package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

public abstract class CollectionsTaskGraphNode extends BasicTaskGraphNode
{
    public CollectionsTaskGraphNode(GraphNodeInformation graphNodeInformation,
                                    RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }
}
