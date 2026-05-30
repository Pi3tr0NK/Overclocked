package dao;

import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import java.sql.ResultSet;

import model.DettaglioOrdineBean;
import model.ProdottoBean;

public class DettaglioOrdineDAOImpl implements DettaglioOrdineDAO {
	
	private static final String TABLE_NAME = "dettagliOrdine";
    private DataSource ds = null;
    
    public DettaglioOrdineDAOImpl(DataSource ds) {
        this.ds = ds;
    }
    
	@Override
	public synchronized void doSave(DettaglioOrdineBean d) throws SQLException {
	
        String sql = "INSERT INTO "+TABLE_NAME+" (fk_ordine, fk_prodotto, quantita, prezzo_unitario) VALUES (?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, d.getOrdine().getIdOrdine());
            ps.setInt(2, d.getProdotto().getIdProdotto());
            ps.setInt(3, d.getQuantita());
            ps.setDouble(4, d.getPrezzoUnitario());

            ps.executeUpdate();
        }
	}

	@Override
	public synchronized List<ProdottoBean> doRetrieveByOrdine(int idOrdine) throws SQLException {

	    List<ProdottoBean> lista = new LinkedList<>();

	    String sql = "SELECT p.* FROM "+TABLE_NAME+" d " +
	                 "JOIN prodotto p ON d.fk_prodotto = p.id_prodotto " +
	                 "WHERE p.id_prodotto = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idOrdine);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                ProdottoBean p = new ProdottoBean();
	                p.setIdProdotto(rs.getInt("id_prodotto"));
	                p.setNome(rs.getString("nome"));
	                p.setModello(rs.getString("modello"));
	                p.setDescrizione(rs.getString("descrizione"));
	                p.setMarca(rs.getString("marca"));
	                p.setStock(rs.getInt("stock"));
	                p.setAttivo(rs.getBoolean("attivo"));

	                lista.add(p);
	            }
	        }
	    }

	    return lista;
	}

}
