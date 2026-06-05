package dao;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.RAMBean;

public class RAMDAOImpl implements RAMDAO{
	private static final String TABLE_NAME = "ram";

	private DataSource ds;

	public RAMDAOImpl(DataSource ds) {
	    this.ds = ds;
	}

	public synchronized void doSave(RAMBean ram) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doSave(ram);

	    String sql = "INSERT INTO " + TABLE_NAME + " (capacita, frequenza, tipo, fk_prodotto) VALUES (?, ?, ?, ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, ram.getCapacita());
	        ps.setString(2, ram.getFrequenza());
	        ps.setString(3, ram.getTipo());
	        ps.setInt(4, ram.getIdProdotto());

	        ps.executeUpdate();
	    }
	}

	public synchronized RAMBean doRetrieveByKey(int idRAM) throws SQLException {

	    String sql = "SELECT * FROM " + TABLE_NAME + " r JOIN prodotto p ON r.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idRAM);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	            RAMBean ram = new RAMBean();

	            ram.setIdProdotto(rs.getInt("id_prodotto"));
	            ram.setNome(rs.getString("nome"));
	            ram.setModello(rs.getString("modello"));
	            ram.setDescrizione(rs.getString("descrizione"));
	            ram.setMarca(rs.getString("marca"));
	            ram.setPrezzo(rs.getDouble("prezzo"));
	            ram.setStock(rs.getInt("stock"));
	            ram.setDimensioni(rs.getString("dimensioni"));
                ram.setPeso(rs.getString("peso"));
	            ram.setAttivo(rs.getBoolean("attivo"));
	            ram.setSconto(rs.getInt("sconto"));
		        ram.setCategoria(rs.getString("categoria"));

	            ram.setIdRam(rs.getInt("id_ram"));
	            ram.setCapacita(rs.getString("capacita"));
	            ram.setFrequenza(rs.getString("frequenza"));
	            ram.setTipo(rs.getString("tipo"));

	            return ram;
	        }
	    }

	    return null;
	}
	
	@Override
	public synchronized Collection<RAMBean> doRetrieveAll(String categoria, String prezzo, String marca, String capacita, String frequenza, String tipo) 
			throws SQLException {

	    List<RAMBean> lista = new LinkedList<>();
	    
	    categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
	    marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
	    capacita = (capacita == null || capacita.trim().isEmpty()) ? null : capacita;
	    frequenza = (frequenza == null || frequenza.trim().isEmpty()) ? null : frequenza;
	    tipo = (tipo == null || tipo.trim().isEmpty()) ? null : tipo;
	    
	    Double prezzoMax = null;
	    if (prezzo != null && !prezzo.trim().isEmpty()) {
	        prezzoMax = Double.parseDouble(prezzo);
	    }

	    String sql =
	        "SELECT * " +
	        "FROM ram r " +
	        "JOIN prodotto p ON r.fk_prodotto = p.id_prodotto " +
	        "WHERE (? IS NULL OR p.categoria = ?) " +
	        "AND (? IS NULL OR p.marca = ?) " +
	        "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " +
	        "AND (? IS NULL OR r.capacita = ?) " +
	        "AND (? IS NULL OR r.frequenza = ?) " +
	        "AND (? IS NULL OR r.tipo = ?)";

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

	        ps.setString(7, capacita);
	        ps.setString(8, capacita);

	        ps.setString(9, frequenza);
	        ps.setString(10, frequenza);

	        ps.setString(11, tipo);
	        ps.setString(12, tipo);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {

	                RAMBean ram = new RAMBean();

	                ram.setIdProdotto(rs.getInt("id_prodotto"));
		            ram.setNome(rs.getString("nome"));
		            ram.setModello(rs.getString("modello"));
		            ram.setDescrizione(rs.getString("descrizione"));
		            ram.setMarca(rs.getString("marca"));
		            ram.setPrezzo(rs.getDouble("prezzo"));
		            ram.setStock(rs.getInt("stock"));
		            ram.setDimensioni(rs.getString("dimensioni"));
                    ram.setPeso(rs.getString("peso"));
		            ram.setAttivo(rs.getBoolean("attivo"));
		            ram.setSconto(rs.getInt("sconto"));
			        ram.setCategoria(rs.getString("categoria"));

		            ram.setIdRam(rs.getInt("id_ram"));
		            ram.setCapacita(rs.getString("capacita"));
		            ram.setFrequenza(rs.getString("frequenza"));
		            ram.setTipo(rs.getString("tipo"));

	                lista.add(ram);
	            }
	        }
	    }

	    return lista;
	}

	public synchronized boolean doUpdate(RAMBean ram) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doUpdate(ram);

	    String sql = "UPDATE "+TABLE_NAME+" SET capacita = ?, frequenza = ?, tipo = ? WHERE id_ram = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, ram.getCapacita());
	        ps.setString(2, ram.getFrequenza());
	        ps.setString(3, ram.getTipo());
	        ps.setInt(4, ram.getIdRam());

	        return ps.executeUpdate() > 0;
	    }
	}

	public synchronized boolean setProductStatus(RAMBean ram, boolean attivo) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

	    return prodottoDAO.setProductStatus(ram.getIdProdotto(), attivo);
	}
}
