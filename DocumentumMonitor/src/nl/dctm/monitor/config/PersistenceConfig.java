package nl.dctm.monitor.config;


@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:jdbc.properties" })
@ComponentScan( {"nl.dctm.monitor"} )
public class PersistenceConfig {

}
