package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAO;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;



public class TestGestioneImpiegatoJdbc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ImpiegatoDAO impiegatoDAOInstance = null;
		CompagniaDAO compagniaDAOInstance = null;
		
		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			// ecco chi 'inietta' la connection: il chiamante
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);
			compagniaDAOInstance = new CompagniaDAOImpl(connection);

			//System.out.println(compagniaDAOInstance.list());
			
			testFindByIdCompagnia(compagniaDAOInstance);
			
			testInsertCompagnia(compagniaDAOInstance);
			
			testUpdateCompagnia(compagniaDAOInstance);
			
			TestFindAllByCompagnia(compagniaDAOInstance,impiegatoDAOInstance );
			
			TestDeleteCompagnia(compagniaDAOInstance);
			
			testFindByExample(compagniaDAOInstance);
			
			testFindAllByDataAssunzioneMaggioreDi(compagniaDAOInstance);
			
			testFindfindAllByRagioneSocialeContiene(compagniaDAOInstance);
			
			testListImpiegati(impiegatoDAOInstance);

			testGetImpiegati(impiegatoDAOInstance);

			testUpdateImpiegati(impiegatoDAOInstance);
			
			testInsertImpiegati(impiegatoDAOInstance);

			testDeleteImpiegati(impiegatoDAOInstance);

			testFindByExample(impiegatoDAOInstance);


			testFindAllErroriAssunzione(impiegatoDAOInstance, compagniaDAOInstance);

			testCountByDataFondazioneCompagniaGreaterThan(impiegatoDAOInstance, compagniaDAOInstance);
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private static void testFindByIdCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindByIdCompagnia inizio.............");
		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		if (elencoVociPresenti.size() < 1)
			throw new RuntimeException("testFindById : FAILED, non ci sono voci sul DB");

		Compagnia primoDellaLista = elencoVociPresenti.get(0);

		Compagnia elementoCheRicercoColDAO = compagniaDAOInstance.get(primoDellaLista.getId());
		if (elementoCheRicercoColDAO == null || !elementoCheRicercoColDAO.getRagioneSociale().equals(primoDellaLista.getRagioneSociale()))
			throw new RuntimeException("testFindById : FAILED, le login non corrispondono");

		System.out.println(".......testFindByIdCompagnia fine: PASSED.............");
	}
	
	private static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testInsertCompagnia inizio.............");
		int quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("AcerSRL", 15000, new Date()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertUser : FAILED");

		System.out.println(".......testInsertCompagnia fine: PASSED.............");
	}
	
	private static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testUpdateCompagnia inizio.............");
		List<Compagnia> elencoNegoziPresenti = compagniaDAOInstance.list();
		
		if (!elencoNegoziPresenti.isEmpty()) {
			
			Compagnia primoNegozioDellaLista = elencoNegoziPresenti.get(0);
			String cambioNomeNegozio = "COOP-SRL";
			System.out.println("Elemento da modificare: " + primoNegozioDellaLista);
			primoNegozioDellaLista.setRagioneSociale(cambioNomeNegozio);
			
			compagniaDAOInstance.update(primoNegozioDellaLista);
			
			// test find by id E verifico se l'update è andato a buon fine
			Long idPerTestUpdate = primoNegozioDellaLista.getId();
			Compagnia negozioTrovatoConId = compagniaDAOInstance.get(idPerTestUpdate);

			System.out.println("Elemento modificato: " + negozioTrovatoConId);

			if (negozioTrovatoConId == null || !negozioTrovatoConId.getRagioneSociale().equals(cambioNomeNegozio))
				throw new RuntimeException("Test fallitto: update non avvenuto");			
		}
		else {
			throw new RuntimeException("Test fallito: database vuoto");
		}
		System.out.println(".......testUpdateCompagnia fine: PASSED.............");
		
	}
	
	
	
	private static void TestFindAllByCompagnia(CompagniaDAO compagniaDAOInstance, ImpiegatoDAO impiegatoDAOInstance) throws Exception{
		
		List<Impiegato> listaImpiegatiByCompagnia = new ArrayList<>();
		
		Compagnia compagniaTest = new Compagnia();
		compagniaTest.setRagioneSociale("conadSrl");
		compagniaTest.setFatturatoAnnuo(1000);
		compagniaTest.setDataFondazione(new Date());
		compagniaTest.setId(1L);
		
		System.out.println(compagniaTest);
		
		
		listaImpiegatiByCompagnia = impiegatoDAOInstance.findAllByCompagnia(compagniaTest);
		for (Impiegato impiegato : listaImpiegatiByCompagnia) {
			System.out.println(impiegato);
		}
		
		
	}
	
	
	public static void TestDeleteCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......TestDeleteCompagnia inizio.............");
		// me ne creo uno al volo
		int quantiElementiInseriti = compagniaDAOInstance
				.insert(new Compagnia("euronics", 5000, new Date()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testDeleteUser : FAILED, user da rimuovere non inserito");

		List<Compagnia> elencoVociPresenti = compagniaDAOInstance.list();
		int numeroElementiPresentiPrimaDellaRimozione = elencoVociPresenti.size();
		if (numeroElementiPresentiPrimaDellaRimozione < 1)
			throw new RuntimeException("testDeleteUser : FAILED, non ci sono voci sul DB");

		Compagnia ultimoDellaLista = elencoVociPresenti.get(numeroElementiPresentiPrimaDellaRimozione - 1);
		compagniaDAOInstance.delete(ultimoDellaLista);

		// ricarico per vedere se sono scalati di una unità
		int numeroElementiPresentiDopoDellaRimozione = compagniaDAOInstance.list().size();
		if (numeroElementiPresentiDopoDellaRimozione != numeroElementiPresentiPrimaDellaRimozione - 1)
			throw new RuntimeException("testDeleteUser : FAILED, la rimozione non è avvenuta");

		System.out.println(".......TestDeleteCompagnia fine: PASSED.............");
	}
	
	private static void testFindByExample(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindByExample.......");

		Compagnia compagniaExample = new Compagnia();
		compagniaExample.setRagioneSociale("compagnia1");
		compagniaExample.setFatturatoAnnuo(1000);
		List<Compagnia> result = compagniaDAOInstance.findByExample(compagniaExample);
		for (Compagnia compagnia : result) {
			System.out.println(compagnia);
		}

		System.out.println(".......testFindByExample PASSED.......");
	}
	
	
	private static void testFindAllByDataAssunzioneMaggioreDi(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindAllByDataAssunzioneMaggioreDi.......");

		Date date = new SimpleDateFormat("yyyy/MM/dd").parse("1990/01/01");
		List<Impiegato> result = compagniaDAOInstance.findAllByDataAssunzioneMaggioreDi(date);
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}

		System.out.println(".......testFindAllByDataAssunzioneMaggioreDi PASSED.......");
	}

	private static void testFindfindAllByRagioneSocialeContiene(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindfindAllByRagioneSocialeContiene.......");

		if (compagniaDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione fallita, database vuoto");

		String input = "c";

		List<Compagnia> result = compagniaDAOInstance.findAllByRagioneSocialeContiene(input);
		for (Compagnia compagnia : result) {
			System.out.println(compagnia);
		}

		System.out.println(".......testFindfindAllByRagioneSocialeContiene PASSED.......");
	}
	
	
	private static void testListImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testListImpiegati.......");

		List<Impiegato> result = impiegatoDAOInstance.list();
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}

		System.out.println(".......testListImpiegati PASSED.......");
	}

	private static void testGetImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testGetImpiegati.......");

		if (impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita, elenco vuoto");

		Long idInput = impiegatoDAOInstance.list().get(0).getId();
		System.out.println(impiegatoDAOInstance.get(idInput));

		System.out.println(".......testGetImpiegati PASSED.......");
	}

	private static void testUpdateImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testUpdateImpiegati.......");

		if (impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita DB vuoto");

		Impiegato impiegatoDaAggiungereSuCuiFareUpdate = new Impiegato("luigi", "bianchi", "codiceFiscaleLuigiBianchi",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01"));
		impiegatoDAOInstance.insert(impiegatoDaAggiungereSuCuiFareUpdate);

		Impiegato impiegatoUpdate = new Impiegato(
				impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1).getId(), "gigi", "verdi",
				"codiceFiscaleGigiVerdi", new SimpleDateFormat("yyyy/MM/dd").parse("1951/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("2000/01/01"));

		if (impiegatoDAOInstance.update(impiegatoUpdate) != 1)
			throw new RuntimeException("operazione non riuscita 0 rowsAffected");
		Impiegato ultimoDellaLista = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		if (ultimoDellaLista.getNome().equals(impiegatoDaAggiungereSuCuiFareUpdate.getNome())
				|| ultimoDellaLista.getCognome().equals(impiegatoDaAggiungereSuCuiFareUpdate.getCognome())
				|| ultimoDellaLista.getCodiceFiscale().equals(impiegatoDaAggiungereSuCuiFareUpdate.getCodiceFiscale())
				|| ultimoDellaLista.getDataAssunzione().equals(impiegatoDaAggiungereSuCuiFareUpdate.getDataAssunzione())
				|| ultimoDellaLista.getDataNascita().equals(impiegatoDaAggiungereSuCuiFareUpdate.getDataNascita()))
			throw new RuntimeException("operazione non riuscita i valori non sono cambiati");

		System.out.println(".......testUpdateImpiegati PASSED.......");
	}

	private static void testInsertImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testInsertImpiegati.......");

		Impiegato impiegatoDaInserire = new Impiegato("carlo", "bianchi", "codiceFiscale",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01"));

		if (impiegatoDAOInstance.insert(impiegatoDaInserire) != 1)
			throw new RuntimeException("operazione non riuscita, nessun elemento inserito");

		System.out.println(".......testInsertImpiegati PASSED.......");
	}

	private static void testDeleteImpiegati(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testDeleteImpiegati.......");

		impiegatoDAOInstance.insert(new Impiegato("carlo", "bianchi", "codiceFiscale",
				new SimpleDateFormat("yyyy/MM/dd").parse("1950/01/01"),
				new SimpleDateFormat("yyyy/MM/dd").parse("1999/01/01")));

		if (impiegatoDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione eliminazione non effettuare, elenco vuoto");

		Impiegato impiegatoDaEliminare = impiegatoDAOInstance.list().get(impiegatoDAOInstance.list().size() - 1);

		if (impiegatoDAOInstance.delete(impiegatoDaEliminare) != 1)
			throw new RuntimeException("operazione non riuscita nessun elemento eliminato");

		System.out.println(".......testDeleteImpiegati PASSED.......");
	}

	private static void testFindByExample(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println(".......testFindByExample.......");

		Impiegato impiegatoExample = new Impiegato();
		impiegatoExample.setNome("carlo");
		impiegatoExample.setCodiceFiscale("codiceFiscale");
		List<Impiegato> result = impiegatoDAOInstance.findByExample(impiegatoExample);
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}
		System.out.println(".......testFindByExample PASSED.......");
	}


	private static void testFindAllErroriAssunzione(ImpiegatoDAO impiegatoDAOInstance,
			CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindAllErroriAssunzione.......");

		if (compagniaDAOInstance.list().size() < 1)
			throw new RuntimeException("operazione non riuscita, elenco compagnia vuoto");

		List<Impiegato> result = impiegatoDAOInstance.findAllErroriAssunzione();
		for (Impiegato impiegato : result) {
			System.out.println(impiegato);
		}

		System.out.println(".......testFindAllErroriAssunzione PASSED.......");
	}

	private static void testCountByDataFondazioneCompagniaGreaterThan(ImpiegatoDAO impiegatoDAOInstance,
			CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testCountByDataFondazioneCompagniaGreaterThan.......");

		Date dateInput = new SimpleDateFormat("yyyy/MM/dd").parse("1989/01/01");
		
		System.out.println("numero impiegati: " + impiegatoDAOInstance.countByDataFondazioneCompagniaGreaterThan(dateInput));
		
		System.out.println(".......testCountByDataFondazioneCompagniaGreaterThan PASSED.......");
	}
	
}
