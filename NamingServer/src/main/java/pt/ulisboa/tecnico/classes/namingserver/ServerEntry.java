package pt.ulisboa.tecnico.classes.namingserver;

public class ServerEntry {
    int hostPort;
    String serverType;

    public ServerEntry(int hostPort, String serverType){
        setHostPort(hostPort);
        setServerType(serverType);
    }

    public int getHostPort(){
        return this.hostPort;
    }

    public String getServerType(){
        return this.serverType;
    }

    public void setHostPort(int hostPort){
        this.hostPort = hostPort;
    }

    public void setServerType(String serverType){
        this.serverType = serverType;
    }

}
