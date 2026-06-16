package dao;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.CPUBean;

public class CPUDAOImpl implements CPUDAO {
	private static final String TABLE_NAME = "cpu";

	private DataSource ds;

	public CPUDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(CPUBean cpu) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		int id = prodottoDAO.doSave(cpu);

		String sql = "INSERT INTO " + TABLE_NAME
				+ " (core, thread, frequenza, frequenza_ram, tiporam, socket, tdp, fk_prodotto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, cpu.getCore());
			ps.setInt(2, cpu.getThread());
			ps.setString(3, cpu.getFrequenza());
			ps.setString(4, cpu.getFrequenza_ram());
			ps.setString(5, cpu.getTiporam());
			ps.setString(6, cpu.getSocket());
			ps.setInt(7, cpu.getTdp());
			ps.setInt(8, id);

			ps.executeUpdate();
		}
	}

	public synchronized CPUBean doRetrieveByKey(int idCPU) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME
				+ " c JOIN prodotto p ON c.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idCPU);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				CPUBean cpu = new CPUBean();

				cpu.setIdProdotto(rs.getInt("id_prodotto"));
				cpu.setNome(rs.getString("nome"));
				cpu.setModello(rs.getString("modello"));
				cpu.setDescrizione(rs.getString("descrizione"));
				cpu.setMarca(rs.getString("marca"));
				cpu.setPrezzo(rs.getDouble("prezzo"));
				cpu.setStock(rs.getInt("stock"));
				cpu.setDimensioni(rs.getString("dimensioni"));
				cpu.setPeso(rs.getString("peso"));
				cpu.setAttivo(rs.getBoolean("attivo"));
				cpu.setSconto(rs.getInt("sconto"));
				cpu.setCategoria(rs.getString("categoria"));
				cpu.setImmagini(immaginiDAO.doRetrieveByProdotto(cpu.getIdProdotto()));

				cpu.setIdCpu(rs.getInt("id_cpu"));
				cpu.setCore(rs.getInt("core"));
				cpu.setThread(rs.getInt("thread"));
				cpu.setFrequenza(rs.getString("frequenza"));
				cpu.setFrequenza_ram(rs.getString("frequenza_ram"));
				cpu.setTiporam(rs.getString("tiporam"));
				cpu.setSocket(rs.getString("socket"));
				cpu.setTdp(rs.getInt("tdp"));

				return cpu;
			}
		}

		return null;
	}

	@Override
	public synchronized Collection<CPUBean> doRetrieveAll(String cerca, String categoria, String prezzo, String marca,
			String core, String frequenza, int pagina) throws SQLException {

		List<CPUBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		frequenza = (frequenza == null || frequenza.trim().isEmpty()) ? null : frequenza;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		Integer coreInt = null;
		if (core != null && !core.trim().isEmpty()) {
			coreInt = Integer.parseInt(core);
		}

		String sql = "SELECT * " + "FROM " + TABLE_NAME + " c " + "JOIN prodotto p ON c.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " + "AND (? IS NULL OR c.core = ?) "
				+ "AND (? IS NULL OR c.frequenza = ?) " + "AND attivo = true "
				+ "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?))";

		if (pagina > 0) {
			sql += " LIMIT ? OFFSET ?";
		}

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

			if (coreInt == null) {
				ps.setNull(7, java.sql.Types.INTEGER);
				ps.setNull(8, java.sql.Types.INTEGER);
			} else {
				ps.setInt(7, coreInt);
				ps.setInt(8, coreInt);
			}

			ps.setString(9, frequenza);
			ps.setString(10, frequenza);

			ps.setString(11, cerca);
			ps.setString(12, cerca);
			ps.setString(13, cerca);
			ps.setString(14, cerca);

			if (pagina > 0) {

				int offset = (pagina - 1) * 10;

				ps.setInt(15, 10);
				ps.setInt(16, offset);
			}

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					CPUBean cpu = new CPUBean();

					cpu.setIdProdotto(rs.getInt("id_prodotto"));
					cpu.setNome(rs.getString("nome"));
					cpu.setModello(rs.getString("modello"));
					cpu.setDescrizione(rs.getString("descrizione"));
					cpu.setMarca(rs.getString("marca"));
					cpu.setPrezzo(rs.getDouble("prezzo"));
					cpu.setStock(rs.getInt("stock"));
					cpu.setDimensioni(rs.getString("dimensioni"));
					cpu.setPeso(rs.getString("peso"));
					cpu.setAttivo(rs.getBoolean("attivo"));
					cpu.setSconto(rs.getInt("sconto"));
					cpu.setCategoria(rs.getString("categoria"));
					cpu.setImmagini(immaginiDAO.doRetrieveByProdotto(cpu.getIdProdotto()));

					cpu.setIdCpu(rs.getInt("id_cpu"));
					cpu.setCore(rs.getInt("core"));
					cpu.setThread(rs.getInt("thread"));
					cpu.setFrequenza(rs.getString("frequenza"));
					cpu.setFrequenza_ram(rs.getString("frequenza_ram"));
					cpu.setTiporam(rs.getString("tiporam"));
					cpu.setSocket(rs.getString("socket"));
					cpu.setTdp(rs.getInt("tdp"));

					lista.add(cpu);
				}
			}
		}

		return lista;
	}

	public synchronized boolean doUpdate(CPUBean cpu) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		prodottoDAO.doUpdate(cpu);

		String sql = "UPDATE " + TABLE_NAME + " SET core = ?, thread = ?, frequenza = ?, frequenza_ram = ?, tiporam = ?, socket = ?, tdp = ? WHERE id_cpu = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, cpu.getCore());
			ps.setInt(2, cpu.getThread());
			ps.setString(3, cpu.getFrequenza());
			ps.setString(4, cpu.getFrequenza_ram());
			ps.setString(5, cpu.getTiporam());
			ps.setString(6, cpu.getSocket());
			ps.setInt(7, cpu.getTdp());
			ps.setInt(8, cpu.getIdCpu());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setProductStatus(CPUBean cpu, boolean attivo) throws SQLException {
		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

		return prodottoDAO.setProductStatus(cpu.getIdProdotto(), attivo);
	}

	@Override
	public synchronized int doCountFilteredProducts(String cerca, String categoria, String prezzo, String marca,
			String core, String frequenza) throws SQLException {

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		frequenza = (frequenza == null || frequenza.trim().isEmpty()) ? null : frequenza;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		Integer coreInt = null;
		if (core != null && !core.trim().isEmpty()) {
			coreInt = Integer.parseInt(core);
		}

		String sql = "SELECT COUNT(*) " + "FROM " + TABLE_NAME + " c " + "JOIN prodotto p ON c.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " + "AND (? IS NULL OR c.core = ?) "
				+ "AND (? IS NULL OR c.frequenza = ?) " + "AND attivo = true "
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

			if (coreInt == null) {
				ps.setNull(7, java.sql.Types.INTEGER);
				ps.setNull(8, java.sql.Types.INTEGER);
			} else {
				ps.setInt(7, coreInt);
				ps.setInt(8, coreInt);
			}

			ps.setString(9, frequenza);
			ps.setString(10, frequenza);

			ps.setString(11, cerca);
			ps.setString(12, cerca);
			ps.setString(13, cerca);
			ps.setString(14, cerca);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			
		}
		return 0;
	}
	
}
