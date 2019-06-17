package InfoSecCooker.Communication.Messages.GETResponseMessages;

import java.util.ArrayList;

public class AvailableTasksResponseMsg extends GETResponseMessage
{
    ArrayList<String> availableTasksNames;

    public AvailableTasksResponseMsg(ArrayList<String> availableTasksNames)
    {
        this.availableTasksNames = availableTasksNames;
    }
}
