package ro.model;


public class Client {
    private String name;
    private long serviceTime;

    public Client(String name, long serviceTime) {
        this.name = name;
        this.serviceTime = serviceTime;
    }

    public Client() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(long serviceTime) {
        this.serviceTime = serviceTime;
    }
}
