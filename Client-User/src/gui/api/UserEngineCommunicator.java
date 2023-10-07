package gui.api;

import manager.UserServerAgent;

/**
 * We add this to classes that we want to communicate with the engine.
 */
public interface UserEngineCommunicator {
    UserServerAgent getEngineAgent();
}
