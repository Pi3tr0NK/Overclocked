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

public class ChassisDAOImpl implements ChassisDAO{
	private static final String TABLE_NAME = "chassis";

	private DataSource ds;

	public ChassisDAOImpl(DataSource ds) {
	    this.ds = ds;
	}

	public synchronized void doSave(ChassisBean chassis) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doSave(chassis);

	    String sql = "INSERT INTO " + TABLE_NAME + " (formato, colore, materiale, fk_prodotto) VALUES (?, ?, ?, ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, chassis.getFormato());
	        ps.setString(2, chassis.getColore());
	        ps.setString(3, chassis.getMateriale());
	        ps.setInt(4, chassis.getIdProdotto());

	        ps.executeUpdate();
	    }
	}

	public synchronized ChassisBean doRetrieveByKey(int idChassis) throws SQLException {

	    String sql = "SELECT * FROM " + TABLE_NAME + " c JOIN prodotto p ON c.fk_prodotto = p.id_prodotto WHERE c.id_case = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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
	            chassis.setAttivo(rs.getBoolean("attivo"));

	            chassis.setIdCase(rs.getInt("id_case"));
	            chassis.setFormato(rs.getString("formato"));
	            chassis.setColore(rs.getString("colore"));
	            chassis.setMateriale(rs.getString("materiale"));

	            return chassis;
	        }
	    }

	    return null;
	}

	public synchronized Collection<ChassisBean> doRetrieveAll() throws SQLException {

	    List<ChassisBean> lista = new LinkedList<>();

	    String sql = "SELECT * FROM chassis c JOIN prodotto p ON c.fk_prodotto = p.id_prodotto";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            ChassisBean chassis = new ChassisBean();

	            chassis.setIdProdotto(rs.getInt("id_prodotto"));
	            chassis.setNome(rs.getString("nome"));
	            chassis.setModello(rs.getString("modello"));
	            chassis.setDescrizione(rs.getString("descrizione"));
	            chassis.setMarca(rs.getString("marca"));
	            chassis.setPrezzo(rs.getDouble("prezzo"));
	            chassis.setStock(rs.getInt("stock"));
	            chassis.setAttivo(rs.getBoolean("attivo"));

	            chassis.setIdCase(rs.getInt("id_case"));
	            chassis.setFormato(rs.getString("formato"));
	            chassis.setColore(rs.getString("colore"));
	            chassis.setMateriale(rs.getString("materiale"));

	            lista.add(chassis);
	        }
	    }

	    return lista;
	}

	public boolean doUpdate(ChassisBean chassis) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doUpdate(chassis);

	    String sql = "UPDATE chassis SET formato = ?, colore = ?, materiale = ? WHERE id_case = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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
}
