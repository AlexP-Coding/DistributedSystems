package pt.ulisboa.tecnico.classes.professor;

import io.grpc.StatusRuntimeException;
import pt.ulisboa.tecnico.classes.contract.professor.ProfessorClassServer;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ResponseCode;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions.ClassState;
import pt.ulisboa.tecnico.classes.Stringify;

import java.util.Scanner;

public class Professor {
  private static final String EXIT_CMD = "exit";
  private static final String OPEN_ENROLLMENTS_CMD = "openEnrollments";
  private static final String CLOSE_ENROLLMENTS_CMD = "closeEnrollments";
  private static final String LIST_CLASS_CMD = "list";
  private static final String CANCEL_ENROLLMENTS_CMD = "cancelEnrollment";

  public static void main(String[] args) {
    System.out.println(Professor.class.getSimpleName());

    final String host = "localhost";
    final int port = 5000;
    ProfessorFrontend frontend = new ProfessorFrontend(host, port);


    try(Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");


        String[] result = scanner.nextLine().split(" ");
        String cmd = result[0];

        // exit
        if (EXIT_CMD.equals(cmd)) {
          scanner.close();
          frontend.close();
          break;
        }
        else if (OPEN_ENROLLMENTS_CMD.equals((cmd))) {
          try {
            ProfessorClassServer.OpenEnrollmentsResponse response = frontend.openEnrollments(ProfessorClassServer.OpenEnrollmentsRequest.newBuilder().setCapacity(Integer.parseInt(result[1])).build());

            System.out.println(Stringify.format(response.getCode()));
          } catch (StatusRuntimeException e) {
            System.out.println("Caught exception with description:" + e.getStatus().getDescription());
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Have not got a capacity!");
          }
        }
        else if (CLOSE_ENROLLMENTS_CMD.equals((cmd))) {
          try {
            ProfessorClassServer.CloseEnrollmentsResponse response = frontend.closeEnrollments(ProfessorClassServer.CloseEnrollmentsRequest.newBuilder().build());

            System.out.println(Stringify.format(response.getCode()));
          } catch (StatusRuntimeException e) {
            System.out.println("Caught exception with description:" + e.getStatus().getDescription());
          }
        }
        else if (LIST_CLASS_CMD.equals(cmd)) {
          try {
            ProfessorClassServer.ListClassResponse response = frontend.listClass(ProfessorClassServer.ListClassRequest.getDefaultInstance());
            ResponseCode responseCode = response.getCode();
            if (responseCode.equals(ResponseCode.OK)) {
              ClassState state = response.getClassState();
              System.out.println(Stringify.format(state));
            } else {
              System.out.println(Stringify.format(responseCode));
            }
          } catch (StatusRuntimeException e) {
            System.out.println("Caught exception with description:" + e.getStatus().getDescription());
          }
        }
        else if (CANCEL_ENROLLMENTS_CMD.equals(cmd)) {
          try {
            ProfessorClassServer.CancelEnrollmentResponse response = frontend.cancelEnrollment(ProfessorClassServer.CancelEnrollmentRequest.newBuilder().setStudentId(result[1]).build());
            System.out.println(Stringify.format(response.getCode()));

          } catch (StatusRuntimeException e) {
            System.out.println("Caught exception with description:" + e.getStatus().getDescription());
          } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Have not got a id student!");
          }

        }

      }
    } catch (StatusRuntimeException e) {
      System.out.println("Caught exception with description: " +
              e.getStatus().getDescription());
    }
  }
}
