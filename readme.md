# General description

This application is a multiple elevators emulator. Allows you to see the interaction of elevators with arbitrary numbers of building floors, passengers which want to be transported and elevators itself.

## Start the program

To start this program you should create *config.properties* file and specify path to it as a program argument. If you don't, the program will use default config file with parameters inside.
All you have to do it's create an instance of *ElevatorsEmulator* with program args as parameter, and invoke the method *emulate()*.
You'll receive log messages in file and console during entire transportation cycle. 
After that you'll receive five debug messages which check the success of the transportation

## Restrictions on arguments:

1. Floors number: at least 2, if not, the number will be set as 2.
2. Elevator capacity: at least 1, if not, the capacity will be set as 1.
3. Passengers number: at least 1, if not, the number will be set as 1.
4. Elevators number: at least 1, if not, the number will be set as 1.
All inputs must be integer values or strings which can be parsed to integers.

## Class Descriptions

### ElevatorsEmulator

#### Description
The main working class

##### Constructor
Creates an instance of this class
@param **args** - an array with the path to your *config.properties* file as the first element

##### Methods

###### void emulate()
Performs all "under the hood" operations e.g. creates instances of *Passenger*, *Elevator*, *Building*, *Controller* etc. Some parameters are random, some of them not. Starts passenger threads and transportation threads 

### InputValidator

#### Description
Utility class, checks your *config.properties* input data. If some parameters are wrong, it replaces them to default values.

##### Methods

###### Map<String, Integer> getIntProperties()

Derives properties values from your *config.properties* file and validates them.

@returns **properties** validated properties if all good and default properties for incorrect values. If some properties are correct, and some are not, only incorrect values will be replaced to default values.

### EndTaskValidator

#### Description
Utility class, performs five validation operations after all threads has been closed

##### Methods

###### static void validateResult(List<Floor> floors, List<Elevator> elevators, int passengersNumber)

@param **floors** - list of floors in the building
@param **elevators** - list of elevators in the building
@param **passengersNumber** - entire number of passengers which have to be transported

### MyThreadFactory

#### Description
Utility class, returns new *ThreadFactory* which creates threads with more readable names

##### Methods

###### static ThreadFactory getThreadFactory(String threadName)

@param **threadName** - general name you want to give to the threads which current factory will create.
@returns **ThreadFactory** instance of the *ThreadFactory* class

### ElevatorMovementTask

#### Description
The class which in charge of moving of the one elevator. For every elevator will be created his personal *ElevatorMovementTask* instance.

##### Constructor
@param **elevator** - an instance of the *Elevator* class
@param **controller** - an instance of the *Controller* class
@param **countDownLatch** - an instance of the *CountDownLatch* class, to inform emulator that current elevator has finished its task.
@param **lock** - an instance of the *Lock* class to capture the monitor while current elevator are moving

##### Methods

###### void run()
Performs moving operations until *controller* says that current elevator no longer needed.

### PassengerTransportationTask

#### Description
The class which in charge of moving of the one passenger. For every passenger will be created his personal *PassengerTransportationTask* instance.

##### Constructor
@param **passenger** - an instance of the *Passenger* class
@param **controller** - an instance of the *Controller* class

##### Methods

###### void run()
Performs moving operations until *passenger* won't be transportated to the destination floor.

### Constants
Simple class with the most usable constants in this application

### Messages
Simple class with debug messages and some error messages

### Building
POJO class which emulates *building*

### Elevator
POJO class which emulates *elevator*

### Passenger
POJO class which emulates *passenger*

### Floor
POJO class which emulates *floor*

### MovementDirection
Simple enum class with two elevator directions: *down* and *up*

### TransportationState
Simple enum class with three passenger states: *not started*, *in progress* and *completed*

### Controller
Essential class which have control under either all elements of building like *floors* and *elevators* and *passengers* itself.

##### Constructor
@param **building** - an instance of the *Building* class
@param **elevators** - list of all *elevators* which relate to this building
@param **passengerCount** - exact amount of all *passengers* to stop redundant elevators on time.

##### Methods

###### void sitPassenger(Passenger passenger)
Sits *passenger* into an *elevator* if it available, of passenger desired *direction* coincides to elevator *direction* otherwise blocks passenger thread

@param  **passenger** an instance of the *Passenger* class

###### void releasePassenger(Passenger passenger)
Releases *passenger* into the current *floor* if passenger *destination floor* coincides to the current *floor* otherwise blocks passenger thread

@param  **passenger** an instance of the *Passenger* class

###### void sitPassengers(Elevator elevator)
Sits all *passengers* into an *elevator* from the current floor  if it's possible, awaits until all *passengers* from the *floor* have sat or *elevator* will have no more free space

@param  **elevator** an instance of the *Elevator* class

###### void releasePassengers(Elevator elevator)
Releases all *passengers* from an *elevator* to the current floor  if it's possible, awaits until all *passengers* from the *elevator* have been released or *elevator* will have no more passengers inside.

@param  **elevator** an instance of the *Elevator* class

###### boolean doTask(Elevator elevator)
Checks if current *elevator* still needed to transportation. If no, stops the *elevator*

@param  **elevator** an instance of the *Elevator* class

@returns **true** if elevator still needed, otherwise **false**
