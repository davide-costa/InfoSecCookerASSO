package InfoSecCooker.Data;

import java.io.Serializable;

public abstract class InfoSecData
{
    private Serializable dataObj;


    public InfoSecData(Serializable dataObj)
    {
        this.dataObj = dataObj;
    }

    public abstract InfoSecData getCopy();

    @Override
    public abstract String toString();
}
