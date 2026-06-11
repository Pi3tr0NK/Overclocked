package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.ChassisBean;
import model.MoboBean;

public class ChassisDAOImpl implements ChassisDAO {
	private static final String TABLE_NAME = "chassis";

	private DataSource ds;

	public ChassisDAOImpl(DataSource ds) {
		this.ds = ds;
	}

	public synchronized void doSave(ChassisBean chassis) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		int id = prodottoDAO.doSave(chassis);

		String sql = "INSERT INTO " + TABLE_NAME + " (formato, colore, materiale, fk_prodotto) VALUES (?, ?, ?, ?)";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, chassis.getFormato());
			ps.setString(2, chassis.getColore());
			ps.setString(3, chassis.getMateriale());
			ps.setInt(4, id);

			ps.executeUpdate();
		}
	}

	public synchronized ChassisBean doRetrieveByKey(int idChassis) throws SQLException {

		String sql = "SELECT * FROM " + TABLE_NAME
				+ " c JOIN prodotto p ON c.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idChassis);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				ChassisBean chassis = new ChassisBean();

				chassis.setIdProdotto(rs.getInt("id_prodotto"));
				chassis.setNome(rs.getString("nome"));
				chassis.setModello(rs.getString("modello"));
				chassis.setDescrizione(rs.getString("descrizione"));
				chassis.setMarca(rs.getString("marca"));
				chassis.setPrezzo(rs.getDouble("prezzo"));
				chassis.setStock(rs.getInt("stock"));
				chassis.setDimensioni(rs.getString("dimensioni"));
				chassis.setPeso(rs.getString("peso"));
				chassis.setAttivo(rs.getBoolean("attivo"));
				chassis.setSconto(rs.getInt("sconto"));
				chassis.setCategoria(rs.getString("categoria"));
				chassis.setImmagini(immaginiDAO.doRetrieveByProdotto(chassis.getIdProdotto()));

				chassis.setIdCase(rs.getInt("id_case"));
				chassis.setFormato(rs.getString("formato"));
				chassis.setColore(rs.getString("colore"));
				chassis.setMateriale(rs.getString("materiale"));

				return chassis;
			}
		}

		return null;
	}

	@Override
	public synchronized Collection<ChassisBean> doRetrieveAll(String cerca, String categoria, String prezzo,
			String marca, String formato, String colore, int pagina) throws SQLException {

		List<ChassisBean> lista = new LinkedList<>();
		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
		marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
		formato = (formato == null || formato.trim().isEmpty()) ? null : formato;
		colore = (colore == null || colore.trim().isEmpty()) ? null : colore;

		Double prezzoMax = null;
		if (prezzo != null && !prezzo.trim().isEmpty()) {
			prezzoMax = Double.parseDouble(prezzo);
		}

		String sql = "SELECT * " + "FROM chassis ch " + "JOIN prodotto p ON ch.fk_prodotto = p.id_prodotto "
				+ "WHERE (? IS NULL OR p.categoria = ?) " + "AND (? IS NULL OR p.marca = ?) "
				+ "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) "
				+ "AND (? IS NULL OR ch.formato = ?) " + "AND (? IS NULL OR ch.colore = ?) "
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

			ps.setString(7, formato);
			ps.setString(8, formato);

			ps.setString(9, colore);
			ps.setString(10, colore);

			ps.setString(11, cerca);
			ps.setString(12, cerca);
			ps.setString(13, cerca);
			ps.setString(14, cerca);

			int offset = (pagina - 1) * 10;

			ps.setInt(15, 10);
			ps.setInt(16, offset);

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ChassisBean chassis = new ChassisBean();

					chassis.setIdProdotto(rs.getInt("id_prodotto"));
					chassis.setNome(rs.getString("nome"));
					chassis.setModello(rs.getString("modello"));
					chassis.setDescrizione(rs.getString("descrizione"));
					chassis.setMarca(rs.getString("marca"));
					chassis.setPrezzo(rs.getDouble("prezzo"));
					chassis.setStock(rs.getInt("stock"));
					chassis.setDimensioni(rs.getString("dimensioni"));
					chassis.setPeso(rs.getString("peso"));
					chassis.setAttivo(rs.getBoolean("attivo"));
					chassis.setSconto(rs.getInt("sconto"));
					chassis.setCategoria(rs.getString("categoria"));
					chassis.setImmagini(immaginiDAO.doRetrieveByProdotto(chassis.getIdProdotto()));

					chassis.setIdCase(rs.getInt("id_case"));
					chassis.setFormato(rs.getString("formato"));
					chassis.setColore(rs.getString("colore"));
					chassis.setMateriale(rs.getString("materiale"));

					lista.add(chassis);
				}
			}
		}

		return lista;
	}

	public boolean doUpdate(ChassisBean chassis) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		prodottoDAO.doUpdate(chassis);

		String sql = "UPDATE " + TABLE_NAME + " SET formato = ?, colore = ?, materiale = ? WHERE id_case = ?";

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, chassis.getFormato());
			ps.setString(2, chassis.getColore());
			ps.setString(3, chassis.getMateriale());
			ps.setInt(4, chassis.getIdCase());

			return ps.executeUpdate() > 0;
		}
	}

	public synchronized boolean setProductStatus(ChassisBean chassis, boolean attivo) throws SQLException {

		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

		return prodottoDAO.setProductStatus(chassis.getIdProdotto(), attivo);
	}

	public Collection<ChassisBean> chassisCompatibili(int moboId) throws SQLException {

		Collection<ChassisBean> lista = new LinkedList<>();

		MoboDAOImpl moboDAO = new MoboDAOImpl(ds);

		MoboBean mobo = moboDAO.doRetrieveByKey(moboId);

		String sql = "SELECT * " + "FROM chassis ch " + "JOIN prodotto p ON ch.fk_prodotto = p.id_prodotto "
				+ "WHERE ch.formato = ? " + "AND p.attivo = true";

		ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);

		try (Connection con = ds.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, mobo.getFormato());

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {

					ChassisBean chassis = new ChassisBean();

					chassis.setIdProdotto(rs.getInt("id_prodotto"));
					chassis.setNome(rs.getString("nome"));
					chassis.setModello(rs.getString("modello"));
					chassis.setDescrizione(rs.getString("descrizione"));
					chassis.setMarca(rs.getString("marca"));
					chassis.setPrezzo(rs.getDouble("prezzo"));
					chassis.setStock(rs.getInt("stock"));
					chassis.setDimensioni(rs.getString("dimensioni"));
					chassis.setPeso(rs.getString("peso"));
					chassis.setAttivo(rs.getBoolean("attivo"));
					chassis.setSconto(rs.getInt("sconto"));
					chassis.setCategoria(rs.getString("categoria"));
					chassis.setImmagini(immaginiDAO.doRetrieveByProdotto(chassis.getIdProdotto()));

					chassis.setIdCase(rs.getInt("id_case"));
					chassis.setFormato(rs.getString("formato"));
					chassis.setColore(rs.getString("colore"));
					chassis.setMateriale(rs.getString("materiale"));

					lista.add(chassis);
				}
			}
		}

		return lista;
	}

	@Override
	public synchronized int doCountFilteredProducts(String cerca, String categoria, String prezzo, String marca, String formato, String colore) 
			throws SQLException {

	    List<ChassisBean> lista = new LinkedList<>();
	    ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);
	    
	    categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
	    marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
	    formato = (formato == null || formato.trim().isEmpty()) ? null : formato;
	    colore = (colore == null || colore.trim().isEmpty()) ? null : colore;

	    Double prezzoMax = null;
	    if (prezzo != null && !prezzo.trim().isEmpty()) {
	        prezzoMax = Double.parseDouble(prezzo);
	    }
	    
	    String sql =
	        "SELECT COUNT(*) " +
	        "FROM chassis ch " +
	        "JOIN prodotto p ON ch.fk_prodotto = p.id_prodotto " +
	        "WHERE (? IS NULL OR p.categoria = ?) " +
	        "AND (? IS NULL OR p.marca = ?) " +
	        "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " +
	        "AND (? IS NULL OR ch.formato = ?) " +
	        "AND (? IS NULL OR ch.colore = ?) " +
	        "AND (? IS NULL OR (p.nome LIKE ? OR p.descrizione LIKE ? OR p.modello LIKE ?))";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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

	        ps.setString(7, formato);
	        ps.setString(8, formato);

	        ps.setString(9, colore);
	        ps.setString(10, colore);
	        
	        ps.setString(11, cerca);
            ps.setString(12, cerca);
            ps.setString(13, cerca);
            ps.setString(14, cerca);


            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
	    }
		return 0;
	}
}
