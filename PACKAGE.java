
public class PACKAGE extends Mail {
    private int currency;
    private int length;

    PACKAGE(String n_localPOname, String n_recipient, String n_destination, int n_currency, int n_length) {
        super(n_destination, n_recipient, n_localPOname);
        currency = n_currency;
        length = n_length;
    }

    public int getCurrency() {
        return this.currency;
    }

    public int getLength() {
        return this.length;
    }
}
