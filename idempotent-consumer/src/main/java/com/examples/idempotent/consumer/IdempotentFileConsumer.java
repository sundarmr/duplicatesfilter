package com.examples.idempotent.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;

import com.examples.helper.ArrayListAggregationStrategy;
import com.examples.helper.ConvertToMap;
import com.examples.model.Order;
import com.hazelcast.core.Hazelcast;

public class IdempotentFileConsumer extends RouteBuilder {

	static int MAX_RECORDS = 1000;
	static long BATCH_TIME_OUT = 3000;
	BasicDataSource dataSource = null;

	@Override
	public void configure() throws Exception {
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat();
		jaxbDataFormat.setContextPath(Order.class.getPackage().getName());
		jaxbDataFormat.setPartClass(Order.class.getCanonicalName());

		getContext().addComponent("sqlComponent", getComponent());
				
		getContext().disableJMX();
		
		from("file:///Users/smunirat/apps/myfile?noop=true").streamCaching()
				.routeId("splitandaggregate")
				.split()
				.tokenizeXML("order")
				.streaming()
				.executorService(getExecutor()).unmarshal(jaxbDataFormat)
				.process(new ConvertToMap())
				//Setting the key value on basis of which we will filter 
				//duplicates
				.setHeader("orderid", simple("${body[orderid]}"))
				//Declaring the idempotent consumer with the key
				.idempotentConsumer(header("orderid"))
				//Providing the HazelCast Repo and asking to skip the duplicates
				.messageIdRepository(getHazelRepo()).skipDuplicate(true)
				.aggregate(constant(true), new ArrayListAggregationStrategy())
				.completionPredicate(simple("${body.size} == 1000"))
				.completionTimeout(3000L).multicast()
				.parallelProcessing()
				.to("direct:endpoint1", "direct:endpoint2").end();
		from("direct:endpoint1").routeId("insertorder")
				.to("sqlComponent:{{sql.insertNewRecord}}?batch=true")
				//.log("Inserted ${header.CamelSqlUpdateCount} Order records")
				.end();
		from("direct:endpoint2").routeId("insertproduct").split().body()
				.setBody(simple("${body[products]}")).split().body()
				.parallelProcessing()
				.aggregate(constant(true), new ArrayListAggregationStrategy())
				.completionPredicate(simple("${body.size} == 1000"))
				.completionTimeout(3000L)
				.to("sqlComponent:{{sql.insertProductRecord}}?batch=true")
				//.log("Inserted ${header.CamelSqlUpdateCount} Product records")
				.end();

	}

	public BasicDataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.jdbc.Driver");
			dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mycodedump");
			dataSource.setUsername("root");
			dataSource.setPassword("Redhat01");
		}
		return dataSource;
	}
	
	@Bean
	public HazelcastIdempotentRepository getHazelRepo(){
		HazelcastIdempotentRepository rep = new HazelcastIdempotentRepository(Hazelcast.newHazelcastInstance(),"orderid");
		try {
			rep.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rep;
	}
		
		
	

	@Bean
	public SqlComponent getComponent() {
		SqlComponent component = new SqlComponent();
		component.setDataSource(getDataSource());
		return component;
	}

	@Bean
	public ExecutorService getExecutor() {
		return Executors.newFixedThreadPool(10);
	}
}
