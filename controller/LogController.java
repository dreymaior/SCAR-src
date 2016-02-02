package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
public class LogController {
	
	public static void logar(String message){
		String className = new Exception().getStackTrace()[1].getClassName();		
		Logger logger = LoggerFactory.getLogger(className);
		
		logger.info(message);
	}
	
	@Path("/log")
	@Get
	public void teste(){
	}	
}
