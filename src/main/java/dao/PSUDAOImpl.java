package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.PSUBean;
import model.PSUBean.Formato;
import model.PSUBean.Modulare;

public class PSUDAOImpl implements PSUDAO {
	
	private static final String TABLE_NAME = "psu";
    private DataSource ds;

    public PSUDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    public synchronized void doSave(PSUBean psu) throws SQLException {
        
    	
    	ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
        prodottoDAO.doSave(psu);
        
        
    	String sql = "INSERT INTO "+ TABLE_NAME +" (potenza, certificazione, modulare, formato, fk_prodotto) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, psu.getPotenza());
            ps.setString(2, psu.getCertificazione());
            ps.setString(3, psu.getModulare().name());
            ps.setString(4, psu.getFormato().name());
            ps.setInt(4, psu.getIdProdotto());
            ps.executeUpdate();
        }
    }
    
    public synchronized PSUBean doRetrieveByKey(int idPSU) throws SQLException {

        String sql = "SELECT * FROM "+ TABLE_NAME +" ps JOIN prodotto p ON ps.fk_prodotto = p.id_prodotto WHERE ps.id_psu = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPSU);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                PSUBean psu = new PSUBean();

                psu.setIdProdotto(rs.getInt("id_prodotto"));
                psu.setNome(rs.getString("nome"));
                psu.setModello(rs.getString("modello"));
                psu.setDescrizione(rs.getString("descrizione"));
                psu.setMarca(rs.getString("marca"));
                psu.setPrezzo(rs.getDouble("prezzo"));
                psu.setStock(rs.getInt("stock"));
                psu.setAttivo(rs.getBoolean("attivo"));

                psu.setIdPsu(rs.getInt("id_psu"));
                psu.setPotenza(rs.getInt("potenza"));
                psu.setCertificazione(rs.getString("certificazione"));
                psu.setModulare(Modulare.valueOf(rs.getString("certificazione")));
                psu.setFormato(Formato.valueOf(rs.getString("modulare")));

                return psu;
            }
        }

        return null;
    }
    
    public synchronized Collection<PSUBean> doRetrieveAll() throws SQLException
    {
    	 List<PSUBean> lista = new LinkedList<>();

    	    String sql = "SELECT * FROM "+ TABLE_NAME +" c JOIN prodotto p ON c.fk_prodotto = p.id_prodotto";

    	    try (Connection con = ds.getConnection();
    	    		Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

    	        while (rs.next()) {

                    PSUBean psu = new PSUBean();

                    psu.setIdProdotto(rs.getInt("id_prodotto"));
                    psu.setNome(rs.getString("nome"));
                    psu.setModello(rs.getString("modello"));
                    psu.setDescrizione(rs.getString("descrizione"));
                    psu.setMarca(rs.getString("marca"));
                    psu.setPrezzo(rs.getDouble("prezzo"));
                    psu.setStock(rs.getInt("stock"));
                    psu.setAttivo(rs.getBoolean("attivo"));

                    psu.setIdPsu(rs.getInt("id_psu"));
                    psu.setPotenza(rs.getInt("potenza"));
                    psu.setCertificazione(rs.getString("certificazione"));
                    psu.setModulare(Modulare.valueOf(rs.getString("certificazione")));
                    psu.setFormato(Formato.valueOf(rs.getString("modulare")));

    	            lista.add(psu);
    	        }
    	    }

    	    return lista;
    }
    
    public boolean doUpdate(PSUBean psu) throws SQLException
    {
        ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
        prodottoDAO.doUpdate(psu);
        
        String sql = "UPDATE "+ TABLE_NAME +" SET potenza = ?, certificazione = ?, modulare = ?, formato = ? WHERE id_cpu = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, psu.getPotenza());
            ps.setString(2, psu.getCertificazione());
            ps.setString(3, psu.getModulare().name());
            ps.setString(4, psu.getFormato().name());
            ps.setInt(6, psu.getIdPsu());

            return ps.executeUpdate() > 0;
        }
    }
    
    public synchronized boolean setProductStatus(PSUBean psu, boolean attivo) throws SQLException
    {
    		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

    		return prodottoDAO.setProductStatus(psu.getIdProdotto(), attivo);
    }
}
