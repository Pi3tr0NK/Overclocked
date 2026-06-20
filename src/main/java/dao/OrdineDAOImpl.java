package dao;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import model.CarrelloBean;
import model.CarrelloItemBean;
import model.OrdineBean;
import model.OrdineBean.Stato;
import model.ProdottoBean;
import model.UtenteBean;

public class OrdineDAOImpl implements OrdineDAO {

	private static final String TABLE_NAME = "ordine";
	private DataSource ds = null;

	public OrdineDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(OrdineBean ordine) throws SQLException {

		String sql = "INSERT INTO " + TABLE_NAME + " (data, stato, totale, pagamento, fk_utente, fk_indirizzo) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(ordine.getData()));
			ps.setString(2, ordine.getStato().name());
			ps.setDouble(3, ordine.getTotale());
			ps.setString(4, ordine.getPagamento());
			ps.setInt(5, ordine.getUtente().getIdUtente());
			ps.setInt(6, ordine.getIndirizzo().getIdIndirizzo());

			ps.executeUpdate();
		}
	}

	public synchronized OrdineBean doRetrieveByKey(int idOrdine) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idOrdine);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs, new IndirizzoDAOImpl(ds), new UtenteDAOImpl(ds));
				}
			}
		}
		return null;
	}

	public synchronized int doSaveOrdineCompleto(CarrelloBean cart, UtenteBean utente, int idIndirizzo, String pagamento)
			throws SQLException {

		double totale = 0;
		for (CarrelloItemBean item : cart.getItems()) {
			double prezzoScontato = item.getProdotto().getPrezzo() * (1.0 - item.getProdotto().getSconto() / 100.0);
			totale += prezzoScontato * item.getQuantita();
		}

		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			int idOrdine;
			String sqlOrdine = "INSERT INTO " + TABLE_NAME
					+ " (data, stato, totale, pagamento, fk_utente, fk_indirizzo) " + "VALUES (?, ?, ?, ?, ?, ?)";

			try (PreparedStatement ps = con.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS)) {
				ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
				ps.setString(2, Stato.IN_PREPARAZIONE.name());
				ps.setDouble(3, totale);
				ps.setString(4, pagamento);
				ps.setInt(5, utente.getIdUtente());
				ps.setInt(6, idIndirizzo);
				ps.executeUpdate();
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next())
						idOrdine = rs.getInt(1);
					else
						throw new SQLException("Id ordine non generato");
				}
			}

			String sqlDettaglio = "INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario) "
					+ "VALUES (?, ?, ?, ?)";
			String sqlStock = "UPDATE prodotto SET stock = stock - ? WHERE id_prodotto = ? AND stock >= ?";

			for (CarrelloItemBean item : cart.getItems()) {
				ProdottoBean p = item.getProdotto();
				double prezzoScontato = p.getPrezzo() * (1.0 - p.getSconto() / 100.0);

				try (PreparedStatement ps = con.prepareStatement(sqlDettaglio)) {
					ps.setInt(1, idOrdine);
					ps.setInt(2, p.getIdProdotto());
					ps.setInt(3, item.getQuantita());
					ps.setDouble(4, prezzoScontato);
					ps.executeUpdate();
				}

				try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
					ps.setInt(1, item.getQuantita());
					ps.setInt(2, p.getIdProdotto());
					ps.setInt(3, item.getQuantita());
					int righe = ps.executeUpdate();
					if (righe == 0)
						throw new SQLException("Stock insufficiente per: " + p.getNome());
				}
			}

			con.commit();
			return idOrdine;

		} catch (SQLException e) {
			if (con != null)
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			throw e;
		} finally {
			if (con != null)
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}
	}

	public synchronized Collection<OrdineBean> doRetrieveAll(String nome, String cognome, String email, String stato,
			String dataStart, String dataEnd, int pagina) throws SQLException {

		nome = (nome == null || nome.trim().isEmpty()) ? null : "%" + nome.trim() + "%";
		cognome = (cognome == null || cognome.trim().isEmpty()) ? null : "%" + cognome.trim() + "%";
		email = (email == null || email.trim().isEmpty()) ? null : "%" + email.trim() + "%";
		stato = (stato == null || stato.trim().isEmpty()) ? null : stato.trim();

		dataStart = (dataStart == null || dataStart.trim().isEmpty()) ? null : dataStart.trim();
		dataEnd = (dataEnd == null || dataEnd.trim().isEmpty()) ? null : dataEnd.trim();

		List<OrdineBean> lista = new LinkedList<>();

		String sql = "SELECT o.* " + "FROM " + TABLE_NAME + " o " + "JOIN utente u ON o.fk_utente = u.id_utente "
				+ "WHERE (? IS NULL OR o.stato = ?) " + "AND (? IS NULL OR u.nome LIKE ?) "
				+ "AND (? IS NULL OR u.cognome LIKE ?) " + "AND (? IS NULL OR u.email LIKE ?) "
				+ "AND (? IS NULL OR o.data >= ?) " + "AND (? IS NULL OR o.data <= ?) " + "ORDER BY o.data DESC, o.id_ordine DESC "
				+ "LIMIT ? OFFSET ?";

		try (Connection con = ds.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, stato);
			st.setString(2, stato);

			st.setString(3, nome);
			st.setString(4, nome);

			st.setString(5, cognome);
			st.setString(6, cognome);

			st.setString(7, email);
			st.setString(8, email);

			if (dataStart == null) {
				st.setNull(9, Types.DATE);
				st.setNull(10, Types.DATE);
			} else {
				st.setDate(9, java.sql.Date.valueOf(dataStart));
				st.setDate(10, java.sql.Date.valueOf(dataStart));
			}

			if (dataEnd == null) {
				st.setNull(11, Types.DATE);
				st.setNull(12, Types.DATE);
			} else {
				st.setDate(11, java.sql.Date.valueOf(dataEnd));
				st.setDate(12, java.sql.Date.valueOf(dataEnd));
			}

			int offset = (pagina - 1) * 10;
			st.setInt(13, 10);
			st.setInt(14, offset);

			try (ResultSet rs = st.executeQuery()) {

				IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
				UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

				while (rs.next()) {
					lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
				}
			}
		}

		return lista;
	}

	public synchronized Collection<OrdineBean> doRetrieveAllByUser(int idUser, int pagina) throws SQLException {

		List<OrdineBean> lista = new LinkedList<>();
		String sql = "SELECT * FROM " + TABLE_NAME
				+ " WHERE fk_utente = ? AND stato != 'RIMBORSATO' ORDER BY data DESC";
		if (pagina > 0) {
			sql += " LIMIT ? OFFSET ?";
		}

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idUser);

			if (pagina > 0) {

				int offset = (pagina - 1) * 10;

				ps.setInt(2, 10);
				ps.setInt(3, offset);
			}

			IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
			UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
				}
			}
		}
		return lista;
	}

	public synchronized int doCountFilteredProducts(String nome, String cognome, String email, String stato,
			String dataStart, String dataEnd) throws SQLException {

		nome = (nome == null || nome.trim().isEmpty()) ? null : "%" + nome.trim() + "%";
		cognome = (cognome == null || cognome.trim().isEmpty()) ? null : "%" + cognome.trim() + "%";
		email = (email == null || email.trim().isEmpty()) ? null : "%" + email.trim() + "%";
		stato = (stato == null || stato.trim().isEmpty()) ? null : stato.trim();

		dataStart = (dataStart == null || dataStart.trim().isEmpty()) ? null : dataStart.trim();
		dataEnd = (dataEnd == null || dataEnd.trim().isEmpty()) ? null : dataEnd.trim();

		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " o JOIN utente u ON o.fk_utente = u.id_utente "
				+ "WHERE (? IS NULL OR o.stato = ?) " + "AND (? IS NULL OR u.nome LIKE ?) "
				+ "AND (? IS NULL OR u.cognome LIKE ?) " + "AND (? IS NULL OR u.email LIKE ?) "
				+ "AND (? IS NULL OR o.data >= ?) " + "AND (? IS NULL OR o.data <= ?) " + "ORDER BY o.data DESC";

		try (Connection con = ds.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

			st.setString(1, stato);
			st.setString(2, stato);

			st.setString(3, nome);
			st.setString(4, nome);

			st.setString(5, cognome);
			st.setString(6, cognome);

			st.setString(7, email);
			st.setString(8, email);

			if (dataStart == null) {
				st.setNull(9, Types.DATE);
				st.setNull(10, Types.DATE);
			} else {
				st.setDate(9, java.sql.Date.valueOf(dataStart));
				st.setDate(10, java.sql.Date.valueOf(dataStart));
			}

			if (dataEnd == null) {
				st.setNull(11, Types.DATE);
				st.setNull(12, Types.DATE);
			} else {
				st.setDate(11, java.sql.Date.valueOf(dataEnd));
				st.setDate(12, java.sql.Date.valueOf(dataEnd));
			}

			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			return 0;
		}
	}

	public synchronized Collection<OrdineBean> doRetrieveResiByUser(int idUser) throws SQLException {

		List<OrdineBean> lista = new LinkedList<>();
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE fk_utente = ? AND stato = 'RIMBORSATO' ORDER BY data DESC";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idUser);

			IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
			UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
				}
			}
		}
		return lista;
	}

	public synchronized boolean doRimborsa(int idOrdine, int idUtente) throws SQLException {

		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);

			String sqlCheck = "SELECT id_ordine, data FROM " + TABLE_NAME
					+ " WHERE id_ordine = ? AND fk_utente = ? AND stato != 'RIMBORSATO'";

			LocalDate dataOrdine;
			try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
				ps.setInt(1, idOrdine);
				ps.setInt(2, idUtente);
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next())
						return false;
					dataOrdine = rs.getDate("data").toLocalDate();
				}
			}

			if (dataOrdine.isBefore(LocalDate.now().minusDays(30))) {
				return false;
			}

			String sqlDettagli = "SELECT fk_prodotto, quantita FROM dettagliOrdine WHERE fk_ordine = ?";
			String sqlStock = "UPDATE prodotto SET stock = stock + ? WHERE id_prodotto = ?";

			try (PreparedStatement psD = con.prepareStatement(sqlDettagli)) {
				psD.setInt(1, idOrdine);
				try (ResultSet rs = psD.executeQuery()) {
					while (rs.next()) {
						try (PreparedStatement psS = con.prepareStatement(sqlStock)) {
							psS.setInt(1, rs.getInt("quantita"));
							psS.setInt(2, rs.getInt("fk_prodotto"));
							psS.executeUpdate();
						}
					}
				}
			}

			String sqlUpdate = "UPDATE " + TABLE_NAME + " SET stato = 'RIMBORSATO' WHERE id_ordine = ?";
			try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
				ps.setInt(1, idOrdine);
				ps.executeUpdate();
			}

			con.commit();
			return true;

		} catch (SQLException e) {
			if (con != null)
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			throw e;
		} finally {
			if (con != null)
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}
	}

	public synchronized boolean doUpdate(OrdineBean o) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME
				+ " SET data=?, stato=?, totale=?, pagamento=?, fk_utente=?, fk_indirizzo=?" + " WHERE id_ordine=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(o.getData()));
			ps.setString(2, o.getStato().name());
			ps.setDouble(3, o.getTotale());
			ps.setString(4, o.getPagamento());
			ps.setInt(5, o.getUtente().getIdUtente());
			ps.setInt(6, o.getIndirizzo().getIdIndirizzo());
			ps.setInt(7, o.getIdOrdine());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setOrdineStatus(int idOrdine, Stato stato) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME + " SET stato=? WHERE id_ordine=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, stato.name());
			ps.setInt(2, idOrdine);

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized int doCountByLastMonth() throws SQLException {

		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE data >= DATE_SUB(NOW(), INTERVAL 1 MONTH)";

		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next())
				return rs.getInt(1);
		}
		return 0;
	}
	
	public synchronized int doCount(String stato) throws SQLException {

		boolean filtra = stato != null && !stato.isBlank();

		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + (filtra ? " WHERE stato = ?" : "");

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			if (filtra) {
				ps.setString(1, stato);
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}

		return 0;
	}

	private OrdineBean mapRow(ResultSet rs, IndirizzoDAOImpl indirizzoDAO, UtenteDAOImpl utenteDAO)
			throws SQLException {

		OrdineBean o = new OrdineBean();
		o.setIdOrdine(rs.getInt("id_ordine"));
		o.setData(rs.getDate("data").toLocalDate());
		o.setStato(Stato.valueOf(rs.getString("stato")));
		o.setTotale(rs.getDouble("totale"));
		o.setPagamento(rs.getString("pagamento"));
		o.setIndirizzo(indirizzoDAO.doRetrieveByKey(rs.getInt("fk_indirizzo")));
		o.setUtente(utenteDAO.doRetrieveByKey(rs.getInt("fk_utente")));

		return o;

	}
}