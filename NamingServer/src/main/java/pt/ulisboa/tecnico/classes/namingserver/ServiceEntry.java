package pt.ulisboa.tecnico.classes.namingserver;

import java.util.List;
import java.util.ArrayList;

public class ServiceEntry {
    String serviceName;
    List<ServerEntry> listServerEntry = new ArrayList<>();

    public ServiceEntry(String serviceName){
        setServiceName(serviceName);
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }

    public String getServiceName(){
        return this.serviceName;
    }

    public List<ServerEntry> getListServerEntry(){
        return this.listServerEntry;
    }

    public void updateEntryList(ServerEntry serverEntry){
        this.listServerEntry.add(serverEntry);
    }
}
