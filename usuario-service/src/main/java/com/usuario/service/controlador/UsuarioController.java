package com.usuario.service.controlador;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usuario.service.entidades.Usuario;
import com.usuario.service.modelos.Carro;
import com.usuario.service.modelos.Moto;
import com.usuario.service.servicio.UsuarioService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioService.getAll();
		if (usuarios.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerUsuario(@PathVariable("id") int id) {
		Usuario usuario = usuarioService.getUsuarioById(id);
		if (Objects.isNull(usuario)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(usuario);
	}

	@PostMapping
	public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
		Usuario result = usuarioService.save(usuario);
		return ResponseEntity.ok(result);
	}

	@CircuitBreaker(name="carrosCB", fallbackMethod = "fallBackGetCarros")
	@GetMapping("/carros/{usuarioId}")
	public ResponseEntity<List<Carro>> getCarros(@PathVariable("usuarioId") int usuarioId) {
		Usuario usuario = usuarioService.getUsuarioById(usuarioId);
		if (Objects.isNull(usuario)) {
			return ResponseEntity.notFound().build();
		}
		List<Carro> carros = usuarioService.getCarros(usuario.getId());
		return ResponseEntity.ok(carros);
	}

	@CircuitBreaker(name="motosCB", fallbackMethod = "fallBackGetMotos")
	@GetMapping("/motos/{usuarioId}")
	public ResponseEntity<List<Moto>> getMotos(@PathVariable("usuarioId") int usuarioId) {
		Usuario usuario = usuarioService.getUsuarioById(usuarioId);
		if (Objects.isNull(usuario)) {
			return ResponseEntity.notFound().build();
		}
		List<Moto> motos = usuarioService.getMotos(usuario.getId());
		return ResponseEntity.ok(motos);
	}

	@CircuitBreaker(name="carrosCB", fallbackMethod = "fallBackSaveCarro")
	@PostMapping("/carro/{usuarioId}")
	public ResponseEntity<Carro> guardarCarro(@PathVariable("usuarioId") int usuarioId, @RequestBody Carro carro) {
		Carro nuevo = usuarioService.saveCarro(usuarioId, carro);
		return ResponseEntity.ok(nuevo);
	}
	
	@CircuitBreaker(name="motosCB", fallbackMethod = "fallBackGetMoto")
	@PostMapping("/moto/{usuarioId}")
	public ResponseEntity<Moto> guardarMoto(@PathVariable("usuarioId") int usuarioId, @RequestBody Moto moto) {
		Moto nuevo = usuarioService.saveMoto(usuarioId, moto);
		return ResponseEntity.ok(nuevo);
	}
	
	@CircuitBreaker(name="todosCB", fallbackMethod = "fallBackGetTodos")
	@GetMapping("/todos/{usuarioId}")
	public ResponseEntity<Map<String, Object>> listarTodosLosVehiculos(@PathVariable("usuarioId") int usuarioId){
		Map<String, Object> result = usuarioService.getUsuarioYVehiculos(usuarioId);
		return ResponseEntity.ok(result);
		
	}
	
	private ResponseEntity<List<Carro>> fallBackGetCarros(@PathVariable("usuarioId") int id, RuntimeException exception){
		return new ResponseEntity("El usuario: "+id+" tiene los carros en el taller", HttpStatus.OK);
	}
	
	private ResponseEntity<Carro> fallBackSaveCarro(@PathVariable("usuarioId") int id, @RequestBody Carro carro, RuntimeException exception){
		return new ResponseEntity("El usuario: "+id+" no tienen dinero para los carros", HttpStatus.OK);
	}
	
	
	private ResponseEntity<List<Moto>> fallBackGetMotos(@PathVariable("usuarioId") int id, RuntimeException exception){
		return new ResponseEntity("El usuario: "+id+" tiene las motos en el taller", HttpStatus.OK);
	}
	
	private ResponseEntity<Moto> fallBackSaveMoto(@PathVariable("usuarioId") int id, @RequestBody Moto moto, RuntimeException exception){
		return new ResponseEntity("El usuario: "+id+" no tienen dinero para las motos", HttpStatus.OK);
	}
	
	private ResponseEntity<Map<String, Object>> fallBackGetTodos(@PathVariable("usuarioId") int id, RuntimeException exception){
		return new ResponseEntity("El usuario: "+id+" tiene los vehículos en el taller", HttpStatus.OK);
	}

}
