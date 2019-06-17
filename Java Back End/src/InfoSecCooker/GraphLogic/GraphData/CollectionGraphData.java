package InfoSecCooker.GraphLogic.GraphData;

import java.util.List;

public class CollectionGraphData extends InfoSecGraphData
{
    List collection;

    public CollectionGraphData(List collection)
    {
        this.collection = collection;
    }

    public List getCollection()
    {
        return collection;
    }

    @Override
    public InfoSecGraphData getCopy()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return collection.toString();
    }
}
