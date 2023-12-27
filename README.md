# Predictions

Predictions is a generic simulation framework that allows users to define a world with entities and laws that govern their behavior. The framework is designed to be flexible and extensible, allowing users to create simulations for a variety of purposes.

## Technologies used

* IDE: IntelliJ
* Programming language: Java 8
* GUI: JavaFX
* Server: Apache Tomcat 10.0.11

## System overview
The Predictions framework consists of two types of desktop applications and a server.

The 2 desktops applications are:
* **Administrator application:** This application allows the administrator to upload new simulations (formatted as XML files), approve or reject client requests for simulation runs, review all client simulation runs, and perform additional administrative tasks. There can only be one administrator app running at a time, any attempt to run and connect another admin app to the server will result with its immediate closure.
* **User application:** The user application enables users to view the simulations currently held by the server, submit simulation requests to the administrator, run accepted simulation requests, review the progress of simulations, and access other related features.  There can be multiple users connected to the server. A user cannot connect if it chooses a username of an existing connected user.

All communication to and from the server is done using HTTP. The entirety of the “Predictions” system is configured to work only with the domain http://localhost:8080. Therefore, the server and the different clients must be operating from within the same host.

As mentioned, the Predictions framework is a versatile simulation framework that includes the following components:

* **World:** The world is a container for entities and laws. It defines the initial state of the simulation, including the number of entities and their properties.
* **Entity:** An entity is a single object in the world. It has a name, a type, and a set of properties.
* **Rule:** A rule is a set of instructions that govern the behavior of entities. It can change the properties of entities, create new entities, or delete entities.
* **Environment Properties:** The environment property is a set of global variables that can be accessed by entities and laws.
* **Termination conditions:** Termination conditions define when the simulation will end.

The system has the capability to run multiple simulations simultaneously for each user. The server maintains a thread pool to facilitate concurrent runs. 
The administrator has the authority to configure the thread pool size and adjust it when no other simulations are currently running or awaiting execution.

## Simulation process

The simulation process consists of the following steps:

1. **Initialization:** The world is initialized with the specified number of entities and their properties. The environment is also initialized with the specified values.
2. **Simulation steps:** The simulation runs for a specified number of steps. In each step, the following actions are performed:
    * All laws that are eligible to be executed are executed.
    * The properties of all entities are updated.
3. **Termination:** The simulation terminates if one of the termination conditions is met.

## Simulation output

At the end of the simulation, users can access the following output:

* **Entity counts and types:** A summary of the number of entities and their types.
* **Entity property averages:** The average value of a property for a given entity type over time.
* **Entity property consistency:** The consistency of a property value for a given entity type over time.

## Additional features

The Predictions framework also supports the following features:

* **Spatial entities:** Entities can be assigned a location in space.
* **Randomized entities and properties:** The properties of entities can be randomly generated at initialization.
* **Error handling:** The framework detects and reports errors that occur during the simulation.
* **Parallel Simulation Runs:** The framework can run multiple simulations simultaneously, using the number of threads specified in the configuration file.
* **Simulation Control:**  Users can stop, pause, and resume running simulations. They can also advance simulations by one tick while paused.

## Comments
* **README files:** Each release contains the app as well a readme file with detailed instructions about the app's features and its operation.
* **Configuration files:** Two sample simulation configuration files are provided for your convenience. If you want to write your own configuration file, you can use the provided files or the provided XML schema as a reference.
