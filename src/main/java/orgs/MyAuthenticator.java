package orgs;

import org.jboss.logging.Logger;
import org.jboss.seam.security.Authenticator;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lholmquist
 * Date: 10/17/11
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Named
@RequestScoped
public class MyAuthenticator extends BaseAuthenticator implements Authenticator {

    private static final Logger log = Logger.getLogger(MyAuthenticator.class);



  @Inject
  Identity identity;
    @Inject
    Credentials credentials;
    @Inject
    BeanManager manager;

    private Subject subject;

    private String jaasConfigName = null;

    public MyAuthenticator() {
        subject = new Subject();
    }


    public void authenticate() {
        if (getJaasConfigName() == null) {
            throw new IllegalStateException("jaasConfigName cannot be null.  Please set it to a valid JAAS configuration name.");
        }

        try {

            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

            System.out.println(request.getUserPrincipal());
            System.out.println(request.getRemoteUser());

            getLoginContext().login();
            setStatus(AuthenticationStatus.SUCCESS);
            setUser(new SimpleUser(credentials.getUsername()));
        } catch (LoginException e) {
            setStatus(AuthenticationStatus.FAILURE);
            log.error("JAAS authentication failed", e);
        }
    }

    protected LoginContext getLoginContext() throws LoginException {
        return new LoginContext(getJaasConfigName(), subject,
                createCallbackHandler());
    }

    /**
     * Creates a callback handler that can handle a standard username/password
     * callback, using the credentials username and password properties
     */
    public CallbackHandler createCallbackHandler() {
        return new CallbackHandler() {
            public void handle(Callback[] callbacks)
                    throws IOException, UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                    if (callbacks[i] instanceof NameCallback) {
                        ((NameCallback) callbacks[i]).setName(credentials.getUsername());
                    } else if (callbacks[i] instanceof PasswordCallback) {
                        if (credentials.getCredential() instanceof PasswordCredential) {
                            PasswordCredential credential = (PasswordCredential) credentials.getCredential();
                            ((PasswordCallback) callbacks[i]).setPassword(credential.getValue() != null ?
                                    credential.getValue().toCharArray() : null);
                        }
                    } else {
                        log.warn("Unsupported callback " + callbacks[i]);
                    }
                }
            }
        };
    }

    public String getJaasConfigName() {
        return jaasConfigName;
    }

    public void setJaasConfigName(String jaasConfigName) {
        this.jaasConfigName = jaasConfigName;
    }

    public void postAuthenticate() {

        System.out.println("Post Authenticate");

    }
}
