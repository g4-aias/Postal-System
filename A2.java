import java.io.*;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class A2 {
    public static final String mlog = "master.txt";
    public static final String flog = "log_front.txt";
    public static PrintWriter masterLog;
    public static PrintWriter frontLog;

    A2() throws IOException {
        masterLog = new PrintWriter(new BufferedWriter(new FileWriter(mlog,false)));
        frontLog = new PrintWriter(new BufferedWriter(new FileWriter(flog,false)));
    }

    public static void main(String[] args) throws IOException {
        new A2();

        initiateOffices("offices.txt");
        initializeWantedList("wanted.txt");
        initiateCommands("commands.txt");

        masterLog.close();
        frontLog.close();
    }

    /*
    Read from commands.txt
    Given the command, run the appropriate function
     */
    public static void initiateCommands(String fileName) throws IOException {
        Scanner input = new Scanner(new File(fileName));
        String line;

        while(input.hasNextLine()){
            line = input.nextLine();
            Scanner sc = new Scanner(line);
            while (sc.hasNext()){
                helperAppendmLog(mlog);
                switch(sc.next()){
                    case "LETTER": newLetter(line, masterLog);
                        break;
                    case "PACKAGE": newPackage(line, masterLog);
                        break;
                    case "PICKUP": pickUp(line);
                        break;
                    case "DAY":
                        PostOffice.EndDay(mlog);
                        PostOffice.StartDay(mlog);
                        break;
                    default:
                        break;
                }
                break;
            }
        }
    }

    /*
    Make sure master log file is open and can be appended to
     */
    public static void helperAppendmLog(String fileName) throws IOException {
        masterLog = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
    }

    /*
    Get pick up information from command
    Attempt pick up
     */
    public static void pickUp(String command) throws IOException {
        Scanner sc = new Scanner(command);
        sc.next();
        List<String> data = new ArrayList<>();
        while(sc.hasNext()) {
            data.add(sc.next());
        }
        PostOffice localPO = PostOffice.getPO(data.get(0));
        localPO.pickUp(data.get(1), frontLog);
    }

    /*
    Instantiate a package instance
    If local PO is not null, attempt to give mail
     */
    public static void newPackage(String command, PrintWriter logFile) throws IOException {
        Scanner sc = new Scanner(command);
        sc.next();
        List<String> data = new ArrayList<>();
        List<Integer> data2 = new ArrayList<>();
        while(sc.hasNext()){
            if(sc.hasNextInt())
                data2.add(sc.nextInt());
            else
                data.add(sc.next());
        }
        PostOffice localPO = PostOffice.getPO(data.get(0));
        PACKAGE new_package = new PACKAGE(data.get(0),data.get(1),data.get(2),data2.get(0),data2.get(1));
        if(localPO != null)
            localPO.mailItem(new_package, logFile);
    }

    /*
    Instantiate a letter instance
    If local PO is not null, attempt to give mail
     */
    public static void newLetter(String command, PrintWriter logFile) throws IOException {
        Scanner sc = new Scanner(command);
        sc.next();
        List<String> data = new ArrayList<>();
        while(sc.hasNext()){
            data.add(sc.next());
        }
        PostOffice localPO = PostOffice.getPO(data.get(0));
        LETTER new_letter = new LETTER(data.get(0),data.get(1),data.get(2),data.get(3));
        if(localPO != null)
            localPO.mailItem(new_letter, logFile);
    }

    /*
    Initialize all postal offices from the file offices.txt
     */
    public static void initiateOffices(String fileName) throws IOException {
        Scanner input = new Scanner(new File(fileName));
        int numPOs = input.nextInt();
        PostOffice[] POarr = new PostOffice[numPOs];
        int i = 0, j = 0;
        int[] temparr = new int[5];
        String name = "";
        int transit_time = 0, postage = 0, capacity = 0, persuasion = 0, package_maxlength = 0;

        while(input.hasNext() || i == 5){

            if (input.hasNextInt()) {
                temparr[i] = input.nextInt();
                switch (i) {
                    case 0: transit_time = temparr[i];
                        break;
                    case 1: postage = temparr[i];
                        break;
                    case 2: capacity = temparr[i];
                        break;
                    case 3: persuasion = temparr[i];
                        break;
                    case 4: package_maxlength = temparr[i];
                        break;
                }
                i++;
            }
            else if (input.hasNext()){
                name = input.next();
            }

            if (i == 5) {
                POarr[j] = new PostOffice(name, transit_time, postage, capacity, persuasion, package_maxlength);
                j++;
                i = 0;
            }
        }
    }

    /*
    Initialize all wanted criminal list from the file wanted.txt
     */
    public static void initializeWantedList(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        sc.next();
        while(sc.hasNext()){
            PostOffice.addCriminal(sc.next());
        }
    }
}
