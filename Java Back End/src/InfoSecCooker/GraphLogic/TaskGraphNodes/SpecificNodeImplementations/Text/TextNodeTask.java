package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Text;

import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.BasicTaskGraphNode;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TextNodeTask extends BasicTaskGraphNode
{
    public TextNodeTask(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            InputParametersException
    {
        StringGraphData stringGraphData = (StringGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, StringGraphData.class, 0);
        String handleTextOperationRequestComputation = handleTextOperationRequestComputation(stringGraphData.getString());

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new StringGraphData(handleTextOperationRequestComputation));
        return resultArray;
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        switch (index)
        {
            case 1:
                return transformStringBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing PING class");
        }
    }

    private InfoSecGraphData transformStringBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        String urlConcat = "";

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            StringGraphData stringGraphData = (StringGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), StringGraphData.class, 0);

            urlConcat += stringGraphData.getString();
        }

        return new StringGraphData(urlConcat);
    }

    protected abstract String handleTextOperationRequestComputation(String string);
}
