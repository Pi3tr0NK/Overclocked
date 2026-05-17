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
import model.MemoriaBean.Tecnologia;
import model.MemoriaBean.Tipo;

public class MemoriaDAOImpl implements MemoriaDAO {

	private static final String TABLE_NAME = "chassis";

	private DataSource ds;

	public MemoriaDAOImpl(DataSource ds) {
	    this.ds = ds;
	}

	public synchronized void doSave(MemoriaBean mem) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doSave(mem);

	    String sql = "INSERT INTO " + TABLE_NAME + " (capacita, vel_scrittura, vel_lettura, tipo, tecnologia, formato, fk_prodotto) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, mem.getCapacita());
	        ps.setInt(2, mem.getVelLettura());
	        ps.setInt(3, mem.getVelScrittura());
	        ps.setString(4, mem.getTipo().name());
	        ps.setString(5, mem.getTecnologia().name());
	        ps.setString(6, mem.getFormato());
	        ps.setInt(7, mem.getIdProdotto());
	        
	        ps.executeUpdate();
	    }
	}

	public synchronized MemoriaBean doRetrieveByKey(int idMemoria) throws SQLException {

	    String sql = "SELECT * FROM " + TABLE_NAME + " m JOIN prodotto p ON m.fk_prodotto = p.id_prodotto WHERE m.id_memoria = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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
	        	mem.setAttivo(rs.getBoolean("attivo"));

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

	public synchronized Collection<MemoriaBean> doRetrieveAll() throws SQLException {

	    List<MemoriaBean> lista = new LinkedList<>();

	    String sql = "SELECT * FROM "+TABLE_NAME+" m JOIN prodotto p ON m.fk_prodotto = p.id_prodotto";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	        	MemoriaBean mem = new MemoriaBean();

	        	mem.setIdProdotto(rs.getInt("id_prodotto"));
	        	mem.setNome(rs.getString("nome"));
	        	mem.setModello(rs.getString("modello"));
	        	mem.setDescrizione(rs.getString("descrizione"));
	        	mem.setMarca(rs.getString("marca"));
	        	mem.setPrezzo(rs.getDouble("prezzo"));
	        	mem.setStock(rs.getInt("stock"));
	        	mem.setAttivo(rs.getBoolean("attivo"));

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

	    return lista;
	}

	public boolean doUpdate(MemoriaBean mem) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doUpdate(mem);

	    String sql = "UPDATE "+TABLE_NAME+" SET capacita = ?, vel_scrittura = ?, vel_lettura = ?, tipo = ?, tecnologia = ?, formato = ? WHERE id_case = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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
}
