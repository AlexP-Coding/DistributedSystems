package pt.ulisboa.tecnico.classes.namingserver;

import java.util.concurrent.ConcurrentHashMap;

public class NamingServices {
    private final ConcurrentHashMap<String, ServiceEntry> serviceEntries = new ConcurrentHashMap<>();

    public NamingServices(){
        addServiceEntry("class");
    }

    public ConcurrentHashMap<String, ServiceEntry> getServiceEntries() {
        return serviceEntries;
    }

    public void addServiceEntry(String serviceName){
        serviceEntries.put(serviceName, new ServiceEntry(serviceName));
    }

    public void updateServiceEntry(String serviceName, ServerEntry serverEntry){
        serviceEntries.get(serviceName).updateEntryList(serverEntry);
    }
}
