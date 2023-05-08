package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ClassServerFrontend {
    private DomainClassState classState;
    private boolean isActive;

    public ClassServerFrontend(DomainClassState classState, boolean isActive) {
        this.classState = classState;
        this.isActive = isActive;
    }

    void begin(String address, int port, String serverType, boolean debug) {
        final BindableService serverImpl = new ClassServerServiceImpl(this.isActive, this.classState);
        final BindableService studentImpl = new StudentServiceImpl(this.isActive, this.classState, debug);
        final BindableService professorImpl = new ProfessorServiceImpl(this.isActive, this.classState, debug);
        final BindableService adminImpl = new AdminServiceImpl(this.classState);

        // Create a new server to listen on port.
        Server server = ServerBuilder.forPort(port).addService(serverImpl).addService(studentImpl).addService(professorImpl).addService(adminImpl).build();
        try{
            // Server threads are running in the background.
            server.start();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        System.out.println("Server started");

        try{
            // Do not exit the main thread. Wait until server is terminated.
            server.awaitTermination();
        }
        catch (java.lang.InterruptedException e) {
            e.printStackTrace();
        }
    }
}
