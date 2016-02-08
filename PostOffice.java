import java.io.*;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

public class PostOffice {

    private static List<PostOffice> POlist = new ArrayList<>();
    private static List<String> criminalList = new ArrayList<>();
    private static int DAY = 1;

    private List<Mail> itemsInTransit;
    private List<Mail> items;
    private int numItems;

    private String name;
    private int transit_time;
    private int postage;
    private int capacity;
    private int persuasion;
    private int max_length;

    private String fileName;
    private PrintWriter logFile;

    PostOffice(String officeName, int i_tran, int i_pos, int i_cap, int i_per, int i_length ) throws IOException {

        name = officeName;
        transit_time = i_tran;
        postage = i_pos;
        capacity = i_cap;
        persuasion = i_per;
        max_length = i_length;

        numItems = 0;
        items = new ArrayList<>();
        itemsInTransit = new ArrayList<>();

        fileName = "log_" + officeName + ".txt";
        logFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName,false)));

        POlist.add(this);
    }

    private boolean checkPOlist(String POname) {
        for(int i = 0; i < POlist.size(); i++) {
            if (POlist.get(i).name.equals(POname))
                return true;
        }
        return false;
    }

    private boolean checkCriminalList(String cName){
        for(int i = 0; i < criminalList.size(); i++) {
            if (criminalList.get(i).equals(cName))
                return true;
        }
        return false;
    }

    private void appendToFile(String fileName) throws IOException {
        this.logFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
    }

    private void printReject(Mail item, PrintWriter mlog) throws IOException {
        appendToFile(fileName);
        this.logFile.println("- " + "Rejected " + item.getClass().getSimpleName() + " -");
        this.logFile.println("Source: " + this.name);
        mlog.println("- " + "Rejected " + item.getClass().getSimpleName() + " -");
        mlog.println("Source: " + this.name);
        mlog.flush();
        this.logFile.close();
    }

    private void printDestroy(PostOffice PO, Mail item, String mlogName) throws IOException {
        PrintWriter temp = new PrintWriter(new BufferedWriter(new FileWriter(PO.fileName,true)));
        temp.println("- Incinerated " + item.getClass().getSimpleName() + " -");
        temp.println("Destroyed at: " + PO.name);

        PrintWriter mlog = new PrintWriter(new BufferedWriter(new FileWriter(mlogName,true)));
        mlog.println("- Incinerated " + item.getClass().getSimpleName() + " -");
        mlog.println("Destroyed at: " + PO.name);
        mlog.flush();
        temp.close();
    }

    private void printMail(Mail item, boolean accepted) throws IOException {
        appendToFile(fileName);
        if(accepted)
            this.logFile.println("- " + "Accepted " + item.getClass().getSimpleName() + " -");
        else {
            this.logFile.println("- " + "New " + item.getClass().getSimpleName() + " -");
            this.logFile.println("Source: " + this.name);
        }
        this.logFile.println("Destination: " + item.getDestination());
        this.logFile.close();
    }

    /*
    If mail violates any of these conditions return false
    If true, proceed to accept the mail and prepare to send it at the end of the day
     */
    private boolean checkConditions(Mail item, boolean bribe) {
        if(!checkPOlist(item.getDestination()))
            return false;

        if(checkCriminalList(item.getRecipient()))
            return false;

        if(item instanceof PACKAGE && !bribe) {
            if( ((PACKAGE)item).getCurrency() < this.postage)
                return false;
            else if( ((PACKAGE)item).getCurrency() >= this.postage && ((PACKAGE)item).getLength() > this.max_length )
                return false;

            PostOffice destination = getPO(item.getDestination());
            if(destination != null) {
                if (((PACKAGE) item).getCurrency() >= this.postage && ((PACKAGE) item).getLength() > destination.max_length)
                    return false;
            }
        }

        if (this.numItems >= this.capacity)
            return false;

        return true;
    }

    /*
    Run function at start of each day
    Destroy any packages that exceed destinations length limit
    Destroy any mail if destination has full capacity
    Add mail to Postal Office and remove it from transit
    Write to destination
     */
    private void checkTransitItems(String mlogName) throws IOException {
        for (int i = 0; i < this.itemsInTransit.size(); i++){
            Mail item = this.itemsInTransit.get(i);
            PostOffice destination = getPO(item.getDestination());

            if(item.getDayTransit() == (this.transit_time)) {
                item.setArrival(true);

                if(item instanceof PACKAGE) {
                    if (((PACKAGE) item).getLength() > destination.max_length) {
                        System.out.println("Package at destroyed destination, length requirement not met");
                        printDestroy(destination,item,mlogName);
                        itemsInTransit.remove(i);
                        i--;
                        continue;
                    }
                }

                if(destination.capacity <= destination.items.size()) {
                    printDestroy(destination,item,mlogName);
                    System.out.println("Package is destroyed destination, PO is full");
                    itemsInTransit.remove(i);
                    i--;
                    continue;
                }

                destination.logFile = new PrintWriter(new BufferedWriter(new FileWriter(destination.fileName,true)));
                destination.logFile.println("- Standard transit arrival -");
                destination.logFile.close();
                destination.items.add(item);
                itemsInTransit.remove(i);
                i--;
                System.out.println("Post Office: " + destination.name + " there is " + destination.items.size());
            }
        }
    }

    /*
    Increments days passed in transit for all items belonging to respective Postal Offices
     */
    private void incrementTransitTime(){
        for(int i = 0; i < this.itemsInTransit.size(); i++){
            Mail item = this.itemsInTransit.get(i);
            item.incrementDayTransit();
        }
    }

    /*
    At the end of the day, send all mail in the Postal Office to transit
    If mail is marked as arrived, or if mail's destination name is the same as the local Postal Office
    Increment days passed on each mail item
     */
    private void sendMail() throws IOException {
        for(int i = 0; i < this.items.size(); i++) {
            Mail item = this.items.get(i);
            if( !(item.getDestination().equals(this.name)) ) {
                this.itemsInTransit.add(item);
                appendToFile(fileName);
                this.logFile.println("- Standard transit departure -");
                this.logFile.close();
                this.items.remove(i);
                i--;
            }
            else {
                if(item.getArrival())
                    item.incrementDayArrival();
            }
        }
    }

    /*
    Destroy all items that is sitting at the Postal Office for more than 14 days
    Write a log to the Postal Office that destroyed the mail
     */
    private void destroyMail(String mlogName) throws IOException {
        for(int i = 0; i < this.items.size(); i++) {
            Mail item = this.items.get(i);
            if (item.getDayArrival() >= 14) {
                if (item instanceof LETTER) {
                    if ( !((LETTER) item).getRetrnAdr().equals("NONE") ) {
                        mailItem(new LETTER(item.getDestination(), ((LETTER) item).getRetrnAdr(), item.getLocalPOname(), "NONE"), logFile);
                        this.items.remove(item);
                        i--;
                        continue;
                    }
                }
                this.printDestroy(this,item,mlogName);
                this.items.remove(item);
                i--;
            }
        }
    }

    /*
    Takes a mail object as argument and checks if mail is a valid item or not
    If mail is rejected for any of the conditions print to the local Postal Office log
     */
    public void mailItem(Mail item, PrintWriter mlog) throws IOException {
        boolean bribe = false;
        printMail(item, false);

        if (item instanceof PACKAGE) {
            if ( ((PACKAGE)item).getCurrency() >= (this.postage+this.persuasion) && ((PACKAGE)item).getLength() > this.max_length ){
                mlog.println("- Something funny going on... -");
                mlog.println("Where did that extra money at " + this.name + " come from?");
                mlog.flush();
                bribe = true;
            }
        }

        if (!checkConditions(item,bribe)) {
            printReject(item, mlog);
            return;
        }

        printMail(item, true);
        this.items.add(item);
    }

    /*
    Simulates a postal office pick up, if the person picking up the item is a wanted criminal, disallow it
    The person will be able to pick up mail if it is in the postal office, not in transit and mail must exist
     */
    public void pickUp(String recipient, PrintWriter flog) throws IOException {
        PostOffice local = this;

        System.out.println("This postal office has " + local.items.size());
        for(int i = 0; i < local.items.size(); i++) {
            Mail item = local.items.get(i);
            if(checkCriminalList(recipient)) {
                flog.println(recipient + ", a wanted terrorist with links to ISIS has been caught today in the " + local.name + " postal office.");
                return;
            }

            if(item.getRecipient().equals(recipient)) {
                appendToFile(fileName);
                logFile.println("- Delivery process complete -");
                logFile.println("Delivery took " + (DAY - (item.getInitialDay()-1)) + " days.");
                local.items.remove(item);
                i--;
            }
        }
        logFile.close();
    }

    /*
    Returns a Postal Office object given a name
    Returns null if no postal office is found
     */
    public static PostOffice getPO(String POname) {
        for(int i = 0; i < POlist.size(); i++) {
            if (POlist.get(i).name.equals(POname))
                return POlist.get(i);
        }
        return null;
    }


    /*
    At the end of each day remove all accepted items from their initiating post offices, such
    items are now in transit. Write to master log and postal logs
     */
    public static void EndDay(String mlogName) throws IOException {
        for (int i = 0; i < POlist.size(); i++) {
            POlist.get(i).incrementTransitTime();
            POlist.get(i).destroyMail(mlogName);
            POlist.get(i).sendMail();
        }

        for (int i = 0; i < POlist.size(); i++) {
            PostOffice PO = POlist.get(i);
            PO.appendToFile(PO.fileName);
            PO.logFile.println("- - DAY " + DAY + " OVER - -");
            PO.logFile.close();
        }

        PrintWriter mlog = new PrintWriter(new BufferedWriter(new FileWriter(mlogName,true)));
        mlog.println("- - DAY " + DAY + " OVER - -");
        mlog.close();

        DAY++;
    }

    /*
    At the beginning of each day, check if any in transit items have arrived at the receiving
    Postal Office
     */
    public static void StartDay(String mlogName) throws IOException {
        for (int i = 0; i < POlist.size(); i++) {
            POlist.get(i).checkTransitItems(mlogName);
        }
    }

    public static int currentDay() {
        return DAY;
    }

    /*
    List of criminals added given a name, read through a file
     */
    public static void addCriminal(String cName) {
        criminalList.add(cName);
    }

}
