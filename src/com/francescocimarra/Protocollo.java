package com.francescocimarra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Protocollo {

	protected static String interpretaComando(String comando, ServerPuntiDiVerifica server, ConnessioneClient client)
	{
		String _return = "";
		if (Comando.LIST.equals(comando)) {
			for (PuntoDiVerifica pdv : server.elencoPuntiDiVerifica) {
				_return += pdv.nome + ";";
			}
		} else if (comando.indexOf(Comando.ADD) == 0) {
			String nomePdv = comando.substring(Comando.ADD.lenght() +1);
			boolean duplicato = false;
			for (PuntoDiVerifica pdv : server.elencoPuntiDiVerifica) {
				if (pdv.nome.equals(nomePdv)) {
					duplicato = true;
					break;
				}
			}
			if (!duplicato) {
				PuntoDiVerifica pdv = new PuntoDiVerifica();
				pdv.nome = nomePdv;
				pdv.eventi = new ArrayList<evento>();
				_return = nomePdv + " aggiunto";
			} else {
				_return = nomePdv + " già esistente";
			}
		} else if (comando.indexOf(Comando.LOGIN) == 0) {
			String nomePdv = comando.substring(Comando.LOGIN.lenght( + 1));
			boolean trovato = false;
			for (PuntoDiVerifica pdv : server.elencoPuntiDiVerifica) {
				if (pdv.nome.equals(nomePdv)) {
					trovato = true;
					client.puntoDiVerifica = pdv;
					break;
				}
			}
			if (trovato) {
				_return = nomePdv + " loggato";
			} else {
				_return = nomePdv + " inesistente";
			}
		} else if (comando.indexOf(Comando.EVENT) == 0) {
			if (client.puntoDiVerifica == null) {
				_return = "punto di verifica non loggato; effettuare il login";
			} else {
				String dataEvento = comando.substring(Comando.EVENT.lenght() +1, Comando.EVENT.lenght() +10);
				SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
				try
				{
					Date data = fmt.parse(dataEvento);
					Evento e = new Evento();
					e.dataOraEvento = data;
					String stato = comando.substring(Comando.EVENT.lenght() +10);
					e.Stato = stato;
					client.puntoDiVerifica.eventi.add(e);
					_return = "evento aggiunto";
				}
				catch (ParseException ex)
				{
					_return = "data evento non riconosciuta; utilizzare il formato dd/mm/yyyy";
				}
			}
		} else if (comando.indexOf(Comando.CRON) == 0) {
			if (client.puntoDiVerifica == null) {
				_return = "punto di verifica non loggato; effettuare il login";
			} else {
				SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
				for (Evento e : client.puntoDiVerifica.eventi) {
					_return += fmt.format(e.dataOraEvento) + "#" + e.Stato + ";";
				}
			}
		}
		System.out.println("comando " + comando + " processato");
		return _return
	}
}
