package commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;
import org.crsh.command.InvocationContext;
import org.springframework.beans.factory.BeanFactory;

public class CreatePeriod extends BaseCommand {

    @Command
    @Usage("Creates a Period of the specified type in the specified dates.")
    public String main(InvocationContext context, @Usage("Application or Election") @Option(names = { "type" }) String type,
            @Usage("Start Date") @Option(names = { "start" }) String start,
            @Usage("End Date") @Option(names = { "end" }) String end) {
        BeanFactory bf = (BeanFactory) context.getAttributes().get("spring.beanfactory");
        if ((type != null && type != "Application" && type != "Election") || type == null) {
            return "Type must be Application or Election";
        }
        if (start == null || end == null) {
            return "Start and End Dates must follow the format dd/MM/yyyy";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate startDate = LocalDate.parse(start, dtf);
            LocalDate endDate = LocalDate.parse(end, dtf);
        } catch (DateTimeParseException dtpe) {
            return "Start and End Dates must follow the format dd/MM/yyyy";
        }

    }
}
