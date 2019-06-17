package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections;

import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortNodeTask extends CollectionsTaskGraphNode
{
    public SortNodeTask(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            InputParametersException
    {
        CollectionGraphData collectionData = (CollectionGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, CollectionGraphData.class, 0);
        List collection = collectionData.getCollection();
        Collections.sort(collection);

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new CollectionGraphData(collection));
        return resultArray;
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        switch (index)
        {
            case 1:
                return transformListsBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing Sort class");
        }
    }

    private InfoSecGraphData transformListsBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        List listConcatenateResult = new ArrayList();

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            CollectionGraphData collectionData = (CollectionGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), CollectionGraphData.class, 0);

            listConcatenateResult.addAll(collectionData.getCollection());
        }

        return new CollectionGraphData(listConcatenateResult);
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", SortNodeTask.class.getSimpleName())
                .put("numberInputs", getExpectedNumberInputs())
                .put("numberOutputs", getExpectedNumberOutputs())
                .put("nodeType", "Middle").toString();

        return jsonString;
    }

    protected static int getExpectedNumberInputs()
    {
        return 1;
    }

    protected static int getExpectedNumberOutputs()
    {
        return 1;
    }

    @Override
    public boolean areNumberOfInputsInACorrectState()
    {
        return getInputs().size() == getExpectedNumberInputs();
    }

    @Override
    public boolean areNumberOfOutputsInACorrectState()
    {
        return getOutputs().size() == getExpectedNumberOutputs();
    }
}
