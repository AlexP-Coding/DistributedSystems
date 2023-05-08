package pt.ulisboa.tecnico.classes.classserver;


public class DomainStudent {
  private String studentId;
  private String studentName;

  public DomainStudent(String studentId, String studentName) {
    setStudentId(studentId);
    setStudentName(studentName);
  }

  public void setStudentId(String studentId) { this.studentId = studentId; }
  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }

  public String getStudentId() { return studentId; }

  public String getStudentName() { return studentName; }

  @Override
  public String toString() {
    return "Id:" + studentId + ", Name:" + studentName;
  }

}