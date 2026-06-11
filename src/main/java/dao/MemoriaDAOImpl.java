package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.MemoriaBean;
import model.MoboBean;
import model.MemoriaBean.Tecnologia;
import model.MemoriaBean.Tipo;

public class MemoriaDAOImpl implements MemoriaDAO {

	private static final String TABLE_NAME = "memoria";

	private DataSource ds;

	public MemoriaDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(MemoriaBean mem) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		int id = prodottoDAO.doSave(mem);

		String sql = "INSERT INTO " + TABLE_NAME
				+ " (capacita, vel_scrittura, vel_lettura, tipo, tecnologia, formato, fk_prodotto) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, mem.getCapacita());
			ps.setInt(2, mem.getVelLettura());
			ps.setInt(3, mem.getVelScrittura());
			ps.setString(4, mem.getTipo().name());
			ps.setString(5, mem.getTecnologia().name());
			ps.setString(6, mem.getFormato());
			ps.setInt(7, id);

			ps.executeUpdate();
		}
	}

	public synchronized MemoriaBean doRetrieveByKey(int idMemoria) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME
				+ " m JOIN prodotto p ON m.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idMemoria);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				MemoriaBean mem = new MemoriaBean();

				mem.setIdProdotto(rs.getInt("id_prodotto"));
				mem.setNome(rs.getString("nome"));
				mem.setModello(rs.getString("modello"));
				mem.setDescrizione(rs.getString("descrizione"));
				mem.setMarca(rs.getString("marca"));
				mem.setPrezzo(rs.getDouble("prezzo"));
				mem.setStock(rs.getInt("stock"));
				mem.setDimensioni(rs.getString("dimensioni"));
				mem.setPeso(rs.getString("peso"));
				mem.setAttivo(rs.getBoolean("attivo"));
				mem.setSconto(rs.getInt("sconto"));
				mem.setCategoria(rs.getString("categoria"));
				mem.setImmagini(immaginiDAO.doRetrieveByProdotto(mem.getIdProdotto()));

				mem.setIdMemoria(rs.getInt("id_memoria"));
				mem.setCapacita(rs.getString("capacita"));
				mem.setVelLettura(rs.getInt("vel_lettura"));
				mem.setVelScrittura(rs.getInt("vel_scrittura"));
				mem.setTipo(Tipo.valueOf(rs.getString("tipo")));
				mem.setTecnologia(Tecnologia.valueOf(rs.getString("tecnologia")));
				mem.setFormato(rs.getString("formato"));
				return mem;
			}
		}

		return null;
	}

	@Override
	public synchronized Collection<MemoriaBean> doRetrieveAll(String cerca, String categoria, String prezzo,
			String marca, String capacita, String tipo, String tecnologia, int pagina) throws SQLException {

		List<MemoriaBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		capacita = (capacita == null || capacita.trim().isEmpty()) ? null : capacita;
		tipo = (tipo == null || tipo.trim().isEmpty()) ? null : tipo;
		tecnologia = (tecnologia == null || tecnologia.trim().isEmpty()) ? null : tecnologia;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		String sql = "SELECT * " + "FROM memoria m " + "JOIN prodotto p ON m.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) "
				+ "AND (? IS NULL OR m.capacita = ?) " + "AND (? IS NULL OR m.tipo = ?) "
				+ "AND (? IS NULL OR m.tecnologia = ?) "
				+ "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?))" + "LIMIT ? OFFSET ?";
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

			ps.setString(7, capacita);
			ps.setString(8, capacita);

			ps.setString(9, tipo);
			ps.setString(10, tipo);

			ps.setString(11, tecnologia);
			ps.setString(12, tecnologia);

			ps.setString(13, cerca);
			ps.setString(14, cerca);
			ps.setString(15, cerca);
			ps.setString(16, cerca);

			int offset = (pagina - 1) * 10;

			ps.setInt(17, 10);
			ps.setInt(18, offset);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					MemoriaBean mem = new MemoriaBean();

					mem.setIdProdotto(rs.getInt("id_prodotto"));
					mem.setNome(rs.getString("nome"));
					mem.setModello(rs.getString("modello"));
					mem.setDescrizione(rs.getString("descrizione"));
					mem.setMarca(rs.getString("marca"));
					mem.setPrezzo(rs.getDouble("prezzo"));
					mem.setStock(rs.getInt("stock"));
					mem.setDimensioni(rs.getString("dimensioni"));
					mem.setPeso(rs.getString("peso"));
					mem.setAttivo(rs.getBoolean("attivo"));
					mem.setSconto(rs.getInt("sconto"));
					mem.setCategoria(rs.getString("categoria"));
					mem.setImmagini(immaginiDAO.doRetrieveByProdotto(mem.getIdProdotto()));

					mem.setIdMemoria(rs.getInt("id_memoria"));
					mem.setCapacita(rs.getString("capacita"));
					mem.setVelLettura(rs.getInt("vel_lettura"));
					mem.setVelScrittura(rs.getInt("vel_scrittura"));
					mem.setTipo(Tipo.valueOf(rs.getString("tipo")));
					mem.setTecnologia(Tecnologia.valueOf(rs.getString("tecnologia")));
					mem.setFormato(rs.getString("formato"));

					lista.add(mem);
				}
			}
		}

		return lista;
	}

	public boolean doUpdate(MemoriaBean mem) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		prodottoDAO.doUpdate(mem);

		String sql = "UPDATE " + TABLE_NAME
				+ " SET capacita = ?, vel_scrittura = ?, vel_lettura = ?, tipo = ?, tecnologia = ?, formato = ? WHERE id_case = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, mem.getCapacita());
			ps.setInt(2, mem.getVelLettura());
			ps.setInt(3, mem.getVelScrittura());
			ps.setString(4, mem.getTipo().name());
			ps.setString(5, mem.getTecnologia().name());
			ps.setString(6, mem.getFormato());
			ps.setInt(7, mem.getIdProdotto());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setProductStatus(MemoriaBean mem, boolean attivo) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

		return prodottoDAO.setProductStatus(mem.getIdProdotto(), attivo);
	}

	public Collection<MemoriaBean> memoriaCompatibili(int moboId) throws SQLException {

		List<MemoriaBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		MoboDAOImpl moboDAO = new MoboDAOImpl(ds);

		MoboBean mobo = moboDAO.doRetrieveByKey(moboId);

		String sql = "SELECT * " + "FROM memoria m " + "JOIN prodotto p ON m.fk_prodotto = p.id_prodotto "
				+ "WHERE p.attivo = true " + "AND (" + "   (? = true AND m.tecnologia = 'NVME') "
				+ "   OR (? > 0 AND m.tecnologia = 'SATA')" + ")";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setBoolean(1, mobo.isNvme());
			ps.setInt(2, mobo.getPorteSata());

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					MemoriaBean mem = new MemoriaBean();

					mem.setIdProdotto(rs.getInt("id_prodotto"));
					mem.setNome(rs.getString("nome"));
					mem.setModello(rs.getString("modello"));
					mem.setDescrizione(rs.getString("descrizione"));
					mem.setMarca(rs.getString("marca"));
					mem.setPrezzo(rs.getDouble("prezzo"));
					mem.setStock(rs.getInt("stock"));
					mem.setDimensioni(rs.getString("dimensioni"));
					mem.setPeso(rs.getString("peso"));
					mem.setAttivo(rs.getBoolean("attivo"));
					mem.setSconto(rs.getInt("sconto"));
					mem.setCategoria(rs.getString("categoria"));
					mem.setImmagini(immaginiDAO.doRetrieveByProdotto(mem.getIdProdotto()));

					mem.setIdMemoria(rs.getInt("id_memoria"));
					mem.setCapacita(rs.getString("capacita"));
					mem.setVelLettura(rs.getInt("vel_lettura"));
					mem.setVelScrittura(rs.getInt("vel_scrittura"));
					mem.setTipo(Tipo.valueOf(rs.getString("tipo")));
					mem.setTecnologia(Tecnologia.valueOf(rs.getString("tecnologia")));
					mem.setFormato(rs.getString("formato"));

					lista.add(mem);
				}
			}
		}

		return lista;
	}

	public int doCountFilteredProducts(String cerca, String categoria, String prezzo, String marca, String capacita,
			String tipo, String tecnologia) throws SQLException {

		List<MemoriaBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		capacita = (capacita == null || capacita.trim().isEmpty()) ? null : capacita;
		tipo = (tipo == null || tipo.trim().isEmpty()) ? null : tipo;
		tecnologia = (tecnologia == null || tecnologia.trim().isEmpty()) ? null : tecnologia;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		String sql = "SELECT COUNT(*) " + "FROM memoria m " + "JOIN prodotto p ON m.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) "
				+ "AND (? IS NULL OR m.capacita = ?) " + "AND (? IS NULL OR m.tipo = ?) "
				+ "AND (? IS NULL OR m.tecnologia = ?) "
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

			ps.setString(7, capacita);
			ps.setString(8, capacita);

			ps.setString(9, tipo);
			ps.setString(10, tipo);

			ps.setString(11, tecnologia);
			ps.setString(12, tecnologia);

			ps.setString(13, cerca);
			ps.setString(14, cerca);
			ps.setString(15, cerca);
			ps.setString(16, cerca);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
			return 0;
		}
	}
}
