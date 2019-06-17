package InfoSecCooker.GraphLogic.GraphData;

import java.util.ArrayList;

public class InfoSecGraphDataBufferedCollection extends InfoSecGraphData
{
    ArrayList<InfoSecGraphData> collection;

    public InfoSecGraphDataBufferedCollection(ArrayList<InfoSecGraphData> collection)
    {
        this.collection = collection;
    }

    public ArrayList<InfoSecGraphData> getCollection()
    {
        return collection;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return null;
    }
}
