package dao;

import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import model.UtenteBean;
import model.UtenteBean.Ruolo;

public class UtenteDAOImpl implements UtenteDAO {
	private static final String TABLE_NAME = "utente";
	private DataSource ds;

	public UtenteDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(UtenteBean u) throws SQLException {

		String sql = "INSERT INTO " + TABLE_NAME + " (email, nome, cognome, password, cellulare, fk_indirizzo, ruolo) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, u.getEmail());
			ps.setString(2, u.getNome());
			ps.setString(3, u.getCognome());
			ps.setString(4, u.getPassword());
			ps.setString(5, u.getCellulare());
			ps.setInt(6, u.getIndirizzo().getIdIndirizzo());
			ps.setString(7, u.getRuolo().name());

			ps.executeUpdate();
		}
	}

	public synchronized UtenteBean doRetrieveByKey(int id) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ?";

		UtenteBean u = null;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					u = new UtenteBean();

					IndirizzoDAOImpl ind = new IndirizzoDAOImpl(ds);

					u.setIdUtente(rs.getInt("id_utente"));
					u.setEmail(rs.getString("email"));
					u.setNome(rs.getString("nome"));
					u.setCognome(rs.getString("cognome"));
					u.setPassword(rs.getString("password"));
					u.setCellulare(rs.getString("cellulare"));
					u.setIndirizzo(ind.doRetrieveByKey(rs.getInt("fk_indirizzo")));
					u.setRuolo(Ruolo.valueOf(rs.getString("ruolo")));
				}
			}
		}

		return u;
	}

	public synchronized UtenteBean doRetrieveByEmail(String email) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";

		UtenteBean u = null;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					u = new UtenteBean();

					IndirizzoDAOImpl ind = new IndirizzoDAOImpl(ds);

					u.setIdUtente(rs.getInt("id_utente"));
					u.setEmail(rs.getString("email"));
					u.setNome(rs.getString("nome"));
					u.setCognome(rs.getString("cognome"));
					u.setPassword(rs.getString("password"));
					u.setCellulare(rs.getString("cellulare"));
					u.setIndirizzo(ind.doRetrieveByKey(rs.getInt("fk_indirizzo")));
					u.setRuolo(Ruolo.valueOf(rs.getString("ruolo")));
				}
			}
		}

		return u;
	}

	public synchronized boolean doUpdate(UtenteBean u) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME
				+ " SET nome=?, cognome=?, cellulare=?, fk_indirizzo=?, ruolo=? WHERE id_utente=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, u.getNome());
			ps.setString(2, u.getCognome());
			ps.setString(3, u.getCellulare());
			ps.setInt(4, u.getIndirizzo().getIdIndirizzo());
			ps.setString(5, u.getRuolo().name());
			ps.setInt(6, u.getIdUtente());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized UtenteBean checkLogin(String email, String password) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ?";

		UtenteBean u = null;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);
			ps.setString(2, password);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					u = new UtenteBean();

					IndirizzoDAOImpl ind = new IndirizzoDAOImpl(ds);

					u.setIdUtente(rs.getInt("id_utente"));
					u.setEmail(rs.getString("email"));
					u.setNome(rs.getString("nome"));
					u.setCognome(rs.getString("cognome"));
					u.setPassword(rs.getString("password"));
					u.setCellulare(rs.getString("cellulare"));
					u.setIndirizzo(ind.doRetrieveByKey(rs.getInt("fk_indirizzo")));
					u.setRuolo(Ruolo.valueOf(rs.getString("ruolo")));
				}
			}
		}

		return u;
	}

	public synchronized List<UtenteBean> doRetrieveAll(String ruolo, int pagina) throws SQLException {

		List<UtenteBean> list = new LinkedList<>();
		
		ruolo = (ruolo == null || ruolo.trim().isEmpty()) ? null : ruolo.trim();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE (? IS NULL OR ruolo = ?) LIMIT ? OFFSET ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ruolo);
			ps.setString(2, ruolo);

			int offset = (pagina - 1) * 10;

			ps.setInt(3, 10);
			ps.setInt(4, offset);

			try (ResultSet rs = ps.executeQuery()) {

				IndirizzoDAOImpl ind = new IndirizzoDAOImpl(ds);

				while (rs.next()) {

					UtenteBean u = new UtenteBean();

					u.setIdUtente(rs.getInt("id_utente"));
					u.setEmail(rs.getString("email"));
					u.setNome(rs.getString("nome"));
					u.setCognome(rs.getString("cognome"));
					u.setPassword(rs.getString("password"));
					u.setCellulare(rs.getString("cellulare"));
					u.setIndirizzo(ind.doRetrieveByKey(rs.getInt("fk_indirizzo")));
					u.setRuolo(Ruolo.valueOf(rs.getString("ruolo")));

					list.add(u);
				}
			}
		}
		return list;
	}

	public synchronized int doCountFilteredUtenti(String ruolo) throws SQLException {
		
		ruolo = (ruolo == null || ruolo.trim().isEmpty()) ? null : ruolo.trim();
		
		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE (? IS NULL OR ruolo = ?)";
		
		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, ruolo);
			ps.setString(2, ruolo);


			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	public synchronized boolean updatePassword(int idUtente, String newPassword) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME + " SET password = ? WHERE id_utente = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, newPassword);
			ps.setInt(2, idUtente);

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean updateRuolo(int idUtente, Ruolo ruolo) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME + " SET ruolo = ? WHERE id_utente = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ruolo.name());
			ps.setInt(2, idUtente);

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized int doCountUtenti(String ruolo) throws SQLException {

		String sql = "SELECT COUNT(*) " + "FROM " + TABLE_NAME + " " + "WHERE (? IS NULL OR ruolo = ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ruolo);
			ps.setString(2, ruolo);

			try (ResultSet rs = ps.executeQuery()) {

				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		return 0;
	}

	public synchronized boolean setUtenteRuolo(int idUtente, Ruolo ruolo) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME + " SET ruolo=? WHERE id_utente=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ruolo.name());
			ps.setInt(2, idUtente);

			return ps.executeUpdate() > 0;
		}
	}

}
