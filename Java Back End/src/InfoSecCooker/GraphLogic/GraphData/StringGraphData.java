package InfoSecCooker.GraphLogic.GraphData;

public class StringGraphData extends InfoSecGraphData
{
    private String string;


    public StringGraphData(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return new StringGraphData(new String(string));
    }

    @Override
    public String toString()
    {
        return string;
    }
}
