package ro.control;

import javafx.scene.control.TextField;
import ro.model.RandomClientGenerator;
import ro.model.Service;
import ro.ui.GUI;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private final GUI gui;
    private long minArrivingTime;
    private long maxArrivingTime;
    private long minServiceTime;
    private long maxServiceTime;
    private long numberOfQueues;
    private long simulationTime;
    private List<Service> services;

    /**
     * The method “Simulation” gets the gui GUI as a parameter.
     * The services are instantiated in a new Array List.
     */
    public Simulation(GUI gui) {
        this.gui = gui;
        services = new ArrayList<>();
    }

    /**
     * The method start the data of each fields from the gui in millis value.
     * The fields are: minArrivingTime, maxArrivingTime, minServiceTime, maxServiceTime, simulationTime, numberOfQueues.
     * In order to transform the String value of the inputs in millis, it uses the getMillisValue method, which I described below
     * It creates the queues, using the createQueues method described below.
     * It starts the generator, using the startGenerator method described below.
     */
    public void start() {
        minArrivingTime = getMillisValue(gui.getMinArrivingTimeInput());
        maxArrivingTime = getMillisValue(gui.getMaxArrivingTimeInput());
        minServiceTime = getMillisValue(gui.getMinServiceTimeInput());
        maxServiceTime = getMillisValue(gui.getMaxServiceTimeInput());
        simulationTime = getMillisValue(gui.getSimulationIntervalInput());
        numberOfQueues = getMillisValue(gui.getNrQueuesInput()) / 1000;

        createQueues();
        startGenerator();
    }

    /**
     * total of clients
     */
    private long getTotalClients() {
        long k = 0;
        for (Service service : services
        ) {
            k += service.getClientCount();
        }
        return k;
    }

    /**
     * The start generator method generates the start of the simulation using the Thread
     * This concept creates a new Thread each time for each simulation.
     * It uses the generator from the RandomClientGenerator, considering the
     * minimum and the maximum service time, the minimum and the maximum arriving time,
     * the simulation time and the services
     * The generator starts with the run method described before
     * the gui gets its button disable as long as the simulation is running
     * And in the end, the thread starts itself, which does also start the simulation itself
     */
    private void startGenerator() {
        new Thread(() -> {
            RandomClientGenerator generator = new RandomClientGenerator(
                    minServiceTime,
                    maxServiceTime,
                    minArrivingTime,
                    maxArrivingTime,
                    simulationTime,
                    services);
            generator.run();
            gui.getStartSimulationButton().setDisable(false);
        }).start();
    }

    /**
     * This method creates the queues, as its name suggest.
     * It does not have any parameters.
     * For all the queues it creates a new Service, having the name "S" and the number which corresponds to it,
     * and its own simulation time = how long does it take for it to proceed all the clients' products
     * The service is added to the list of the services.
     * The service adds as the observer the gui, which means that it notifies when it has changed
     * For each service, a new Thread starts. Here appears the well-known concept of multithreading
     */
    private void createQueues() {
        for (long i = 0; i < numberOfQueues; i++) {
            Service service = new Service("S" + i, simulationTime);
            services.add(service);
            service.addObserver(gui);
            new Thread(service).start();
        }
    }

    /**
     * This method gets th millis value transformation,
     * from the String type from the input, it makes the parse to Long and it multiplies with 1000
     * in order to get the millis.
     *
     * @param minArrivingTimeInput TextField
     * @return the value of the minArrivingTimeInput in millis
     */
    private long getMillisValue(TextField minArrivingTimeInput) {
        String text = minArrivingTimeInput.getText();
        return Long.parseLong(text) * 1000;
    }
}
