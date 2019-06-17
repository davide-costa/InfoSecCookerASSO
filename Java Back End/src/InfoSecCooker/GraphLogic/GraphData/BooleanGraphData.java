package InfoSecCooker.GraphLogic.GraphData;

public class BooleanGraphData extends InfoSecGraphData
{
    private boolean boolValue;


    public BooleanGraphData(boolean boolValue)
    {
        this.boolValue = boolValue;
    }

    public boolean getInt()
    {
        return boolValue;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return new BooleanGraphData(boolValue);
    }

    @Override
    public String toString()
    {
        if(boolValue)
            return "true";
        else
            return "false";
    }
}
