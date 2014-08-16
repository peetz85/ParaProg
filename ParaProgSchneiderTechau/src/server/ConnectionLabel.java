package server;

/**
 * Created by Pascal on 16.08.2014.
 */
public class ConnectionLabel {
    private String serverName;
    private String serverPort;


    public ConnectionLabel(String serverName, String serverPort){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public String toString(){
        return serverName+serverPort;
    }

    public String getServerName(){
        return serverName;
    }

    public String getServerPort(){
        return serverPort;
    }

}
