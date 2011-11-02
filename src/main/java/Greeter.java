import javax.ejb.Stateless;
import javax.inject.Named;

/**
 * Created by IntelliJ IDEA.
 * User: lholmquist
 * Date: 9/28/11
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Named
@Stateless
public class Greeter {

    public String getMessage()
    {
        return "Hello,  Person";
    }




}
