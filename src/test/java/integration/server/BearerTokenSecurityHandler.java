package integration.server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.util.security.Constraint;

import static org.eclipse.jetty.util.security.Credential.getCredential;

public class BearerTokenSecurityHandler extends ConstraintSecurityHandler {
  private final UserStore userStore = new UserStore();

  public BearerTokenSecurityHandler(String context, String realm) {
    setAuthenticator(new BearerTokenAuthenticator());
    setRealmName("myrealm");
    addConstraintMapping(constraintMapping(context));
    setLoginService(loginService(realm));
  }

  private ConstraintMapping constraintMapping(String context) {
    ConstraintMapping constraintMapping = new ConstraintMapping();
    constraintMapping.setConstraint(constraint());
    constraintMapping.setPathSpec(context);
    return constraintMapping;
  }

  private Constraint constraint() {
    Constraint constraint = new Constraint();
    constraint.setName("BEARER");
    constraint.setRoles(new String[]{"user" });
    constraint.setAuthenticate(true);
    return constraint;
  }

  private HashLoginService loginService(String realm) {
    HashLoginService loginService = new HashLoginService();
    loginService.setUserStore(userStore);
    loginService.setName(realm);
    return loginService;
  }

  public void addUser(String username, String password) {
    userStore.addUser(username, getCredential(password), new String[]{"user"});
  }
}
