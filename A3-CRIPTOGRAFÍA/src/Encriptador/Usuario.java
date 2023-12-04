package Encriptador;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

	private String nombreUsuario;
	private String contraseñaHasheada;
	
	public Usuario(String nombreUsuario, String contraseñaHasheada) {
		this.nombreUsuario = nombreUsuario;
		this.contraseñaHasheada = contraseñaHasheada;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	
	public String getContraseñaHasheada() {
		return contraseñaHasheada;
	}
	
}
