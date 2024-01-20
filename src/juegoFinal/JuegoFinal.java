package juegoFinal;

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

class JuegoFinal {
	static int dif;
	static boolean menu = true;
	static boolean jugando = false;
	static String[] palabras = { "hola", "taza", "casa", "mesa", "pato", "baul", "luna", "piel", "lago", "sapo",
			"marca", "lapiz", "perro", "nubes", "playa", "silla", "ruido", "libro", "calle", "coche", "zapato",
			"animal", "rapido", "carcel", "bosque", "huesos", "flores", "musica", "barato", "fuerte" };
	static String[][] letras = { { "h", "o", "l", "a" }, { "t", "a", "z", "a" }, { "c", "a", "s", "a" },
			{ "m", "e", "s", "a" }, { "p", "a", "t", "o" }, { "b", "a", "u", "l" }, { "l", "u", "n", "a" },
			{ "p", "i", "e", "l" }, { "l", "a", "g", "o" }, { "s", "a", "p", "o" }, { "m", "a", "r", "c", "a" },
			{ "l", "a", "p", "i", "z" }, { "p", "e", "r", "r", "o" }, { "n", "u", "b", "e", "s" },
			{ "p", "l", "a", "y", "a" }, { "s", "i", "l", "l", "a" }, { "r", "u", "i", "d", "o" },
			{ "l", "i", "b", "r", "o" }, { "c", "a", "l", "l", "e" }, { "c", "o", "c", "h", "e" },
			{ "z", "a", "p", "a", "t", "o" }, { "a", "n", "i", "m", "a", "l" }, { "r", "a", "p", "i", "d", "o" },
			{ "c", "a", "r", "c", "e", "l" }, { "b", "o", "s", "q", "u", "e" }, { "h", "u", "e", "s", "o", "s" },
			{ "f", "l", "o", "r", "e", "s" }, { "m", "u", "s", "i", "c", "a" }, { "b", "a", "r", "a", "t", "o" },
			{ "f", "u", "e", "r", "t", "e" } };

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		while (menu) {
			int juego;
			do {
				juego = Integer.parseInt(JOptionPane.showInputDialog(
						"Bienvenido, puedes elegir entre dos juegos:\n1. Ahorcado\n2. Palabras Mezcladas\n3. Salir"
								+ "\nLas relgas de cada juego se te explicaran luego."));
			} while (juego < 1 || juego > 3);
			switch (juego) {
			case 1:
				ahorcado();
				break;
			case 2:
				palabrasMezcladas();
				break;
			case 3:
				System.out.println("saliendo...");
				break;
			}
		}
	}

	public static void ahorcado() {
		int pal_num, vida;
		boolean ok, gan;
		int[] palabrasJugadas = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1 };
		String jug_palabra, jugada, jugadas_msg, pal;
		jugando = true;
		JOptionPane.showMessageDialog(null,
				"Bienvenido, este es el ahorcado. Adivina la palabra letra por letra o la palabra entera.\n"
						+ "Si fallas la palabra, perderás.\nPara simplificar, las palabras no tienen tildes.",
				"Ahorcado", JOptionPane.DEFAULT_OPTION,
				new ImageIcon(JuegoFinal.class.getResource("/img/hangman.gif")));
		do {
			dif = Integer.parseInt(JOptionPane.showInputDialog("Elige tu dificultad:\n1. Normal\n2. Difícil"));
		} while (dif < 1 || dif > 2);
		while (jugando) {
			vida = 6;
			pal_num = generarPalabra(palabrasJugadas);
			if (pal_num == -1) {
				for (int p = 0; p < palabrasJugadas.length; p++) {
					palabrasJugadas[p] = -1;
				}
				JOptionPane.showMessageDialog(null, "AVISO: Las palabras se comenzarán a repetir.");
				pal_num = generarPalabra(palabrasJugadas);
			}
			pal = palabras[pal_num];
			String[] letr = letras[pal_num];
			int[] estado = new int[letr.length];
			String[] jugadas = { "", "", "", "", "", "" };
			jugadas_msg = "";
			jugada = "";
			gan = false; // diag
			while (vida > 0 && !gan) {
				jug_palabra = formarPalabra(letr, estado);
				if (!pal.equalsIgnoreCase(jug_palabra)){
					//este if es para evitar que salga el ingresar una letra
					//despues de haber completado la palabra el turno anterior.
					JOptionPane.showMessageDialog(null, jug_palabra + "\nLetras no acertadas: " + jugadas_msg, "Ahorcado",
						JOptionPane.DEFAULT_OPTION,
						new ImageIcon(JuegoFinal.class.getResource("/img/" + vida + ".png")));
					jugada = JOptionPane.showInputDialog("ingrese letra");
				}
				if (pal.equalsIgnoreCase(jug_palabra) || (jugada.length() > 1 && pal.equalsIgnoreCase(jugada))) {
					JOptionPane.showMessageDialog(null,
							"Ganaste! La palabra era: " + pal + "\nLetras sin acertar: " + jugadas_msg, "Ahorcado",
							JOptionPane.DEFAULT_OPTION,
							new ImageIcon(JuegoFinal.class.getResource("/img/ganaste.jpg")));
					gan = true;
				} else if (jugada.length() > 1) {
					vida = 0;
				} else {
					while (chequearLetra(jugada, jugadas)) {
						jugada = JOptionPane.showInputDialog("Ingresa otra letra");
					}
					ok = cambiarEstado(jugada, letr, estado);
					if (!ok) {
						jugadas_msg = jugadas_msg + jugada;
						jugadas[vida - 1] = jugada;
						vida -= dif;
					}
				}
				if (vida == 0) {
					JOptionPane.showMessageDialog(null, "Perdiste! La palabra era: " + pal + "\nLetras: " + jugadas_msg,
							"Ahorcado", JOptionPane.DEFAULT_OPTION,
							new ImageIcon(JuegoFinal.class.getResource("/img/" + vida + ".png")));
				}
			}
			preguntarMenu();
		}
	}

	public static void palabrasMezcladas() {
		int pal_num, vida, num;
		int[] palabrasJugadas = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1 };
		String pal, letra, jugada, mezcla_msg;
		boolean letra_completa, gan;
		jugando = true;
		JOptionPane.showMessageDialog(null,
				"Bienvenido, en este juego se le reorden las letras al azar a una palabra, y debes adivinar\n"
						+ "que palabra es. La dificultad altera que los intentos que tienes: Normal son 2 intentos, Díficl es un único intento por palabra",
						"Palabras Mezcladas",JOptionPane.DEFAULT_OPTION,new ImageIcon(JuegoFinal.class.getResource("/img/letras.gif")));
		do {
			dif = Integer.parseInt(JOptionPane.showInputDialog("Elige tu dificultad:\n1. Normal\n2. Difícil"));
		} while (dif < 1 || dif > 2);

		while (jugando) {
			vida = 2;
			pal_num = generarPalabra(palabrasJugadas);
			if (pal_num == -1) {
				for (int p = 0; p < palabrasJugadas.length; p++) {
					palabrasJugadas[p] = -1;
				}
				JOptionPane.showMessageDialog(null, "AVISO: Las palabras se comenzarán a repetir.");
				pal_num = generarPalabra(palabrasJugadas);
			}
			pal = palabras[pal_num];
			System.out.println(pal);
			String[] letr = letras[pal_num];
			String[] mezcla = new String[letr.length];
			mezcla_msg = "";
			do {
				for (int l = 0; l < letr.length; l++) {
					letra_completa = false;
					num = (int) (Math.random() * letr.length);
					letra = letr[l];
					while (!letra_completa) {
						if (mezcla[num] == null) {
							// originalmente esta condición tambien empleaba un AND,
							// mezcla[num] == null && num != l, para que no queden en la misma posición
							// y esté la chance que salga la misma palabra, pero no pude implementarlo así
							// porque a veces
							// se creaba un bucle infinito con algunas palabras
							mezcla[num] = letra;
							letra_completa = true;
						} else {
							num = (int) (Math.random() * letr.length);
						}
					}
				}
				for (int m = 0; m < mezcla.length; m++) {
					mezcla_msg = mezcla_msg + mezcla[m];
				}
			} while (mezcla_msg.equalsIgnoreCase(pal));
			gan = false;
			while (vida > 0 && !gan) {
				JOptionPane.showMessageDialog(null,
						"la palabra es: " + mezcla_msg + "\nIntentos restantes: " + (vida / dif));
				jugada = JOptionPane.showInputDialog("Ingrese palabra");
				if (jugada.equalsIgnoreCase(pal)) {
					JOptionPane.showMessageDialog(null, "Ganaste!\nLa palabra era: " + pal,"Palabras Mezcaladas",JOptionPane.DEFAULT_OPTION,new ImageIcon(JuegoFinal.class.getResource("/img/ganaste.jpg")));
					gan = true;
				} else {
					vida -= dif;
				}
				if (vida == 0) {
					JOptionPane.showMessageDialog(null, "Has perdido!\nLa palabra era: " + pal,"Palabras Mezcaladas",JOptionPane.DEFAULT_OPTION,new ImageIcon(JuegoFinal.class.getResource("/img/perdiste_mezcla.jpg")));
				}
			}
			preguntarMenu();
		}
	}

	public static void preguntarMenu() {
		int opt;
		do {
			opt = Integer.parseInt(JOptionPane.showInputDialog(
					"¿Quieres seguir jugando?\n1. Sí\n2. Volver a elegir díficultad\n3. Volver al menu\n4. Salir"));
		} while (!(0 < opt && opt < 5));

		switch (opt) {
		case 1:
			jugando = true;
			break;
		case 2:
			do {
				dif = Integer.parseInt(JOptionPane.showInputDialog("Elige tu dificultad:\n1. Normal\n2. Difícil"));
			} while (dif < 1 || dif > 2);
			jugando = true;
			break;
		case 3:
			jugando = false;
			break;
		case 4:
			jugando = false;
			menu = false;
			break;
		}
	}

	public static int generarPalabra(int[] arr) {
		int num = (int) (Math.random() * 30);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == num) {
				num = (int) (Math.random() * 30);
				i = 0;
			} else if (arr[i] == -1) {
				arr[i] = num;
				return num;
			}
		}
		return -1;
	}

	public static String formarPalabra(String[] letr_arr, int[] estado) {
		String pal = "";
		for (int i = 0; i < letr_arr.length; i++) {
			switch (estado[i]) {
			case 0:
				pal = pal + " _ ";
				break;
			case 1:
				pal = pal + letr_arr[i];
				break;
			}
		}
		return pal;
	}

	public static boolean chequearLetra(String jugada, String[] jugadas) {
		boolean jugado = false;
		for (int i = 0; i < jugadas.length; i++) {
			if (jugada.equalsIgnoreCase(jugadas[i])) {
				jugado = true;
			}
		}
		return jugado;
	}

	public static boolean cambiarEstado(String letra, String letr_arr[], int[] estado) {
		boolean ok = false;
		for (int i = 0; i < letr_arr.length; i++) {
			if (letra.equalsIgnoreCase(letr_arr[i])) {
				estado[i] = 1;
				ok = true;
			}
		}
		return ok;
	}

}
