package it.prova.gestioneimpiegatojdbc.dao.impiegato;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestioneimpiegatojdbc.dao.AbstractMySQLDAO;
import it.prova.gestioneimpiegatojdbc.model.Compagnia;
import it.prova.gestioneimpiegatojdbc.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	
	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}
	
	
	
	@Override
	public List<Impiegato> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato compTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from impiegato")) {

			while (rs.next()) {
				compTemp = new Impiegato();
				compTemp.setNome(rs.getString("nome"));
				compTemp.setCognome(rs.getString("cognome"));
				compTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				compTemp.setDataNascita(rs.getDate("datanascita"));
				compTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				compTemp.setId(rs.getLong("ID"));
				result.add(compTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codicefiscale=?, datanascita=?, dataassunzione=? where id=?;")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			ps.setLong(6, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato (nome, cognome, codicefiscale, datanascita, dataassunzione) VALUES (?, ?, ?, ?, ?);")) {
			ps.setString(1, input.getNome());
			ps.setString(2, input.getCognome());
			ps.setString(3, input.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(input.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(input.getDataAssunzione().getTime()));
			
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impTemp = null;
		
		try (Statement s = connection.createStatement()){
			
			String query ="select * from impiegato where ";
			
			if (!(input.getNome() == null)) {
				query += "nome like '" + input.getNome() + "%' and ";
			}
			if (!(input.getCognome() == null)) {
				query += "cognome like '" + input.getCognome() + "%' and ";
			}
			if (!(input.getDataNascita() == null)) {
				query += "datanascita > '" + new java.sql.Date(input.getDataNascita().getTime()) + " ' and ";
			}
			if (!(input.getDataAssunzione() == null)) {
				query += "dataassunzione > '" + new java.sql.Date(input.getDataAssunzione().getTime()) + " ' and ";
			}
			query+= "1=1";
			System.out.println(query);
			
			try (ResultSet rs = s.executeQuery(query);) {
				while (rs.next()) {
					impTemp = new Impiegato();
					impTemp.setNome(rs.getString("nome"));
					impTemp.setCognome(rs.getString("cognome"));
					impTemp.setCodiceFiscale(rs.getString("codicefiscale"));
					impTemp.setDataNascita(rs.getDate("datanascita"));
					impTemp.setDataAssunzione(rs.getDate("dataassunzione"));
					impTemp.setId(rs.getLong("ID"));
				}
			} // niente catch qui			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato userTemp = null;
		Compagnia compTemp = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where i.compagnia_id = ? ;")) {
			// quando si fa il setDate serve un tipo java.sql.Date
			//ps.setDate(1, new java.sql.Date(dateCreatedInput.getTime()));
			ps.setLong(1, compagniaInput.getId());

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					userTemp = new Impiegato();
					userTemp.setNome(rs.getString("NOME"));
					userTemp.setCognome(rs.getString("COGNOME"));
					userTemp.setCodiceFiscale(rs.getString("codicefiscale"));
					userTemp.setDataNascita(rs.getDate("datanascita"));
					userTemp.setDataAssunzione(rs.getDate("dataassunzione"));
					userTemp.setId(rs.getLong("i.ID"));
					
					compTemp = new Compagnia();
					compTemp.setId(rs.getLong("c.id"));
					compTemp.setRagioneSociale(rs.getString("ragionesociale"));
					compTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					compTemp.setDataFondazione(rs.getDate("datafondazione"));
					userTemp.setCompagnia(compTemp);
					result.add(userTemp);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int countByDataFondazioneCompagniaGreaterThan(Date dateCreatedInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (dateCreatedInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		
		//String conta = "conta";
		
		try (PreparedStatement ps = connection.prepareStatement(
				"select count(*) as conta from compagnia c where c.datafondazione > ?;")) {
			//ps.setString(1, conta);
			ps.setDate(1, new java.sql.Date(dateCreatedInput.getTime()));
			
			try (ResultSet rs = ps.executeQuery();){
			if (rs.next()) {
				result = rs.getInt("conta");
			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findAllErroriAssunzione() throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato userTemp = null;
		Compagnia compTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from"
				+ " impiegato i inner join compagnia c on c.id=i.compagnia_id where c.datafondazione > i.dataassunzione ;")) {

			while (rs.next()) {
				userTemp = new Impiegato();
				userTemp.setNome(rs.getString("NOME"));
				userTemp.setCognome(rs.getString("COGNOME"));
				userTemp.setCodiceFiscale(rs.getString("codicefiscale"));
				userTemp.setDataNascita(rs.getDate("datanascita"));
				userTemp.setDataAssunzione(rs.getDate("dataassunzione"));
				userTemp.setId(rs.getLong("i.ID"));
				
				compTemp = new Compagnia();
				compTemp.setId(rs.getLong("c.id"));
				compTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compTemp.setDataFondazione(rs.getDate("datafondazione"));
				userTemp.setCompagnia(compTemp);
				result.add(userTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	
	
	
	
	
}
