package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphDataBufferedCollection;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.HashMap;

public abstract class SourcesNodeTask extends BasicTaskGraphNode
{
    public SourcesNodeTask(GraphNodeInformation graphNodeInformation,
                           RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        return null;
    }
}
