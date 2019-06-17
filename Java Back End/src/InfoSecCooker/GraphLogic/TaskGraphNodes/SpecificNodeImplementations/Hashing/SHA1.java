package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Hashing;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphDataBufferedCollection;
import InfoSecCooker.GraphLogic.GraphData.StringGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections.SortNodeTask;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SHA1 extends HashingNodeTask
{
    public SHA1(GraphNodeInformation graphNodeInformation,
                RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException, InputParametersException
    {
        StringGraphData stringGraphData = (StringGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, StringGraphData.class, 0);
        String shaString = handleSHARequestComputation(stringGraphData.getString());

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new StringGraphData(shaString));
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
        String stringConcatenateResult = "";

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            StringGraphData stringGraphData = (StringGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), StringGraphData.class, 0);
            stringConcatenateResult += stringGraphData.getString();
        }

        return new StringGraphData(stringConcatenateResult);
    }

    private String handleSHARequestComputation(String dataString) throws
            ComputationException
    {
        String outputString;
        try
        {
            outputString = sha1(dataString);
        } catch (NoSuchAlgorithmException e)
        {
            throw new ComputationException("Error calculating SHA1 for \"" + dataString + "\"");
        }

        return outputString;
    }

    private String sha1(String input) throws NoSuchAlgorithmException
    {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++)
        {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }


    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", SHA1.class.getSimpleName())
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
