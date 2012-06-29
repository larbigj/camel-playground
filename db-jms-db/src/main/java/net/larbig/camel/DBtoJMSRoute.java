package net.larbig.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.Main;

public class DBtoJMSRoute extends RouteBuilder {

	public static void main(String[] args) throws Exception {
		new Main().run(args);
	}

	public void configure() {
		from("quartz://timerName?cron=0/20 * * * * ?")
		.setBody(constant("select * from test_in"))
		.to("sql:select * from test_in?dataSourceRef=dbConnection")
		.split(body())
			//ich merk mir mal das result zum droppen
			.setProperty("ORIG_EXCH",body())
			.bean(SQLBean.class, "convertToJSON")
			.to("log:json_in")
			.to("hornetq:queue:testQueue2")
			//original wieder rein und weg damit
			.process(new Processor() {
				public void process(Exchange exch) throws Exception {
					exch.getOut().setBody(exch.getProperty("ORIG_EXCH"));
				}
			})
			.bean(SQLBean.class, "dropSQL")
			.to("sql:delete from test_in where col1=#?dataSourceRef=dbConnection");
	}
}
