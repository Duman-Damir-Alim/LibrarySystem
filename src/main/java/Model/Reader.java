package Model;

public class Reader implements User {
    private int reader_id;
    private String username;
    private String address;
    private String phone;
    private int borrowed_number;
    private String borrowed_name;
    private String password;

    public Reader(int reader_id, String username, String address, String phone, int borrowed_number, String borrowed_name, String password) {
        this.reader_id = reader_id;
        this.username = username;
        this.address = address;
        this.phone = phone;
        this.borrowed_number = borrowed_number;
        this.borrowed_name = borrowed_name;
        this.password = password;
    }

    public int getReader_id() {
        return reader_id;
    }

    public void setReader_id(int reader_id) {
        this.reader_id = reader_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBorrowed_number() {
        return borrowed_number;
    }

    public void setBorrowed_number(int borrowed_number) {
        this.borrowed_number = borrowed_number;
    }

    public String getBorrowed_name() {
        return borrowed_name;
    }

    public void setBorrowed_name(String borrowed_name) {
        this.borrowed_name = borrowed_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
