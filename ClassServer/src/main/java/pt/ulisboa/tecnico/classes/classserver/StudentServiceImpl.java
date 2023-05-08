package pt.ulisboa.tecnico.classes.classserver;

import static io.grpc.Status.INVALID_ARGUMENT;
import java.util.logging.Logger;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.*;
import pt.ulisboa.tecnico.classes.contract.student.StudentServiceGrpc;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.*;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;

public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase{
    private boolean isActive;
    private DomainClassState classState;
    private boolean debug;
    private static final Logger LOGGER = Logger.getLogger(StudentServiceImpl.class.getName());

    public StudentServiceImpl(boolean isActive, DomainClassState classState, boolean debug) {
        setActive(isActive);
        setDomainClassState(classState);
        setDebug(debug);
        if (debug) {LOGGER.info("Created StudentServiceImpl.");}
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public void setDomainClassState(DomainClassState classState) {
        this.classState = classState;
    }
    public void setDebug(boolean debug) { this.debug = debug; }

    @Override
    public void listClass(ListClassRequest request, StreamObserver<ListClassResponse> responseObserver) {
        ResponseCode code = ResponseCode.OK;
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
        if (debug) LOGGER.info("ListClassResponse " + code + " ready to send");
        ListClassResponse response = ListClassResponse.newBuilder().setCode(code).setClassState(state).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

        if (debug) LOGGER.info("ListClassResponse " + code + " sent successfully");
    }

    @Override
    public void enroll(EnrollRequest request, StreamObserver<EnrollResponse> responseObserver) {
        Student protoStudent = request.getStudent();
        String studentId = protoStudent.getStudentId();
        String studentName = protoStudent.getStudentName();
        int nrEnrolled = -1;
        int nrDiscarded = -1;

        if (!classState.isValidStudentId(studentId))
            responseObserver.onError(INVALID_ARGUMENT.withDescription("Input " + studentId + "is not a valid student id!").asRuntimeException());
        else if (!classState.isValidStudentName(studentName))
            responseObserver.onError(INVALID_ARGUMENT.withDescription("Input " + studentName + "is not a valid student name!").asRuntimeException());
        else {
            ResponseCode code;
            if (!isActive)
                code = ResponseCode.INACTIVE_SERVER;
            else
                synchronized (classState) {
                    code = classState.enroll(studentId, studentName);
                    nrEnrolled = classState.getEnrolled().size();
                    nrDiscarded = classState.getDiscarded().size();
                }

            if (debug) LOGGER.info("#Enrolled: " + nrEnrolled + ". #Discarded: " + nrDiscarded);

            EnrollResponse response = EnrollResponse.newBuilder().setCode(code).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            if (debug) LOGGER.info("EnrollResponse " + code + " sent successfully for " + studentId + ": " + studentName);
        }
    }

}