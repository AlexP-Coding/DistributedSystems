package pt.ulisboa.tecnico.classes.namingserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.naming.ClassServerNamingServer.*;
import pt.ulisboa.tecnico.classes.contract.naming.NamingServerServiceGrpc;

public class NamingServerServiceImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase{
    NamingServices namingServices = new NamingServices();

    public NamingServerServiceImpl(){}

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver){
        String serviceName = request.getServiceName();
        int port = request.getPort();
        String serverType = request.getQualifiersList(0);
        ServerEntry serverEntry = new ServerEntry(port, serverType);

        if(serviceName.equals("class")) {
            namingServices.updateServiceEntry(serviceName, serverEntry);
        }
    }
}
