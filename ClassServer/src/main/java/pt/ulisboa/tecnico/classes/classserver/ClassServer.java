package pt.ulisboa.tecnico.classes.classserver;


public class ClassServer {

  private boolean isActive;
  private DomainClassState classState;

  public ClassServer() {
    setActive(true);
    setDomainClassState(new DomainClassState());
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  public void setDomainClassState(DomainClassState classState) {
    this.classState = classState;
  }

  public static void main(String[] args) {
    ClassServer classServer = new ClassServer();
    ClassServerFrontend frontend = new ClassServerFrontend(classServer.classState, classServer.isActive);
    System.out.println(ClassServer.class.getSimpleName());

    if (args.length >= 1  && args[0].equals("-debug"))
      frontend.begin("localhost", 5000, "P", true);
    else
      frontend.begin("localhost", 5000, "P", false);
  }
}