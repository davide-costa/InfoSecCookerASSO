package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking;

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

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DNSLookup extends NetworkNodeTask
{
    public DNSLookup(GraphNodeInformation graphNodeInformation,
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
        String dnsLookupResult = handleDNSLookupRequestComputation(stringGraphData.getString());

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new StringGraphData(dnsLookupResult));
        return resultArray;
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        switch (index)
        {
            case 1:
                return transformStringUrlBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing PING class");
        }
    }

    private InfoSecGraphData transformStringUrlBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
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

    private String handleDNSLookupRequestComputation(String hostName) throws
            ComputationException
    {
        try
        {
            return getHostAddress(hostName);
        } catch (IOException e)
        {
            throw new ComputationException("Error getting host ip address for \"" + hostName + "\" address");
        }
    }

    private String getHostAddress(String hostName) throws UnknownHostException
    {
        InetAddress host = InetAddress.getByName(hostName);
        return host.getHostAddress();
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", DNSLookup.class.getSimpleName())
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
