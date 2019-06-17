package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks;

import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources.ReadTextFile;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class WriteTextToFile extends SinkNodeTask
{
    private String filepath;

    public WriteTextToFile(GraphNodeInformation graphNodeInformation,
                           RunTimeConfigurations runTimeConfigurations,
                           String filepath)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.filepath = filepath;
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException, InputParametersException
    {
        StringGraphData stringGraphData = (StringGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, StringGraphData.class, 0);
        String string = stringGraphData.getString();

        handleSaveTextToFileSystemRequestComputation(string);

        return null; //sink node, no output value
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        switch (index)
        {
            case 1:
                return transformBytesDataBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing Write to file class");
        }
    }

    private InfoSecGraphData transformBytesDataBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        String concatedString = "";

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            StringGraphData stringGraphData = (StringGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), StringGraphData.class, 0);
            concatedString += stringGraphData.getString();
        }

        return new StringGraphData(concatedString);
    }

    private void handleSaveTextToFileSystemRequestComputation(String string) throws ComputationException
    {
        try
        {
            saveTextToFileSystem(filepath, string);
        } catch (IOException e)
        {
            throw new ComputationException("Error trying to save content string with " + string.length() + " to file with " +
                    "name \"" + filepath + "\"");
        }
    }

    private void saveTextToFileSystem(String filename, String string) throws IOException
    {
        PrintWriter out = new PrintWriter(filename);
        out.print(string);
        out.close();
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", WriteTextToFile.class.getSimpleName())
                .put("numberInputs", getExpectedNumberInputs())
                .put("numberOutputs", getExpectedNumberOutputs())
                .put("nodeType", "Sink").toString();

        return jsonString;
    }

    protected static int getExpectedNumberInputs()
    {
        return 1;
    }

    protected static int getExpectedNumberOutputs()
    {
        return 0;
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
