package InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sinks;

import InfoSecCooker.Communication.TCPMessengers.TCPClientMessenger;
import InfoSecCooker.GraphLogic.GraphData.*;
import InfoSecCooker.GraphLogic.GraphRunning.RunTimeConfigurations;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ComputationException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.InputParametersException;
import InfoSecCooker.GraphLogic.GraphRunning.RuntimeExceptions.ParsingInputsException;
import InfoSecCooker.GraphLogic.TaskGraphNodes.GraphNodeInformation;
import InfoSecCooker.GraphLogic.TaskGraphNodes.SpecificNodeImplementations.Sources.ReadTextFile;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WriteToSocket extends SinkNodeTask
{
    private String socketAddress;
    private int socketPort;

    public WriteToSocket(GraphNodeInformation graphNodeInformation,
                         RunTimeConfigurations runTimeConfigurations,
                         String socketAddress, int socketPort)
    {
        super(graphNodeInformation, runTimeConfigurations);
        this.socketAddress = socketAddress;
        this.socketPort = socketPort;
    }

    @Override
    protected ArrayList<InfoSecGraphData> computeOutput(ArrayList<InfoSecGraphData> infoSecPacketArrayList) throws
            ComputationException, InputParametersException
    {
        ByteArrayGraphData byteArrayGraphData = (ByteArrayGraphData)
                getNthParameterIfExistent(infoSecPacketArrayList, ByteArrayGraphData.class, 0);
        byte[] bytes = byteArrayGraphData.getBytes();
        handleSendBytesToSocketRequestComputation(bytes);

        return null; //sink node, no output value
    }

    @Override
    protected InfoSecGraphData transformBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection, int index) throws
            InputParametersException, ParsingInputsException
    {
        switch (index)
        {
            case 2:
                return transformBytesDataBufferedInputIntoNormalInput(infoSecGraphDataBufferedCollection);
            default:
                throw new ParsingInputsException("Unexpected parameter on parsing Write to file class");
        }
    }

    private InfoSecGraphData transformBytesDataBufferedInputIntoNormalInput(InfoSecGraphDataBufferedCollection infoSecGraphDataBufferedCollection) throws
            InputParametersException
    {
        byte[] concatedBytes = new byte[0];

        ArrayList<InfoSecGraphData> bufferedInputs = infoSecGraphDataBufferedCollection.getCollection();
        for (InfoSecGraphData bufferedInputBase : bufferedInputs)
        {
            InfoSecGraphDataBufferedCollection bufferedInput = (InfoSecGraphDataBufferedCollection) bufferedInputBase;
            ByteArrayGraphData byteArrayGraphData = (ByteArrayGraphData)
                    getNthParameterIfExistent(bufferedInput.getCollection(), ByteArrayGraphData.class, 0);
            byte[] bytes = byteArrayGraphData.getBytes();

            byte[] currentInputBytes = byteArrayGraphData.getBytes();
            byte[] newConcatedBytes = new byte[concatedBytes.length + currentInputBytes.length];
            System.arraycopy(concatedBytes, 0, newConcatedBytes, 0, concatedBytes.length);
            System.arraycopy(currentInputBytes, 0, newConcatedBytes, concatedBytes.length, currentInputBytes.length);

            concatedBytes = newConcatedBytes;
        }

        return new ByteArrayGraphData(concatedBytes);
    }

    private void handleSendBytesToSocketRequestComputation(byte[] bytes) throws ComputationException
    {
        TCPClientMessenger tcpClientMessenger;
        try
        {
            tcpClientMessenger = new TCPClientMessenger(socketAddress, socketPort);
            tcpClientMessenger.writeMessage(bytes);
        } catch (IOException e)
        {
            throw new ComputationException("Error sending bytes to " + socketAddress + " on port "  + socketPort);
        }
    }

    public static String getTaskInformation()
    {
        String jsonString = new JSONObject()
                .put("name", WriteToSocket.class.getSimpleName())
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
