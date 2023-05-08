package pt.ulisboa.tecnico.classes.classserver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;

public class DomainClassState {
    private int capacity = 0;
    private boolean openEnrollments = false;
    private final ConcurrentHashMap<String, DomainStudent> enrolled = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, DomainStudent> discarded = new ConcurrentHashMap<>();

    public DomainClassState() {

    }

    public int getCapacity() { return capacity; }
    public boolean getOpenEnrollments() { return openEnrollments; }
    public ConcurrentHashMap<String, DomainStudent> getEnrolled() { return enrolled; }
    public ConcurrentHashMap<String, DomainStudent> getDiscarded() { return discarded;}

    public synchronized void setCapacity(int capacity) { this.capacity = capacity; }

    public synchronized void setOpenEnrollments(boolean openEnrollments) { this.openEnrollments = openEnrollments; }


    public ResponseCode enroll(String studentId, String studentName) {
        if (!this.getOpenEnrollments()) {
            return ResponseCode.ENROLLMENTS_ALREADY_CLOSED;
        }
        else if (this.isEnrolled(studentId)) {
            return ResponseCode.STUDENT_ALREADY_ENROLLED;
        }
        else if (this.getEnrolled().size() >= this.getCapacity()) {
            return ResponseCode.FULL_CLASS;
        }
        else {
            DomainStudent student;
            if (isDiscarded(studentId)) {
                student = this.getDiscarded().get(studentId);
                this.getDiscarded().remove(studentId);
            }
            else
                student = new DomainStudent(studentId, studentName);

            this.getEnrolled().put(student.getStudentId(), student);


            return ResponseCode.OK;
        }
    }


    public boolean isValidStudentId(String studentId) {
        if (studentId == null) return false;
        if (studentId.length() != 9) return false;

        String alunoId = studentId.substring(0,5);
        if (!alunoId.equals("aluno")) return false;

        String nrId = studentId.substring(5);
        try { Integer.parseInt(nrId); } catch (NumberFormatException e) { return false;}

        return true;
    }

    public boolean isValidStudentName(String studentName) {
        if (studentName == null) return false;
        else return studentName.length() >= 3 && studentName.length() <= 30;
    }

    public boolean isEnrolled(String studentId) { return enrolled.containsKey(studentId); }
    public boolean isDiscarded(String studentId) { return discarded.containsKey(studentId); }

    public ArrayList<ClassesDefinitions.Student> getProtoEnrolled() {
        ArrayList<ClassesDefinitions.Student> protoEnrolled = new ArrayList<>();
        fillProtoStudentList(protoEnrolled, enrolled);
        return protoEnrolled;
    }

    public ArrayList<ClassesDefinitions.Student> getProtoDiscarded() {
        ArrayList<ClassesDefinitions.Student> protoDiscarded = new ArrayList<>();
        fillProtoStudentList(protoDiscarded, discarded);
        return protoDiscarded;
    }

    private void fillProtoStudentList(ArrayList<ClassesDefinitions.Student> protoStudents, ConcurrentHashMap<String, DomainStudent> domainStudents) {
        for (DomainStudent student : domainStudents.values()) {
            String studentId = student.getStudentId();
            String studentName = student.getStudentName();
            ClassesDefinitions.Student protoStudent = ClassesDefinitions.Student.newBuilder().setStudentId(studentId).setStudentName(studentName).build();
            protoStudents.add(protoStudent);
        }
    }

}
