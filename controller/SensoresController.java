package controller;

import java.util.List;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
//import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import dao.SensoresDao;
import modelo.Sensores;

@Resource
public class SensoresController {

	private final SensoresDao dao;
	private final Result result;
	
	public SensoresController(SensoresDao dao, Result result) {
		this.dao = dao;
		this.result = result;
	}
	
	@Post @Path("/admin/sensores")
	public void adiciona(Sensores sensores) {
		dao.create(sensores);
		LogController.logar("Tipo sensor " + sensores.getId() + " inserido");
		result.redirectTo(this).lista();
	}
	
	@Get @Path("/admin/sensores/{id}")
	public Sensores edita(Long id) {
		return dao.load(id);
	}
	
	@Put @Path("/sensor/{sensores.id}")
	public void altera(Sensores sensor){
		dao.update(sensor);
		LogController.logar("sensor " + sensor.getId() + " atualizado");
		result.redirectTo(this).lista();
	}
	
	@Delete @Path("/admin/sensores/{id}")
	public void remove(Long id){
		Sensores sensores = dao.load(id);
		dao.delete(sensores);
		LogController.logar("Tipo sensor " + sensores.getId() + " removido");
		result.redirectTo(this).lista();
	}
	
	@Get @Path("/admin/sensores")
	public List<Sensores> lista(){
		return dao.listaTudo();
	}
	
	@Get @Path("/admin/sensores/novo")
	public void formulario(){
	}
}
