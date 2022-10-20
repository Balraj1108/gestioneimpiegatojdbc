package it.prova.gestioneimpiegatojdbc.test;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.connection.MyConnection;
import it.prova.gestioneimpiegatojdbc.dao.Constants;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAO;
import it.prova.gestioneimpiegatojdbc.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAO;
import it.prova.gestioneimpiegatojdbc.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;


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
			
			System.out.println(compagniaDAOInstance.findAllByRagioneSocialeContiene("ConadSLR"));
			
			
			

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
	
	
	
}
