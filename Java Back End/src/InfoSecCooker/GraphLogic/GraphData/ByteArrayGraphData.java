package InfoSecCooker.GraphLogic.GraphData;

public class ByteArrayGraphData extends InfoSecGraphData
{
     private byte[] bytes;


    public ByteArrayGraphData(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return new ByteArrayGraphData(bytes.clone());
    }

    @Override
    public String toString()
    {
        return bytes.toString();
    }
}
