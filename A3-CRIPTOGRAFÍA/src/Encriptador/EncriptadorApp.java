package Encriptador;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncriptadorApp {
	private static String fraseEncriptada = null;
	private static SecretKey codigoMalva;
	
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		
        try {
            KeyGenerator generador = KeyGenerator.getInstance("AES");
            codigoMalva = generador.generateKey();
        } catch (Exception e) {
            System.out.println("Error al generar la clave: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        //POBLAMOS EL ARRAY CON DISTINTOS USUARIOS, Y SUS CONTRASEÑAS HASHEADAS
    	Usuario usuario1 = new Usuario("Paco", hashearContraseña("blabla"));
    	Usuario usuario2 = new Usuario("Juan", hashearContraseña("bleble"));
    	Usuario usuario3 = new Usuario("Lucas", hashearContraseña("bloblo"));
    	
    	ArrayList<Usuario> usuariosEnMaquina = new ArrayList<>();
    	usuariosEnMaquina.add(usuario1);
    	usuariosEnMaquina.add(usuario2);
    	usuariosEnMaquina.add(usuario3);

    	boolean accesoPermitido = false;
    	int intentos = 0;
    	
		    	
    	while(intentos < 3 && !accesoPermitido) {
    		System.out.println("Alto, ¿quién va?. Introduzca su nombre de usuario.");
    		String nombreUsuario = scanner.nextLine();
    		System.out.println("Santo y seña.");
    		String contraseña = scanner.nextLine();
    		
    		if(verificarCredenciales(nombreUsuario, contraseña, usuariosEnMaquina)) {
    			accesoPermitido = true;
    			System.out.println("Bienvenido a la aplicación, " + nombreUsuario);
    		} else {
    			System.out.println("Incorrecto, inténtelo de nuevo.");
    			intentos++;
    		}
    		
    	}
    	
    	if(!accesoPermitido) {
    		System.out.println("Número máximo de intentos alcanzado. Adiós.");
    		System.exit(0);
    	}
		
		while(true) {
			System.out.println("Menú: ");
			System.out.println("1. Salir del programa");
			System.out.println("2. Encriptar frase");
			System.out.println("3. Desencriptar frase");
			System.out.println("Seleccione una opción");
			
			int opcion = scanner.nextInt();
			scanner.nextLine();
			
			switch (opcion) {
			case 1:
				System.out.println("Saliendo del programa.");
				System.exit(0); //salir del programa
				break;
			case 2:
				System.out.println("Introduzca la frase a encriptar: ");
				String frase = scanner.nextLine();
				//LLAMAR A MÉTODO PARA ENCRIPTAR LA FRASE
				try {
					fraseEncriptada = encriptarFrase(frase);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Su frase ha sido encriptada.");
				break;
			case 3:
				if (fraseEncriptada != null) {
					//LLAMAR A MÉTODO PARA DESENCRIPTAR LA FRASE, SI ESTA EXISTE
					String fraseDesencriptada = desencriptarFrase(fraseEncriptada);
					System.out.println("Frase desencriptada: " + fraseDesencriptada);
				} else {
					System.out.println("No existe frase en memoria, por favor encripte una frase antes de tratar de desencriptar.");
				}
				break;
			default:
			System.out.println("Opción inválida. Por favor, inténtelo de nuevo");
			}
		}
	}
	//MÉTODOS PARA ENCRIPTAR Y DESENCRIPTAR
    // Método para encriptar la frase
    public static String encriptarFrase(String frase) throws Exception {
        try {
            Cipher cifrador = Cipher.getInstance("AES");
            cifrador.init(Cipher.ENCRYPT_MODE, codigoMalva); //HAY QUE GUARDAR LA CLAVE FUERA PARA PODER USARLO EN DISTINTOS METODOS
            
            byte[] fraseEncriptadaBytes = cifrador.doFinal(frase.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(fraseEncriptadaBytes);
        } catch (Exception gse) {
            System.out.println("Problema en la ejecución de la encriptación." + gse.getMessage());
            gse.printStackTrace();
            throw gse;
        }
    }

    // Método para desencriptar la frase
    public static String desencriptarFrase(String fraseEncriptada) throws Exception {
        try {
            Cipher cifrador = Cipher.getInstance("AES");
            cifrador.init(Cipher.DECRYPT_MODE, codigoMalva); // Use the stored key

            byte[] fraseEncriptadaBytes = Base64.getDecoder().decode(fraseEncriptada);
            byte[] fraseDesencriptadaBytes = cifrador.doFinal(fraseEncriptadaBytes);
            return new String(fraseDesencriptadaBytes, "UTF-8");
        } catch (Exception gse) {
            System.out.println("Problema en la ejecución de la desencriptación." + gse.getMessage());
            gse.printStackTrace();
            throw gse;
        }
    }
    
    //MÉTODOS PARA EL SEGUNDO REQUERIMENTO
    
    //Método para hashear la contraseña, toma la contraseña introducida por pantalla y la hashea
    private static String hashearContraseña(String contraseña) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(contraseña.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
    
    //Método para comparar las credenciales introducidas con las que están guardadas    
    private static boolean verificarCredenciales(String nombreUsuario, String contraseña, ArrayList<Usuario> usuariosEnMaquina) {
        String contraseñaHasheada;
        try {
            contraseñaHasheada = hashearContraseña(contraseña); //compara la contraseña hasheada guardada con la introducida por pantalla
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error al hashear la contraseña: " + e.getMessage());
            return false;
        }

        for(Usuario usuario : usuariosEnMaquina) { 
            if(usuario.getNombreUsuario().equals(nombreUsuario) && usuario.getContraseñaHasheada().equals(contraseñaHasheada)) {
                return true;
            }
        }
        return false;
    }


}