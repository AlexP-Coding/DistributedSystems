package pt.ulisboa.tecnico.classes.admin;

import io.grpc.StatusRuntimeException;
import pt.ulisboa.tecnico.classes.contract.ClassesDefinitions;
import pt.ulisboa.tecnico.classes.Stringify;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpRequest;
import pt.ulisboa.tecnico.classes.contract.admin.AdminClassServer.DumpResponse;

import java.util.Scanner;

public class Admin {

  private static final String DUMP_CMD = "dump";
  private static final String EXIT_CMD = "exit";

  public static void main(String[] args) {
    System.out.println(Admin.class.getSimpleName());
    final String host = "localhost";
    final int port = 5000;

    try (AdminFrontend frontend = new AdminFrontend(host, port); Scanner scanner = new Scanner(System.in)) {
      while (true) {
        System.out.printf("%n> ");
        String line = scanner.nextLine();
        // exit
        if (EXIT_CMD.equals(line)) {
          scanner.close();
          frontend.close();
          break;
          // dump
        } else if (DUMP_CMD.equals(line)) {
          DumpResponse response = frontend.dump(DumpRequest.getDefaultInstance());
          ClassesDefinitions.ResponseCode responseCode = response.getCode();
          if (responseCode.equals(ClassesDefinitions.ResponseCode.OK)) {
            ClassesDefinitions.ClassState state = response.getClassState();
            System.out.println(Stringify.format(state));
          } else {
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

