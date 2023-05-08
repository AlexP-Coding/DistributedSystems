package pt.ulisboa.tecnico.classes.classserver;

import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.logging.Logger;

import pt.ulisboa.tecnico.classes.contract.professor.ProfessorServiceGrpc;
import  pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;

public class ProfessorServiceImpl extends ProfessorServiceGrpc.ProfessorServiceImplBase {
    private boolean isActive;
    private DomainClassState classState;
    private static final Logger LOGGER = Logger.getLogger(ProfessorServiceImpl.class.getName());
    private boolean debug;

    public ProfessorServiceImpl(boolean isActive, DomainClassState classState, boolean debug) {
        setActive(isActive);
        setDomainClassState(classState);
        setDebug(debug);
        if (debug) {LOGGER.info("Created ProfessorServiceImpl.");}
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public void setDomainClassState(DomainClassState classState) {
        this.classState = classState;
    }
    public void setDebug(boolean debug) { this.debug = debug; }

    @Override
    public void openEnrollments(OpenEnrollmentsRequest request, StreamObserver<OpenEnrollmentsResponse> responseObserver) {
        ResponseCode code;
        int capacity = request.getCapacity();

        if(classState.getOpenEnrollments()){
            code = ResponseCode.ENROLLMENTS_ALREADY_OPENED;
        }
        else if (capacity <= classState.getEnrolled().size())
            code = ResponseCode.FULL_CLASS;
        else{
            code = ResponseCode.OK;
            synchronized(this.classState) {
                classState.setOpenEnrollments(true);
                classState.setCapacity(capacity);
            }
        }
        if(debug){LOGGER.info("Class enrollments opened with capacity:"+ classState.getCapacity()+ ". Code:" + code);}
        responseObserver.onNext(OpenEnrollmentsResponse.newBuilder().setCode(code).build());
        responseObserver.onCompleted();
        if(debug){LOGGER.info("OpenEnrollmentsResponse sent. Capacity:"+ classState.getCapacity() + ". Code:" + code);}

    }

    @Override
    public void closeEnrollments(CloseEnrollmentsRequest request, StreamObserver<CloseEnrollmentsResponse> responseObserver){
        ResponseCode code;
        if(!classState.getOpenEnrollments()){
            code = ResponseCode.ENROLLMENTS_ALREADY_CLOSED;
        }else{
            code = ResponseCode.OK;
            synchronized(this.classState) {
                classState.setOpenEnrollments(false);
            }
        }
        if(debug){LOGGER.info("Class enrollments closed at capacity:"+ classState.getCapacity()+ ". Code:" + code);}
        responseObserver.onNext(CloseEnrollmentsResponse.newBuilder().setCode(code).build());
        responseObserver.onCompleted();
        if(debug){LOGGER.info("OpenEnrollmentsResponse sent. Capacity:"+ classState.getCapacity() + ". Code:" + code);}
    }

    @Override
    public void listClass(ListClassRequest request, StreamObserver<ListClassResponse> responseObserver) {
        int capacity;
        boolean openEnrollments;
        ArrayList<ClassesDefinitions.Student> protoEnrolled;
        ArrayList<ClassesDefinitions.Student> protoDiscarded;
        synchronized (classState) {
            capacity = classState.getCapacity();
            openEnrollments = classState.getOpenEnrollments();
            protoEnrolled = classState.getProtoEnrolled();
            protoDiscarded = classState.getProtoDiscarded();
        }
        ClassState state = ClassState.newBuilder().setCapacity(capacity).setOpenEnrollments(openEnrollments).addAllEnrolled(protoEnrolled).addAllDiscarded(protoDiscarded).build();
        if (debug) LOGGER.info("ListClassResponse " + ResponseCode.OK + " ready to send");
        ListClassResponse response = ListClassResponse.newBuilder().setCode(ResponseCode.OK).setClassState(state).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        if (debug) LOGGER.info("ListClassResponse " + ResponseCode.OK + " sent successfully");
    }

    public void cancelEnrollment(CancelEnrollmentRequest request, StreamObserver<CancelEnrollmentResponse> responseObserver){
        CancelEnrollmentResponse response;
        ResponseCode code;
        int nrEnrolled = -1;
        int nrDiscarded = -1;

        synchronized(this.classState){
            if(!classState.isEnrolled(request.getStudentId())){
                code = ClassesDefinitions.ResponseCode.NON_EXISTING_STUDENT;
            } else{
                DomainStudent studentDiscarded = classState.getEnrolled().get(request.getStudentId());
                classState.getEnrolled().remove(request.getStudentId());
                if (!classState.isDiscarded(request.getStudentId()))
                    classState.getDiscarded().put(request.getStudentId(), studentDiscarded);
                code = ClassesDefinitions.ResponseCode.OK;
            }
            nrEnrolled = classState.getEnrolled().size();
            nrDiscarded = classState.getDiscarded().size();
        }
        response = CancelEnrollmentResponse.newBuilder().setCode(code).build();
        if (debug) LOGGER.info("#Enrolled: " + nrEnrolled + ". #Discarded: " + nrDiscarded);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        if(debug){LOGGER.info("Professor has sent message of canceled enrollment of student:" + request.getStudentId() + ", code:" + code);}
    }
}
