package gui.api;

import manager.UserEngineAgent;

/**
 * We add this to classes that we want to communicate with the engine.
 */
public interface UserEngineCommunicator {
    UserEngineAgent getEngineAgent();
}
