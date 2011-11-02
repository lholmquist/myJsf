import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: lholmquist
 * Date: 11/1/11
 * Time: 8:23 AM
 */
@Stateless
public class TestTimer {

    @Inject
    Greeter greeter;

    @Schedule(hour ="9", minute = "17")
    public void execute()
    {
        System.out.println(greeter.getMessage());
    }




}
