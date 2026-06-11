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
	public synchronized List<DettaglioOrdineBean> doRetrieveByOrdine(int idOrdine) throws SQLException {

	    List<DettaglioOrdineBean> lista = new LinkedList<>();
	    ImmaginiDAOImpl immaginiDAO = new ImmaginiDAOImpl(ds);
	    
	    String sql = "SELECT d.quantita, d.prezzo_unitario, p.* " +
	                 "FROM dettagliOrdine d " +
	                 "JOIN prodotto p ON d.fk_prodotto = p.id_prodotto " +
	                 "WHERE d.fk_ordine = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idOrdine);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                DettaglioOrdineBean d = new DettaglioOrdineBean();

	                d.setQuantita(rs.getInt("quantita"));
	                d.setPrezzoUnitario(rs.getDouble("prezzo_unitario"));

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
                    
                    d.setProdotto(p);
                    
	                lista.add(d);
	            }
	        }
	    }
	    return lista;
	}

}
