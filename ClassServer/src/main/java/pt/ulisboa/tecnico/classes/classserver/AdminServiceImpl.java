package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminServiceGrpc.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

import java.util.ArrayList;
import java.util.logging.Logger;

public class AdminServiceImpl extends AdminServiceImplBase {
    private DomainClassState classState;
    private boolean debug;
    private static final Logger LOGGER = Logger.getLogger(StudentServiceImpl.class.getName());


    public AdminServiceImpl(DomainClassState classState) {
        setDomainClassState(classState);
        setDebug(debug);
        if (debug) {LOGGER.info("Created StudentServiceImpl.");}
    }

    public void setDomainClassState(DomainClassState classState){this.classState = classState;}
    public void setDebug(boolean debug) { this.debug = debug; }

    @Override
    public void dump(DumpRequest request, StreamObserver<DumpResponse> responseObserver){
        ResponseCode code = ResponseCode.OK;
        int capacity = classState.getCapacity();
        boolean openEnrollments = classState.getOpenEnrollments();
        ArrayList<ClassesDefinitions.Student> protoEnrolled = classState.getProtoEnrolled();
        ArrayList<ClassesDefinitions.Student> protoDiscarded = classState.getProtoDiscarded();

        ClassesDefinitions.ClassState state = ClassesDefinitions.ClassState.newBuilder().setCapacity(capacity).setOpenEnrollments(openEnrollments).addAllEnrolled(protoEnrolled).addAllDiscarded(protoDiscarded).build();
        AdminClassServer.DumpResponse response = AdminClassServer.DumpResponse.newBuilder().setCode(code).setClassState(state).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        if (debug) LOGGER.info("#Dumped");
    }

}
