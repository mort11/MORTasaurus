package org.mort11.ee;

import edu.wpi.first.wpilibj.Solenoid;
import org.mort11.util.Constants;

/**
 * End Effector: Minibot Deployment Mechanism
 * @author MORT
 */
public class Deployment {

    private Solenoid deploy;
    private Solenoid retract;

    /**
     * Create a new minibot Deployment.
     * @param solenoidPort The port the Deployment Solenoid is on.
     */
    public Deployment() {
        deploy = new Solenoid(Constants.Deployment.DEPLOY_CHANNEL);
        retract = new Solenoid(Constants.Deployment.RETRACT_CHANNEL);

//        System.out.println("construct deploy: " + deploy.get() + " retract: " + retract.get());
        deploy.set(false);
        retract.set(true);
    }

    /**
     * Deploy the minibot!
     */
    public void deploy() {
//        System.out.println("deploy deploy: " + deploy.get() + " retract: " + retract.get());
        retract.set(false);
        deploy.set(true);
    }

    /**
     * Brings the deployment mechanism back in.
     */
    public void retract() {
//        System.out.println("disengage deploy: " + deploy.get() + " retract: " + retract.get());
        deploy.set(false);
        retract.set(true);
    }
}
