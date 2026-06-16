package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import model.CPUBean;
import model.DissipatoreBean;

public class DissipatoreDAOImpl implements DissipatoreDAO {

	private static final String TABLE_NAME = "dissipatore";
	private DataSource ds = null;

	public DissipatoreDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(DissipatoreBean dissipatore) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		int id = prodottoDAO.doSave(dissipatore);

		String sql = "INSERT INTO " + TABLE_NAME
				+ " (tipo, socket_supportati, dimensioni_ventola, rpm_max, rumore, tdp_supportato, fk_prodotto) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, dissipatore.getTipo().name());
			ps.setString(2, dissipatore.getSocketSupportati());
			ps.setString(3, dissipatore.getDimensioniVentola());
			ps.setInt(4, dissipatore.getRpmMax());
			ps.setInt(5, dissipatore.getRumore());
			ps.setInt(6, dissipatore.getTdpSupportato());
			ps.setInt(7, id);

			ps.executeUpdate();
		}
	}

	public synchronized DissipatoreBean doRetrieveByKey(int idDissipatore) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME
				+ " d JOIN prodotto p ON d.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";

		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idDissipatore);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				DissipatoreBean d = new DissipatoreBean();

				d.setIdProdotto(rs.getInt("id_prodotto"));
				d.setNome(rs.getString("nome"));
				d.setModello(rs.getString("modello"));
				d.setDescrizione(rs.getString("descrizione"));
				d.setMarca(rs.getString("marca"));
				d.setPrezzo(rs.getDouble("prezzo"));
				d.setStock(rs.getInt("stock"));
				d.setDimensioni(rs.getString("dimensioni"));
				d.setPeso(rs.getString("peso"));
				d.setAttivo(rs.getBoolean("attivo"));
				d.setSconto(rs.getInt("sconto"));
				d.setCategoria(rs.getString("categoria"));
				d.setImmagini(immaginiDAO.doRetrieveByProdotto(d.getIdProdotto()));

				d.setIdDissipatore(rs.getInt("id_dissipatore"));
				d.setTipo(DissipatoreBean.Tipo.valueOf(rs.getString("tipo").toUpperCase()));
				d.setSocketSupportati(rs.getString("socket_supportati"));
				d.setDimensioniVentola(rs.getString("dimensioni_ventola"));
				d.setRpmMax(rs.getInt("rpm_max"));
				d.setRumore(rs.getInt("rumore"));
				d.setTdpSupportato(rs.getInt("tdp_supportato"));

				return d;
			}
		}

		return null;
	}

	@Override
	public synchronized Collection<DissipatoreBean> doRetrieveAll(String cerca, String categoria, String prezzo,
			String marca, String tipo, int pagina) throws SQLException {

		List<DissipatoreBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		tipo = (tipo == null || tipo.trim().isEmpty()) ? null : tipo;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		String sql = "SELECT * " + "FROM " + TABLE_NAME + " d " + "JOIN prodotto p ON d.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR p.prezzo <= ?) " + "AND (? IS NULL OR d.tipo = ?) " + "AND attivo = true "
				+ "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?)) "
				+ "LIMIT ? OFFSET ?";
		;

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, categoria);
			ps.setString(2, categoria);

			ps.setString(3, marca);
			ps.setString(4, marca);

			if (prezzoMax == null) {
				ps.setNull(5, java.sql.Types.DOUBLE);
				ps.setNull(6, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(5, prezzoMax);
				ps.setDouble(6, prezzoMax);
			}

			ps.setString(7, tipo);
			ps.setString(8, tipo);

			ps.setString(9, cerca);
			ps.setString(10, cerca);
			ps.setString(11, cerca);
			ps.setString(12, cerca);
			int offset = (pagina - 1) * 10;

			ps.setInt(13, 10);
			ps.setInt(14, offset);
			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					DissipatoreBean d = new DissipatoreBean();

					d.setIdProdotto(rs.getInt("id_prodotto"));
					d.setNome(rs.getString("nome"));
					d.setModello(rs.getString("modello"));
					d.setDescrizione(rs.getString("descrizione"));
					d.setMarca(rs.getString("marca"));
					d.setPrezzo(rs.getDouble("prezzo"));
					d.setStock(rs.getInt("stock"));
					d.setDimensioni(rs.getString("dimensioni"));
					d.setPeso(rs.getString("peso"));
					d.setAttivo(rs.getBoolean("attivo"));
					d.setSconto(rs.getInt("sconto"));
					d.setCategoria(rs.getString("categoria"));
					d.setImmagini(immaginiDAO.doRetrieveByProdotto(d.getIdProdotto()));

					d.setIdDissipatore(rs.getInt("id_dissipatore"));
					d.setTipo(DissipatoreBean.Tipo.valueOf(rs.getString("tipo").toUpperCase()));
					d.setSocketSupportati(rs.getString("socket_supportati"));
					d.setDimensioniVentola(rs.getString("dimensioni_ventola"));
					d.setRpmMax(rs.getInt("rpm_max"));
					d.setRumore(rs.getInt("rumore"));
					d.setTdpSupportato(rs.getInt("tdp_supportato"));

					lista.add(d);
				}
			}
		}

		return lista;
	}

	public synchronized boolean doUpdate(DissipatoreBean dissipatore) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		prodottoDAO.doUpdate(dissipatore);

		String sql = "UPDATE " + TABLE_NAME
				+ " SET tipo = ?, socket_supportati = ?, dimensioni_ventola = ?, rpm_max = ?, rumore = ?, tdp_supportato = ? "
				+ "WHERE id_dissipatore = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, dissipatore.getTipo().name());
			ps.setString(2, dissipatore.getSocketSupportati());
			ps.setString(3, dissipatore.getDimensioniVentola());
			ps.setInt(4, dissipatore.getRpmMax());
			ps.setInt(5, dissipatore.getRumore());
			ps.setInt(6, dissipatore.getTdpSupportato());
			ps.setInt(7, dissipatore.getIdDissipatore());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setProductStatus(DissipatoreBean dissipatore, boolean attivo) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		return prodottoDAO.setProductStatus(dissipatore.getIdProdotto(), attivo);
	}

	public synchronized Collection<DissipatoreBean> dissipatoriCompatibili(int cpuId) throws SQLException {

		List<DissipatoreBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		CPUDAOImpl cpuDAO = new CPUDAOImpl(ds);

		CPUBean cpu = cpuDAO.doRetrieveByKey(cpuId);

		String sql = "SELECT * " + "FROM " + TABLE_NAME + " d " + "JOIN prodotto p ON d.fk_prodotto = p.id_prodotto "
				+ "WHERE p.attivo = true " + "AND d.socket_supportati LIKE ? ";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, "%" + cpu.getSocket() + "%");

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					DissipatoreBean d = new DissipatoreBean();

					d.setIdProdotto(rs.getInt("id_prodotto"));
					d.setNome(rs.getString("nome"));
					d.setModello(rs.getString("modello"));
					d.setDescrizione(rs.getString("descrizione"));
					d.setMarca(rs.getString("marca"));
					d.setPrezzo(rs.getDouble("prezzo"));
					d.setStock(rs.getInt("stock"));
					d.setDimensioni(rs.getString("dimensioni"));
					d.setPeso(rs.getString("peso"));
					d.setAttivo(rs.getBoolean("attivo"));
					d.setSconto(rs.getInt("sconto"));
					d.setCategoria(rs.getString("categoria"));
					d.setImmagini(immaginiDAO.doRetrieveByProdotto(d.getIdProdotto()));

					d.setIdDissipatore(rs.getInt("id_dissipatore"));
					d.setTipo(DissipatoreBean.Tipo.valueOf(rs.getString("tipo").toUpperCase()));
					d.setSocketSupportati(rs.getString("socket_supportati"));
					d.setDimensioniVentola(rs.getString("dimensioni_ventola"));
					d.setRpmMax(rs.getInt("rpm_max"));
					d.setRumore(rs.getInt("rumore"));
					d.setTdpSupportato(rs.getInt("tdp_supportato"));

					lista.add(d);
				}
			}
		}

		return lista;
	}

	@Override
	public synchronized int doCountFilteredProducts(String cerca, String categoria, String prezzo, String marca,
			String tipo) throws SQLException {

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		tipo = (tipo == null || tipo.trim().isEmpty()) ? null : tipo;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		String sql = "SELECT COUNT(*) " + "FROM " + TABLE_NAME + " d "
				+ "JOIN prodotto p ON d.fk_prodotto = p.id_prodotto " + "WHERE (? IS NULL OR p.categoria = ?) "
				+ "AND (? IS NULL OR p.marca = ?) " + "AND (? IS NULL OR p.prezzo <= ?) "
				+ "AND (? IS NULL OR d.tipo = ?) " + "AND attivo = true "
				+ "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?))";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, categoria);
			ps.setString(2, categoria);

			ps.setString(3, marca);
			ps.setString(4, marca);

			if (prezzoMax == null) {
				ps.setNull(5, java.sql.Types.DOUBLE);
				ps.setNull(6, java.sql.Types.DOUBLE);
			} else {
				ps.setDouble(5, prezzoMax);
				ps.setDouble(6, prezzoMax);
			}

			ps.setString(7, tipo);
			ps.setString(8, tipo);

			ps.setString(9, cerca);
			ps.setString(10, cerca);
			ps.setString(11, cerca);
			ps.setString(12, cerca);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			return 0;
		}
	}
}
