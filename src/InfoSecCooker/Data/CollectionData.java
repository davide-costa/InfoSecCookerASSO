package InfoSecCooker.Data;

import java.io.Serializable;
import java.util.List;

public class CollectionData extends InfoSecData
{
    List collection;

    public CollectionData(List collection)
    {
        //TODO fix this aldrabation
        super((Serializable) collection);
        this.collection = collection;
    }

    public List getCollection()
    {
        return collection;
    }

    //TODO fix this aldrabation
    @Override
    public InfoSecData getCopy()
    {
        return this;
    }

    @Override
    public String toString()
    {
        return collection.toString();
    }
}
