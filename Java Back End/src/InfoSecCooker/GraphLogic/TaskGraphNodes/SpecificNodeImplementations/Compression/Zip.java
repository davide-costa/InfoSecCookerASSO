package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Compression;

import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.PipeGraphEdge.PipeGraphEdge;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Collections.SortNodeTask;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Deflater;

public class Zip extends CompressionNodeTask
{
    public Zip(GraphNodeInformation graphNodeInformation,
               RunTimeConfigurations runTimeConfigurations)
    {
        super(graphNodeInformation, runTimeConfigurations);
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException, InputParametersException
    {
        ByteArrayGraphData byteArrayGraphData = (ByteArrayGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, ByteArrayGraphData.class, 0);
        byte[] compressedBytes = handleZipCompressRequestComputation(byteArrayGraphData.getBytes());

        ArrayList<InfoSecGraphData> resultArray = new ArrayList<>();
        resultArray.add(new ByteArrayGraphData(compressedBytes));
        return resultArray;
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            ParsingInputsException, InputParametersException
    {
        switch (index)
        {
            case 1:
                return transformBytesBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing Zip class");
        }
    }

    private InfoSecGraphData transformBytesBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        byte[] concatedBytes = new byte[0];

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            ByteArrayGraphData bytesArrayGraphData = (ByteArrayGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), StringGraphData.class, 0);

            byte[] currentInputBytes = bytesArrayGraphData.getBytes();
            byte[] newConcatedBytes = new byte[concatedBytes.length + currentInputBytes.length];
            System.arraycopy(concatedBytes, 0, newConcatedBytes, 0, concatedBytes.length);
            System.arraycopy(currentInputBytes, 0, newConcatedBytes, concatedBytes.length, currentInputBytes.length);

            concatedBytes = newConcatedBytes;
        }

        return new ByteArrayGraphData(concatedBytes);
    }

    private byte[] handleZipCompressRequestComputation(byte[] data) throws ComputationException
    {
        byte[] compressedData;
        try
        {
            compressedData = compress(data);
        } catch (IOException e)
        {
            throw new ComputationException("Error compressing data with size " + data.length);
        }

        return compressedData;
    }

    public static byte[] compress(byte[] data) throws IOException
    {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished())
        {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();

        return output;
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", Zip.class.getSimpleName())
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
