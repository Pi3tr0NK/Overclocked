package dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import model.OrdineBean;
import model.ProdottoBean;
import model.OrdineBean.Stato;

public class OrdineDAOImpl implements OrdineDAO{
	
	
	private static final String TABLE_NAME = "immagini";
    private DataSource ds = null;
    
    public OrdineDAOImpl(DataSource ds) {
        this.ds = ds;
    }
    
    public synchronized void doSave(OrdineBean ordine) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME+ " (data, stato, totale, fattura_path, fk_utente, fk_indirizzo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ordine.getData()));
            ps.setString(2, ordine.getStato().name());
            ps.setDouble(3, ordine.getTotale());
            ps.setString(4, ordine.getFatturaPath());
            ps.setInt(5, ordine.getUtente().getIdUtente());
            ps.setInt(6, ordine.getIndirizzo().getIdIndirizzo());

            ps.executeUpdate();


        }
    }
    
    public synchronized Collection<OrdineBean> doRetrieveAll() throws SQLException {
        List<OrdineBean> lista = new LinkedList<>();
        String sql = "SELECT * FROM "+ TABLE_NAME;

        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);
            
            IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
            UtenteDAOImpl  utenteDAO = new UtenteDAOImpl(ds);
            
            while (rs.next()) {
            	OrdineBean o = new OrdineBean();
                o.setData(rs.getDate("data").toLocalDate());
                o.setStato(Stato.valueOf(rs.getString("stato")));
                o.setTotale(rs.getDouble("totale"));
                o.setFatturaPath(rs.getString("fattura_path"));
                o.setIndirizzo(indirizzoDAO.doRetrieveByKey(rs.getInt("fk_indirizzo")));
                o.setUtente(utenteDAO.doRetrieveByKey(rs.getInt("fk_utente")));

                lista.add(o);
            }
        }
        return lista;
    }
    

	public Collection<OrdineBean> doRetrieveAllByUser(int idUser) throws SQLException {
		
	       List<OrdineBean> lista = new LinkedList<>();
	        String sql = "SELECT * FROM "+ TABLE_NAME + " WHERE fk_utente=?";

	        try (Connection con = ds.getConnection();
	                PreparedStatement ps = con.prepareStatement(sql)) {

	               ps.setInt(1, idUser);
	               
	               ResultSet rs = ps.executeQuery();

	               IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
	               UtenteDAOImpl  utenteDAO = new UtenteDAOImpl(ds);
	               
	               
	               while (rs.next()) {
	            	   OrdineBean o = new OrdineBean();
	            	   o.setData(rs.getDate("data").toLocalDate());
	                   o.setStato(Stato.valueOf(rs.getString("stato")));
	                   o.setTotale(rs.getDouble("totale"));
	                   o.setFatturaPath(rs.getString("fattura_path"));
	                   o.setIndirizzo(indirizzoDAO.doRetrieveByKey(rs.getInt("fk_indirizzo")));
	                   o.setUtente(utenteDAO.doRetrieveByKey(rs.getInt("fk_utente")));
	                
	                   lista.add(o);
	               }
	           }
	           return lista;
	}
	
	
	
    public synchronized boolean doUpdate(OrdineBean o) throws SQLException {
    	
        String sql = "UPDATE " + TABLE_NAME + " SET data=?, stato=?, totale=?, fattura_path=?, fk_utente=?, fk_indirizzo=? WHERE id_ordine=?";

        try(Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
        	
        	ps.setDate(1, Date.valueOf(o.getData()));
        	ps.setString(2, o.getStato().name());
            ps.setDouble(3, o.getTotale());
            ps.setString(4, o.getFatturaPath());
            ps.setInt(5, o.getUtente().getIdUtente());
            ps.setInt(6, o.getIndirizzo().getIdIndirizzo());
            ps.setInt(7, o.getIdOrdine());

            return ps.executeUpdate() > 0;
        }
    	
    	
    }
    

    public synchronized boolean setOrdineStatus(int idOrdine, Stato stato) throws SQLException {
    	
        String sql = "UPDATE "+ TABLE_NAME +" SET stato=? WHERE id_ordine=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, stato.name());
            ps.setInt(2, idOrdine);

            return ps.executeUpdate() >0;
        }    	
    }

}
