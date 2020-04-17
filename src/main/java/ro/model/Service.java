package ro.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Service extends Observable implements Runnable {

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private final String serviceName;
    private final BlockingQueue<Client> queue;
    private final long simulationTimeMillis;
    private long averageTime;
    private DateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
    private String peakTime;

    /**
     * @param serviceName          the name of the service
     * @param simulationTimeMillis the simulation time in millis
     *                             the queue is now instantiated as a new Linked Blocking Queue
     */
    public Service(String serviceName, long simulationTimeMillis) {
        this.simulationTimeMillis = simulationTimeMillis;
        this.serviceName = serviceName;
        queue = new LinkedBlockingQueue<>();
    }

    /**
     * This method run is override from the Runnable interface
     * the variable startTime gets the current time in millis from the system (the current real time)
     * while the current time in millis is less than the start time + simulation millis
     * then the client (who is the first element of the queue) is retrieved and removed from the queue(its head actually)
     * and if the queue is empty it returns null
     * the observers are notified about the client and the generateMessage method is called, having the @param client
     * the thread is suspended until the time in which the client is served is done
     * These are made within an exception because if the program terminates abnormally
     * it is needed to be handled
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            long totalTime = 0;
            long howManyClients = 0;
            long max = 0;
            while (System.currentTimeMillis() < startTime + simulationTimeMillis) {
                if (max < queue.size()) {
                    max = queue.size();
                    peakTime = currentTime.format(new Date());
                }
                Client client = queue.poll(simulationTimeMillis, TimeUnit.SECONDS);
                howManyClients++;
                notifyObservers(generateMessage(client));
                long serviceTimePerClient = client.getServiceTime();
                Thread.sleep(client.getServiceTime());
                totalTime += serviceTimePerClient;
            }
            averageTime = totalTime / howManyClients;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Average time for queue " + serviceName + " is " + averageTime / 1000.00);
        System.out.println("Peak time for queue " + serviceName + " is " + peakTime);
        notifyObservers(generateMessageAverageTime(averageTime));
        notifyObservers(generateMessagePeakTime(peakTime));

    }

    /**
     * This method generates the message which will be send to the GUI further
     *
     * @return the current time: serviceName(queueNumber) processing client clientName (the time it took to service the client)
     */
    private String generateMessage(Client client) {
        String time = getTime();
        String serviceTime = String.valueOf((double) client.getServiceTime() / 1000);
        return time + ": " + serviceName + " processing client '" + client.getName() + "' (" + serviceTime + ")";
    }

    /**
     * generate message for the peak time
     *
     * @param peakTime String
     */
    private String generateMessagePeakTime(String peakTime) {
        return "\nPeak time for queue " + serviceName + " is " + peakTime;
    }

    /**
     * generate message for the average time
     *
     * @param averageTime long
     */
    private String generateMessageAverageTime(long averageTime) {
        return "\nAverage time for queue " + serviceName + " is " + averageTime / 1000.00;
    }

    /**
     * This method gets the current real time
     *
     * @return current real time in time format
     */
    private String getTime() {
        return TIME_FORMAT.format(new Date());
    }

    /**
     * This method notify the observers when something has changed
     */
    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }

    /**
     * This method adds each of the clients in the queue
     * It notifies the observers about the time at which the respective client arrived at what time
     * The client is added in the queue
     */
    public void addClient(Client client) {
        queue.add(client);
        notifyObservers(getTime() + ": " + client.getName() + " arrived at" + serviceName);
    }

    /**
     * This method gets the number of the clients which are already in the queue
     *
     * @return the size of the queue
     */
    public int getClientCount() {
        return queue.size();
    }
}
