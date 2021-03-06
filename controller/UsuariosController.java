package controller;

import java.util.List;

import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.ValidationMessage;
//import br.com.caelum.vraptor.validator.Validations;
import dao.UsuarioDao;
import modelo.Usuario;

@Resource
public class UsuariosController {
	
	private final UsuariosWebController usuarioWeb;
	
	private UsuarioDao dao;
	private Result result;
	private Validator validator;
	
	
	public UsuariosController(UsuarioDao dao, Result result, Validator validator, UsuariosWebController usuarioWeb) {
		this.dao = dao;
		this.result = result;
		this.validator = validator;
		this.usuarioWeb = usuarioWeb;
	}
	
	static public boolean isValidCpf (String strCpf )
	   {
	      int     d1, d2;
	      int     digito1, digito2, resto;
	      int     digitoCPF;
	      String  nDigResult;
	      	      
	      d1 = d2 = 0;
	      digito1 = digito2 = resto = 0;

	      for (int nCount = 1; nCount < strCpf.length() -1; nCount++)
	      {
	         digitoCPF = Integer.valueOf (strCpf.substring(nCount -1, nCount)).intValue();

	         //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
	         d1 = d1 + ( 11 - nCount ) * digitoCPF;

	         //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
	         d2 = d2 + ( 12 - nCount ) * digitoCPF;
	      };

	      //Primeiro resto da divis�o por 11.
	      resto = (d1 % 11);

	      //Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.
	      if (resto < 2)
	         digito1 = 0;
	      else
	         digito1 = 11 - resto;

	      d2 += 2 * digito1;

	      //Segundo resto da divis�o por 11.
	      resto = (d2 % 11);

	      //Se o resultado for 0 ou 1 o digito � 0 caso contr�rio o digito � 11 menos o resultado anterior.
	      if (resto < 2)
	         digito2 = 0;
	      else
	         digito2 = 11 - resto;

	      //Digito verificador do CPF que est� sendo validado.
	      String nDigVerific = strCpf.substring (strCpf.length()-2, strCpf.length());

	      //Concatenando o primeiro resto com o segundo.
	      nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

	      //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
	      return nDigVerific.equals(nDigResult);
	   }
	
	@Path("/usuarios")
	@Post
	public void adiciona(Usuario usuario){
		
		if (usuario.getNome() == null || usuario.getNome().length() < 3){
			validator.add(new ValidationMessage(
					"Nome e obrigatorio e precisa ter mais de 3 letras",
					"usuario.nome"));
		}
		
		if (usuario.getSobrenome() == null || usuario.getSobrenome().length() < 3){
			validator.add(new ValidationMessage(
					"Sobrenome e obrigatorio e precisa ter mais de 3 letras",
					"usuario.sobrenome"));
		}
		
		if (dao.existeUsuario(usuario)) {
			validator.add(new ValidationMessage(
					"Login ja existe",
					"usuario.login"));
		}
		
		if (usuario.getCpfCnpj() == null || usuario.getCpfCnpj().length() != 11 || isValidCpf(usuario.getCpfCnpj()) == false){
			validator.add(new ValidationMessage(
					"CPF/CNPJ e obrigatorio e precisa ser valido",
					"usuario.cpfCnpj"));
		}
		
		if (usuario.getTelefone() == null || usuario.getTelefone().length() < 10){
			validator.add(new ValidationMessage(
					"Telefone e obrigatorio e ter no maximo 8 caracteres 4499999999",
					"usuario.telefone"));
		}
		
		if (usuario.getEndereco() == null || usuario.getEndereco().length() < 10){
			validator.add(new ValidationMessage(
					"Endereco e obrigatorio e ter pelo menos 10 caracteres",
					"usuario.endereco"));
		}
		
		validator.onErrorUsePageOf(UsuariosController.class).formulario();
		
		if (dao.existeUsuario(usuario)){
			validator.add(new ValidationMessage("Login já existe", "usuario.login"));
		}
		
		validator.onErrorUsePageOf(UsuariosController.class).formulario();
		
		dao.create(usuario);
		LogController.logar("usuario " + usuario.getLogin() + " inserido");		
		
		result.redirectTo(this).lista();
	}
	
	@Path("/usuarios/{login}")
	@Get
	public Usuario edita(String login) {
		return dao.load(login);
	}
	
	@Path("/usuarios/{usuario.login}")
	@Put
	public void altera(Usuario usuario) {
		dao.update(usuario);
		LogController.logar("usuario " + usuario.getLogin() + " alterado");
		result.redirectTo(this).lista();
	}
	
	@Path("/usuarios/{login}")
	@Delete
	public void remove(String login){
		Usuario usuario = dao.load(login);
		dao.delete(usuario);
		LogController.logar("usuario " + usuario.getLogin() + " removido");
		result.redirectTo(UsuariosController.class).lista();
	}

	@Path("/usuarios")
	@Get
	public List<Usuario> lista() {
		return dao.listaTudo();
	}
	
	@Path("/usuarios/novo/usuario")
	@Get
	public void formulario(){
	}
	
	@Path("/login")
	@Get
	public void login(){
	}	
	
	@Path("/login")
	@Post
	public void login(Usuario usuario){
		Usuario carregado = dao.carrega(usuario);
		if (carregado == null) {
			validator.add(new ValidationMessage("Login e/ou senha inválidos", "usuario.login"));
		}
		validator.onErrorUsePageOf(UsuariosController.class).login();
	}
	
	@Path("/logout")
	public void logout() {
		usuarioWeb.logout();
		result.redirectTo(Listener.class).index();
	}
	
}
