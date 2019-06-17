package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.StringGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FetchURL extends SourcesNodeTask
{
    private String url;

    public FetchURL(GraphNodeInformation graphNodeInformation,
                    RunTimeConfigurations runTimeConfigurations,
                    String url)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.url = url;
    }

    @Override
    public ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException
    {
        String urlDataString;
        try
        {
            urlDataString = fetchURLToString(url);
        } catch (IOException e)
        {
            throw new ComputationException("Error trying to read content of text file with " +
                    "name \"" + url + "\"");
        }

        StringGraphData outputStringUrlData = new StringGraphData(urlDataString);
        ArrayList<InfoSecGraphData> outputDataArray = new ArrayList<>();
        outputDataArray.add(outputStringUrlData);
        return outputDataArray;
    }

    private String fetchURLToString(String requestURL) throws IOException
    {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", FetchURL.class.getSimpleName())
                .put("numberInputs", getExpectedNumberInputs())
                .put("numberOutputs", getExpectedNumberOutputs())
                .put("nodeType", "Source").toString();

        return jsonString;
    }

    protected static int getExpectedNumberInputs()
    {
        return 0;
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
