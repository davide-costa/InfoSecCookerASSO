package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.ToInt;

import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

public abstract class StringToConverterNodeTask extends BasicTaskGraphNode
{
    public StringToConverterNodeTask(GraphNodeInformation graphNodeInformation,
                                     RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }
}
