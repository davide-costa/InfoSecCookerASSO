package InfoSecCooker.GraphLogic.GraphData;

public class IntGraphData extends InfoSecGraphData
{
    private int integer;


    public IntGraphData(int integer)
    {
        this.integer = integer;
    }

    public int getInt()
    {
        return integer;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return new IntGraphData(integer);
    }

    @Override
    public String toString()
    {
        return String.valueOf(integer);
    }
}
