package model;

public class ClientData {

    private Long clientId;
    private String login;
    private String email;
    private String password;
    private String telephoneNumber;

    public ClientData(String log, String pass, String em, String tn) {
        this.login = log;
        this.password = pass;
        this.email = em;
        this.telephoneNumber = tn;
    }

    public ClientData() {

    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void clearValues() {
        this.email = " ";
        this.password = " ";
        this.telephoneNumber = null;
    }
    }
