package ro.model;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class RandomClientGenerator {

    private long minServiceTime;
    private long maxServiceTime;
    private long minArrivalTime;
    private long maxArrivalTime;
    private long simulationTimeMillis;
    private List<Service> services;

    public RandomClientGenerator(long minServiceTime, long maxServiceTime, long minArrivalTime, long maxArrivalTime, long simulationTimeMillis, List<Service> services) {
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.simulationTimeMillis = simulationTimeMillis;
        this.services = services;
    }

    /**
     * Generates clients and assigns them to services
     */
    public void run() {
        try {
            long clientsGenerated = 0;
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + simulationTimeMillis) {
                Service minLoadService = getMinLoadService();
                Client client = generateClient(clientsGenerated++);
                minLoadService.addClient(client);
                long arrivalTime = ThreadLocalRandom.current().nextLong(minArrivalTime, maxArrivalTime);
                Thread.sleep(arrivalTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates clients
     * it sets their names
     * they are generated random within the time of min and max service time given in the GUI
     */
    private Client generateClient(long clientId) {
        Client client = new Client();
        client.setName("Client" + clientId);
        long serviceTime = ThreadLocalRandom.current().nextLong(minServiceTime, maxServiceTime);
        client.setServiceTime(serviceTime);
        return client;
    }

    /**
     * This method gets the minimum load service
     * for each service of services
     * if the service for a queue is less than the last selected minimum, then the minimum gets the new service
     * this is useful in order to generate the clients to the quickest queue service
     *
     * @return result (the service)
     */
    private Service getMinLoadService() {
        Service result = services.get(0);
        for (Service service : services) {
            if (service.getClientCount() < result.getClientCount()) {
                result = service;
            }
        }
        return result;
    }
}
