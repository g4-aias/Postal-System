
public class LETTER extends Mail {

    private String retrnAdr;

    LETTER(String n_localPOname, String n_recipient, String n_destination, String n_retrnAdr) {
        super(n_destination, n_recipient, n_localPOname);
        retrnAdr = n_retrnAdr;
    }

    public String getRetrnAdr() {
        return this.retrnAdr;
    }
}
