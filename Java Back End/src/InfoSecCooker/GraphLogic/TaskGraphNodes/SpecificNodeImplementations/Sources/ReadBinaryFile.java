package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources;

import InfoSecCooker.GraphLogic.GraphData.ByteArrayGraphData;
import InfoSecCooker.GraphLogic.GraphData.InfoSecGraphData;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Paths;

public class ReadBinaryFile extends ReadFromFile
{
    public ReadBinaryFile(GraphNodeInformation graphNodeInformation,
                        RunTimeConfigurations runTimeConfigurations,
                        String filepath)
    {
        super(graphNodeInformation, runTimeConfigurations, filepath);
    }

    @Override
    protected InfoSecGraphData computeLoadFileFromSystem(String fileName) throws ComputationException
    {
        byte[] bytes;
        try
        {
            bytes = readBinaryFileFromFileSystem(fileName);
        } catch (IOException e)
        {
            throw new ComputationException("Error trying to read content of binary file with " +
                    "name \"" + fileName + "\"");
        }

        ByteArrayGraphData byteArrayGraphData = new ByteArrayGraphData(bytes);
        return byteArrayGraphData;
    }

    private byte[] readBinaryFileFromFileSystem(String filename) throws IOException
    {
        return java.nio.file.Files.readAllBytes(Paths.get(filename));
    }


    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", ReadBinaryFile.class.getSimpleName())
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
