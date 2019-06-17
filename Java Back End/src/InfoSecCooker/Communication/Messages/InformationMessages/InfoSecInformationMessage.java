package InfoSecCooker.Communication.Messages.InformationMessages;

import InfoSecCooker.Communication.Messages.InfoSecControlMessage;

/**
 * This class represents a message to report information from the InfoSecCooker application.
 * Information messages are used to report internal state of the InfoSecCooker to the front end application.
 * The information messages exchanged with the front end application will be derived from this class.
 * They will be sent as JSON objects of each derived class. But will have a Java class representation for better abstraction.
 */
public abstract class InfoSecInformationMessage implements InfoSecControlMessage
{

}
