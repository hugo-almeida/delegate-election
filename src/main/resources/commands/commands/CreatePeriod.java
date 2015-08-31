package commands;

import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;
import org.crsh.command.InvocationContext;
import org.springframework.beans.factory.BeanFactory;

import endpoint.Controller;

public class CreatePeriod extends BaseCommand {

    @Command
    @Usage("Creates a Period of the specified type in the specified dates.")
    public String main(InvocationContext context, @Usage("Stuff") @Option(names = { "f", "type" }) String type) {
        BeanFactory bf = (BeanFactory) context.getAttributes().get("spring.beanfactory");
        Controller tc = bf.getBean(Controller.class);
        return type;
    }
}
