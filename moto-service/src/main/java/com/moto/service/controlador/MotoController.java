package com.moto.service.controlador;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moto.service.entidades.Moto;
import com.moto.service.servicio.MotoService;


@RestController
@RequestMapping("/moto")
public class MotoController {

	@Autowired	
	private MotoService motoService;
	
	@GetMapping
	public ResponseEntity<List<Moto>> listarCarros(){
		List<Moto> motos = motoService.getAll();
		if(motos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(motos);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Moto> obtenerMoto(@PathVariable("id") int id){
		Moto moto = motoService.getMotoById(id);
		if(Objects.isNull(moto)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(moto);
	}
	
	@PostMapping
	public ResponseEntity<Moto> guardarMoto(@RequestBody Moto carro){
		Moto nuevo = motoService.save(carro);
		return ResponseEntity.ok(nuevo);
	}
	
	@GetMapping("/usuario/{usuarioId}")
	public ResponseEntity<List<Moto>> listarMotosPorUsuarioId(@PathVariable("usuarioId") int id ){
		List<Moto> motos = motoService.byUsuarioId(id);
		if(motos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(motos);
	}
	
	
}
