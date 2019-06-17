package InfoSecCooker.Communication.Messages.CommandMessages;

import InfoSecCooker.Communication.Messages.InfoSecRequestMessage;

/**
 * This class represents a message to command the InfoSecCooker application.
 * Command messages are used by the front end application to command the InfoSecCooker Java application (which implements the logic).
 * The command messages exchanged with the front end application will be derived from this class.
 * They will be sent as JSON objects of each derived class. But will have a Java class representation for better abstraction.
 */
public abstract class InfoSecCommandMessage extends InfoSecRequestMessage
{

}
