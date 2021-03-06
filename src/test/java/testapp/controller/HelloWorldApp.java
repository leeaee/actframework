package testapp.controller;

import act.app.ActionContext;
import act.boot.app.RunApp;
import org.osgl.http.H;
import org.osgl.mvc.annotation.Before;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.Result;

import static act.controller.Controller.Util.render;
import static act.controller.Controller.Util.text;

/**
 * The simple hello world app.
 * <p>Run this app, try to update some of the code, then
 * press F5 in the browser to watch the immediate change
 * in the browser!</p>
 */
public class HelloWorldApp {

    ActionContext context;

    public HelloWorldApp() {
    }

    @Before
    public void mockFormat(String fmt) {
        if ("json".equals(fmt)) {
            context.accept(H.Format.JSON);
        }
        context.session().put("foo", "bar");
    }

    @GetAction("/hello")
    public String sayHello() {
        return "Hello Ying!";
    }

    @GetAction("/bye")
    public void byePlayAndSpring() {
        text("bye Play and Spring!");
    }

    @GetAction("/greeting")
    public Result greeting(String who, int age) {
        ActionContext ctx = ActionContext.current();
        ctx.renderArg("who", who);
        return render(who, age);
    }

    @GetAction("/thank")
    public static String thankYou() {
        return "thank you!";
    }

    public static void main(String[] args) throws Exception {
        RunApp.start(HelloWorldApp.class);
    }

}
