package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources;

import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphData.StringGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

public class ReadTextFile extends ReadFromFile
{
    public ReadTextFile(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations,
                        String filepath)
    {
        super(graphNodeInformation, runTimeConfigurations, filepath);
    }

    @Override
    protected InfoSecGraphData computeLoadFileFromSystem(String fileName) throws ComputationException
    {
        String textString;
        try
        {
            textString = readTextFromFileSystem(fileName);
        } catch (IOException e)
        {
            throw new ComputationException("Error trying to read content of text file with " +
                    "name \"" + fileName + "\"");
        }

        StringGraphData stringArrayGraphData = new StringGraphData(textString);
        return stringArrayGraphData;
    }

    private String readTextFromFileSystem(String filename) throws IOException
    {
        return String.valueOf(java.nio.file.Files.readAllLines(Paths.get(filename)));
    }


    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", ReadTextFile.class.getSimpleName())
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
