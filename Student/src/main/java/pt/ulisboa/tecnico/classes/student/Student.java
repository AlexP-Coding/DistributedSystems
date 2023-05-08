package pt.ulisboa.tecnico.classes.student;

import java.util.Scanner;
import io.grpc.StatusRuntimeException;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.ListClassRequest;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.ListClassResponse;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.EnrollRequest;
import pt.ulisboa.tecnico.classes.contract.student.StudentClassServer.EnrollResponse;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.Stringify;


public class Student {
  private static final String EXIT_CMD = "exit";
  private static final String ENROLL_CMD = "enroll";
  private static final String LIST_CMD = "list";


  public static void main(String[] args) {
    final String host = "localhost";
    final int port = 5000;
    String studentId = args[0];
    String studentName = args[1];

 for (int i=2; i<args.length; i++) {
      studentName = studentName.concat(" ");
      studentName = studentName.concat(args[i]);
    }

    try (StudentFrontend frontend = new StudentFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");
        String line = scanner.nextLine();

        // exit
        if (EXIT_CMD.equals(line)) {
          scanner.close();
          frontend.close();
          break;
        }

        // enroll
        else if (ENROLL_CMD.equals(line)) {
          ClassesDefinitions.Student student = ClassesDefinitions.Student.newBuilder().setStudentId(studentId).setStudentName(studentName).build();
          EnrollRequest request = EnrollRequest.newBuilder().setStudent(student).build();
          EnrollResponse response = frontend.enroll(request);
          ResponseCode responseCode = response.getCode();
          System.out.println(Stringify.format(responseCode));
        }

        // list
        else if (LIST_CMD.equals(line)) {
          ListClassResponse response = frontend.listClass(ListClassRequest.getDefaultInstance());
          ResponseCode responseCode = response.getCode();
          if (responseCode.equals(ResponseCode.OK)) {
            ClassState state = response.getClassState();
            System.out.println(Stringify.format(state));
          }
          else {
            System.out.println(Stringify.format(responseCode));
          }
        }
      }
    } catch (StatusRuntimeException e) {
      System.out.println("Caught exception with description: " +
              e.getStatus().getDescription());
    }
  }
}
