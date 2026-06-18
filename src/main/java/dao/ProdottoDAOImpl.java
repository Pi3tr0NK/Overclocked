package dao;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import model.ProdottoBean;

public class ProdottoDAOImpl implements ProdottoDAO {
	private static final String TABLE_NAME = "prodotto";
	private DataSource ds = null;

	public ProdottoDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized int doSave(ProdottoBean p) throws SQLException {
		String sql = "INSERT INTO " + TABLE_NAME
				+ " (nome, modello, descrizione, marca, prezzo, stock, attivo, sconto, categoria, dimensioni, peso) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int idGenerato = 0;
		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, p.getNome());
			ps.setString(2, p.getModello());
			ps.setString(3, p.getDescrizione());
			ps.setString(4, p.getMarca());
			ps.setDouble(5, p.getPrezzo());
			ps.setInt(6, p.getStock());
			ps.setBoolean(7, p.isAttivo());
			ps.setInt(8, p.getSconto());
			ps.setString(9, p.getCategoria());
			ps.setString(10, p.getDimensioni());
			ps.setString(11, p.getPeso());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					idGenerato = rs.getInt(1);
					p.setIdProdotto(idGenerato);
				}
			}
		}

		return idGenerato;
	}

	public synchronized ProdottoBean doRetrieveByKey(int id) throws SQLException {

		String sql = "SELECT categoria FROM " + TABLE_NAME + " " + "WHERE id_prodotto = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return null;

			String categoria = rs.getString("categoria");

			switch (categoria) {

			case "STORAGE":
				return new MemoriaDAOImpl(ds).doRetrieveByKey(id);

			case "CPU":
				return new CPUDAOImpl(ds).doRetrieveByKey(id);

			case "GPU":
				return new GPUDAOImpl(ds).doRetrieveByKey(id);

			case "MOBO":
				return new MoboDAOImpl(ds).doRetrieveByKey(id);

			case "RAM":
				return new RAMDAOImpl(ds).doRetrieveByKey(id);

			case "PSU":
				return new PSUDAOImpl(ds).doRetrieveByKey(id);

			case "CASE":
				return new ChassisDAOImpl(ds).doRetrieveByKey(id);

			case "DISSIPATORE":
				return new DissipatoreDAOImpl(ds).doRetrieveByKey(id);

			default:
				break;
			}
		}

		return null;
	}

	@Override
	public synchronized Collection<ProdottoBean> doRetrieveAll(String cerca, String prezzo, String marca,
			String categoria, String attivo, String ordine, int pagina) throws SQLException {

		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		List<ProdottoBean> lista = new LinkedList<>();

		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		attivo = (attivo == null || attivo.trim().isEmpty()) ? null : attivo;
		ordine = (ordine == null || ordine.trim().isEmpty()) ? null : ordine;
		
		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		Boolean attivoBool = null;
		if (attivo != null) {
			attivoBool = Boolean.parseBoolean(attivo);
		}

		String sql = "SELECT * " +
	             "FROM " + TABLE_NAME + " p " +
	             "WHERE (? IS NULL OR p.marca = ?) " +
	             "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " +
	             "AND (? IS NULL OR p.categoria = ?) " +
	             "AND (? IS NULL OR p.attivo = ?) " +
	             "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?)) ";

		if ("prezzoASC".equals(ordine)) {
		    sql += "ORDER BY (p.prezzo * (100 - p.sconto) / 100.0) ASC ";
		} else if ("prezzoDESC".equals(ordine)) {
		    sql += "ORDER BY (p.prezzo * (100 - p.sconto) / 100.0) DESC ";
		} else if ("nomeASC".equals(ordine)) {
		    sql += "ORDER BY p.nome ASC ";
		} else if ("nomeDESC".equals(ordine)) {
		    sql += "ORDER BY p.nome DESC ";
		}
	
		sql += "LIMIT ? OFFSET ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, marca);
			ps.setString(2, marca);

			if (prezzoMax == null) {
				ps.setNull(3, Types.DOUBLE);
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(3, prezzoMax);
				ps.setDouble(4, prezzoMax);
			}

			ps.setString(5, categoria);
			ps.setString(6, categoria);

			if (attivoBool == null) {
				ps.setNull(7, Types.BOOLEAN);
				ps.setNull(8, Types.BOOLEAN);
			} else {
				ps.setBoolean(7, attivoBool);
				ps.setBoolean(8, attivoBool);
			}

			String ricerca = null;
			if (cerca != null && !cerca.trim().isEmpty()) {
				ricerca = "%" + cerca + "%";
			}

			ps.setString(9, ricerca);
			ps.setString(10, ricerca);
			ps.setString(11, ricerca);
			ps.setString(12, ricerca);

			int offset = (pagina - 1) * 10;

			ps.setInt(13, 10);
			ps.setInt(14, offset);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ProdottoBean p = new ProdottoBean();

					p.setIdProdotto(rs.getInt("id_prodotto"));
					p.setNome(rs.getString("nome"));
					p.setModello(rs.getString("modello"));
					p.setDescrizione(rs.getString("descrizione"));
					p.setMarca(rs.getString("marca"));
					p.setPrezzo(rs.getDouble("prezzo"));
					p.setStock(rs.getInt("stock"));
					p.setDimensioni(rs.getString("dimensioni"));
					p.setPeso(rs.getString("peso"));
					p.setAttivo(rs.getBoolean("attivo"));
					p.setSconto(rs.getInt("sconto"));
					p.setCategoria(rs.getString("categoria"));
					p.setImmagini(immaginiDAO.doRetrieveByProdotto(p.getIdProdotto()));

					lista.add(p);
				}
			}
		}

		return lista;
	}

	public synchronized boolean doUpdate(ProdottoBean p) throws SQLException {

		String sql = "UPDATE " + TABLE_NAME
				+ " SET nome=?, modello=?, descrizione=?, marca=?, prezzo=?, stock=?, attivo=?, sconto=?, categoria=? WHERE id_prodotto=?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, p.getNome());
			ps.setString(2, p.getModello());
			ps.setString(3, p.getDescrizione());
			ps.setString(4, p.getMarca());
			ps.setDouble(5, p.getPrezzo());
			ps.setInt(6, p.getStock());
			ps.setBoolean(7, p.isAttivo());
			ps.setInt(8, p.getSconto());
			ps.setString(9, p.getCategoria());
			ps.setInt(10, p.getIdProdotto());
			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setProductStatus(int idProdotto, boolean attivo) throws SQLException {
		String sql = "UPDATE " + TABLE_NAME + " SET attivo = ? WHERE id_prodotto = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, attivo);
			ps.setInt(2, idProdotto);

			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public synchronized Map<String, Integer> doCountProductsByCategory() throws SQLException {

		Map<String, Integer> result = new HashMap<>();

		String sql = "SELECT categoria, COUNT(*) AS totale FROM " + TABLE_NAME + " WHERE attivo = 1 GROUP BY categoria";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				result.put(rs.getString("categoria"), rs.getInt("totale"));
			}
		}

		return result;
	}

	public synchronized List<ProdottoBean> doRetrieveNovita(int limit) throws SQLException {
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		List<ProdottoBean> novita = new ArrayList<>();

		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE attivo = true ORDER BY id_prodotto DESC LIMIT ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, limit);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ProdottoBean p = new ProdottoBean();

					p.setIdProdotto(rs.getInt("id_prodotto"));
					p.setNome(rs.getString("nome"));
					p.setModello(rs.getString("modello"));
					p.setDescrizione(rs.getString("descrizione"));
					p.setMarca(rs.getString("marca"));
					p.setPrezzo(rs.getDouble("prezzo"));
					p.setStock(rs.getInt("stock"));
					p.setAttivo(rs.getBoolean("attivo"));
					p.setSconto(rs.getInt("sconto"));
					p.setCategoria(rs.getString("categoria"));
					p.setImmagini(immaginiDAO.doRetrieveByProdotto(p.getIdProdotto()));

					novita.add(p);
				}
			}
		}

		return novita;
	}

	public synchronized List<ProdottoBean> doRetrieveBestseller(int n) throws SQLException {

		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		List<ProdottoBean> prodotti = new ArrayList<>();

		String sql = "SELECT p.*, SUM(d.quantita) AS totale_venduto " + "FROM " + TABLE_NAME + " p "
				+ "JOIN dettagliOrdine d ON p.id_prodotto = d.fk_prodotto "
				+ "JOIN ordine o ON d.fk_ordine = o.id_ordine " + "WHERE o.data >= CURDATE() - INTERVAL 365 DAY "
				+ "AND p.attivo = true " + "GROUP BY p.id_prodotto " + "ORDER BY totale_venduto DESC " + "LIMIT ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, n);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ProdottoBean p = new ProdottoBean();

					p.setIdProdotto(rs.getInt("id_prodotto"));
					p.setNome(rs.getString("nome"));
					p.setModello(rs.getString("modello"));
					p.setDescrizione(rs.getString("descrizione"));
					p.setMarca(rs.getString("marca"));
					p.setPrezzo(rs.getDouble("prezzo"));
					p.setStock(rs.getInt("stock"));
					p.setDimensioni(rs.getString("dimensioni"));
					p.setPeso(rs.getString("peso"));
					p.setAttivo(rs.getBoolean("attivo"));
					p.setSconto(rs.getInt("sconto"));
					p.setCategoria(rs.getString("categoria"));
					p.setImmagini(immaginiDAO.doRetrieveByProdotto(p.getIdProdotto()));

					prodotti.add(p);
				}
			}
		}

		return prodotti;
	}

	public synchronized List<ProdottoBean> doRetrieveCorrelati(int limit, int idProdotto, String categoria,
			double prezzo) throws SQLException {

		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		List<ProdottoBean> correlati = new ArrayList<>();

		String sql = "SELECT * FROM " + TABLE_NAME + " " + "WHERE attivo = true " + "AND categoria = ? "
				+ "AND prezzo BETWEEN ? AND ? " + "AND id_prodotto <> ? " + "LIMIT ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, categoria);
			ps.setDouble(2, prezzo - 200);
			ps.setDouble(3, prezzo + 200);
			ps.setInt(4, idProdotto);
			ps.setInt(5, limit);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ProdottoBean p = new ProdottoBean();

					p.setIdProdotto(rs.getInt("id_prodotto"));
					p.setNome(rs.getString("nome"));
					p.setModello(rs.getString("modello"));
					p.setDescrizione(rs.getString("descrizione"));
					p.setMarca(rs.getString("marca"));
					p.setPrezzo(rs.getDouble("prezzo"));
					p.setStock(rs.getInt("stock"));
					p.setAttivo(rs.getBoolean("attivo"));
					p.setSconto(rs.getInt("sconto"));
					p.setCategoria(rs.getString("categoria"));
					p.setImmagini(immaginiDAO.doRetrieveByProdotto(p.getIdProdotto()));

					correlati.add(p);
				}
			}
		}

		return correlati;
	}

	public synchronized int doCountProducts() throws SQLException {

		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;

		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	public synchronized int doCountExpiredProducts() throws SQLException {

		String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " " + "WHERE stock<=0";

		try (Connection con = ds.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	public synchronized int doCountFilteredProducts(String cerca, String prezzo, String marca, String categoria,
			String attivo) throws SQLException {

		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		attivo = (attivo == null || attivo.trim().isEmpty()) ? null : attivo;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		Boolean attivoBool = null;
		if (attivo != null) {
			attivoBool = Boolean.parseBoolean(attivo);
		}

		String sql = "SELECT COUNT(*) " + "FROM " + TABLE_NAME + " p " + "WHERE (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) "
				+ "AND (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.attivo = ?) "
				+ "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?))";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, marca);
			ps.setString(2, marca);

			if (prezzoMax == null) {
				ps.setNull(3, Types.DOUBLE);
				ps.setNull(4, Types.DOUBLE);
			} else {
				ps.setDouble(3, prezzoMax);
				ps.setDouble(4, prezzoMax);
			}

			ps.setString(5, categoria);
			ps.setString(6, categoria);

			if (attivoBool == null) {
				ps.setNull(7, Types.BOOLEAN);
				ps.setNull(8, Types.BOOLEAN);
			} else {
				ps.setBoolean(7, attivoBool);
				ps.setBoolean(8, attivoBool);
			}

			String ricercaLike = null;
			if (cerca != null && !cerca.trim().isEmpty()) {
				ricercaLike = "%" + cerca + "%";
			}

			ps.setString(9, ricercaLike);
			ps.setString(10, ricercaLike);
			ps.setString(11, ricercaLike);
			ps.setString(12, ricercaLike);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		return 0;
	}

}