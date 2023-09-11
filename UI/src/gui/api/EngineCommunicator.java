package gui.api;

import manager.EngineAgent;

/**
 * We add this to classes that we want to communicate with the engine.
 */
public interface EngineCommunicator {
    EngineAgent getEngineAgent();
}
