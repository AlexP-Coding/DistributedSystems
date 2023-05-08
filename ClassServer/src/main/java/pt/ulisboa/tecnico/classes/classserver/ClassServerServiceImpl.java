package pt.ulisboa.tecnico.classes.classserver;

import pt.ulisboa.tecnico.classes.contract.classserver.ClassServerServiceGrpc;


public class ClassServerServiceImpl extends ClassServerServiceGrpc.ClassServerServiceImplBase{
    private boolean isActive;
    private DomainClassState classState;

    public ClassServerServiceImpl(boolean isActive, DomainClassState classState) {
        setActive(isActive);
        setDomainClassState(classState);
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setDomainClassState(DomainClassState classState) {
        this.classState = classState;
    }

}
