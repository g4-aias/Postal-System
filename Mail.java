
public abstract class Mail {
    private String destination;
    private String recipient;
    private String localPOname;
    private boolean arrived;
    private int initialDay;
    private int daysPassedSinceTransit;
    private int daysPassedSinceArrival;

    Mail(String n_destination, String n_recipient, String n_localPOname){
        destination = n_destination;
        recipient = n_recipient;
        localPOname = n_localPOname;
        arrived = false;
        daysPassedSinceArrival = 1;
        daysPassedSinceTransit = 0;
        initialDay = PostOffice.currentDay();
    }

    public void incrementDayTransit() {
        this.daysPassedSinceTransit++;
    }

    public void incrementDayArrival() {
        this.daysPassedSinceArrival++;
    }

    public void setArrival(boolean flag) {
        this.arrived = flag;
    }

    public boolean getArrival() {
        return this.arrived;
    }

    public int getDayTransit() {
        return this.daysPassedSinceTransit;
    }

    public int getDayArrival() {
        return this.daysPassedSinceArrival;
    }

    public int getInitialDay() {
        return this.initialDay;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getLocalPOname() {
        return this.localPOname;
    }


}
