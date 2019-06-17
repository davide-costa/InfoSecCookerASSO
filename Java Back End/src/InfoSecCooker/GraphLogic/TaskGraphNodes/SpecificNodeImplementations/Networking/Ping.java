package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Networking;

import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections.SortNodeTask;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class Ping extends NetworkNodeTask
{
    public Ping(GraphNodeInformation graphNodeInformation,
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
        IntGraphData intGraphData = (IntGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, IntGraphData.class, 1);

        boolean result = handlePingRequestComputation(stringGraphData.getString(), intGraphData.getInt());

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new BooleanGraphData(result));
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
            case 2:
                return transformIntTimeoutBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
             default:
                 throw new ParsingInputsException("Unexpected parameter on parsing PING class");
        }
    }

    private InfoSecGraphData transformIntTimeoutBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        int timeoutsSum = 0;

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            IntGraphData intGraphData = (IntGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), IntGraphData.class, 0);

            timeoutsSum += intGraphData.getInt();
        }

        int timeoutAverage = timeoutsSum / bufferedInputs.size();
        return new IntGraphData(timeoutAverage);
    }

    private InfoSecGraphData transformStringUrlBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
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

    private boolean handlePingRequestComputation(String hostName, int timeout) throws
            ComputationException
    {
        try
        {
            return pingHost(hostName, timeout);
        } catch (IOException e)
        {
            throw new ComputationException("Error trying to ping \"" + hostName + "\" address");
        }
    }


    private boolean pingHost(String hostName, int timeout) throws IOException
    {
        InetAddress host = InetAddress.getByName(hostName);
        return host.isReachable(timeout);
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", Ping.class.getSimpleName())
                .put("numberInputs", getExpectedNumberInputs())
                .put("numberOutputs", getExpectedNumberOutputs())
                .put("nodeType", "Middle").toString();

        return jsonString;
    }

    protected static int getExpectedNumberInputs()
    {
        return 2;
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
