package InfoSecCooker.Outputs;

import java.io.Serializable;

public abstract class InfoSecData
{
    Serializable dataObj;

    public InfoSecData(Serializable dataObj)
    {
        this.dataObj = dataObj;
    }

    public abstract InfoSecData getCopy();

    @Override
    public abstract String toString();
}
