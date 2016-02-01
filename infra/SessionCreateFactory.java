package infra;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@SuppressWarnings("deprecation")
@Component
@ApplicationScoped
public class SessionCreateFactory implements ComponentFactory<SessionFactory> {
	
	private SessionFactory factory;
	
	@PostConstruct
	public void open() {
		AnnotationConfiguration configuration =
				new AnnotationConfiguration();
		configuration.configure();
		
		this.factory = configuration.buildSessionFactory();
	}
	
	public SessionFactory getInstance() {
		return this.factory;
	}
	
	@PreDestroy
	public void close(){
		this.factory.close();
	}
}
