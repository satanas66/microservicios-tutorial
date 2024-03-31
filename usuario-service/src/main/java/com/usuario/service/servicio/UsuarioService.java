package com.usuario.service.servicio;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.feignclients.CarroFeignClient;
import com.usuario.service.feignclients.MotoFeignClient;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.repositorio.UsuarioRepository;


@Service
public class UsuarioService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CarroFeignClient carroFeignClient;
	
	@Autowired
	private MotoFeignClient motoFeignClient;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public List<Carro> getCarros(int usuarioId){
		List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/"+usuarioId, List.class);
		return carros;
	}
	
	public List<Moto> getMotos(int usuarioId){
		List<Moto> moto= restTemplate.getForObject("http://localhost:8003/moto/usuario/"+usuarioId, List.class);
		return moto;
	}
	
	public List<Usuario> getAll(){
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuarioById(int id) {
		return usuarioRepository.findById(id).orElse(null);
	}

	public Usuario save(Usuario usuario) {
		Usuario nuevo = usuarioRepository.save(usuario);
		return nuevo;
	}
	
	public Carro saveCarro(int usuarioId, Carro carro) {
		carro.setUsuarioId(usuarioId);
		Carro nuevo = carroFeignClient.save(carro);
		return nuevo;
	}
	
	public Moto saveMoto(int usuarioId, Moto moto) {
		moto.setUsuarioId(usuarioId);
		Moto nuevo = motoFeignClient.save(moto);
		return nuevo;
	}
	
	public Map<String, Object> getUsuarioYVehiculos(int usaurioId){
		Map<String, Object> result = new LinkedHashMap<>();
		Usuario usuario = usuarioRepository.findById(usaurioId).orElse(null);
		
		if(Objects.isNull(usuario)) {
			result.put("Mensaje", "El usuario no existe");
			return result;
		}
		
		result.put("Usuario", usuario);
		result.put("Carros", "El usuario no tiene carros");
		result.put("Motos", "El usuario no tiene motos");
		
		List<Carro> carros = carroFeignClient.getCarros(usaurioId);	
		if(!carros.isEmpty()) {
			result.put("Carros", carros);
		}
		List<Moto> motos = motoFeignClient.getMotos(usaurioId);		
		if(!motos.isEmpty()) {
			result.put("Motos", motos);
		}
		
		return result;				
	}
}
