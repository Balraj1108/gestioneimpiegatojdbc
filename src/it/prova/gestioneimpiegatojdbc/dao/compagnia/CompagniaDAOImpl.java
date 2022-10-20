package it.prova.gestioneimpiegatojdbc.dao.compagnia;

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



public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	
	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}
	
	
	
	@Override
	public List<Compagnia> list() throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {

			while (rs.next()) {
				compTemp = new Compagnia();
				compTemp.setRagioneSociale(rs.getString("ragionesociale"));
				compTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
				compTemp.setDataFondazione(rs.getDate("datafondazione"));
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
	public Compagnia get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setRagioneSociale(rs.getString("ragionesociale"));
					result.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					result.setDataFondazione(rs.getDate("datafondazione"));
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
	public int update(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?;")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			ps.setLong(4, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia input) throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia (ragionesociale, fatturatoannuo, datafondazione) VALUES (?, ?, ?);")) {
			ps.setString(1, input.getRagioneSociale());
			ps.setInt(2, input.getFatturatoAnnuo());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(3, new java.sql.Date(input.getDataFondazione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null || input.getId() == null || input.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM compagnia WHERE ID=?")) {
			ps.setLong(1, input.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia userTemp = null;
		
		try (Statement s = connection.createStatement()){
			
			String query ="select * from compagnia where ";
			
			if (!(input.getRagioneSociale() == null)) {
				query += "ragionesociale like '" + input.getRagioneSociale() + "%' and ";
			}
			if (!(input.getFatturatoAnnuo() < 0)) {
				query += "fatturatoannuo like '" + input.getFatturatoAnnuo() + "%' and ";
			}
			if (!(input.getDataFondazione() == null)) {
				query += "datecreated > '" + new java.sql.Date(input.getDataFondazione().getTime()) + " ' and ";
			}
			query+= "1=1";
			System.out.println(query);
			
			try (ResultSet rs = s.executeQuery(query);) {
				while (rs.next()) {
					userTemp = new Compagnia();
					userTemp.setRagioneSociale(rs.getString("ragionesociale"));
					userTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					userTemp.setDataFondazione(rs.getDate("datafondazione"));
					userTemp.setId(rs.getLong("ID"));
					result.add(userTemp);
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
	public List<Impiegato> findAllByDataAssunzioneMaggioreDi(Date dateCreatedInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (dateCreatedInput == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato userTemp = null;
		Compagnia compTemp = null;

		try (PreparedStatement ps = connection.prepareStatement("select * from impiegato i inner join compagnia c on c.id=i.compagnia_id where i.dataassunzione > ?;")) {
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(1, new java.sql.Date(dateCreatedInput.getTime()));

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
	public List<Compagnia> findAllByRagioneSocialeContiene(String ragioneSocialeInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		if (ragioneSocialeInput.equals("") || ragioneSocialeInput == null) {
			throw new Exception("Valore di input non ammesso.");
		}
		
		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia userTemp = null;
		
		try (PreparedStatement ps = connection.prepareStatement("select * from compagnia c where c.ragionesociale = ?;")) {
			ps.setString(1, ragioneSocialeInput);
			
			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					userTemp = new Compagnia();
					userTemp.setRagioneSociale(rs.getString("ragionesociale"));
					userTemp.setFatturatoAnnuo(rs.getInt("fatturatoannuo"));
					userTemp.setDataFondazione(rs.getDate("datafondazione"));
					userTemp.setId(rs.getLong("ID"));
					result.add(userTemp);
				}
			} // niente catch qui
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			// TODO: handle exception
		}
		return result;
	}
	
}
