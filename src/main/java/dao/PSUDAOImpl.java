package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        String sql = "SELECT * FROM "+ TABLE_NAME +" ps JOIN prodotto p ON ps.fk_prodotto = p.id_prodotto WHERE p.id_prodotto  = ?";

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
                psu.setDimensioni(rs.getString("dimensioni"));
                psu.setPeso(rs.getString("peso"));
                psu.setAttivo(rs.getBoolean("attivo"));
                psu.setSconto(rs.getInt("sconto"));
		        psu.setCategoria(rs.getString("categoria"));

                psu.setIdPsu(rs.getInt("id_psu"));
                psu.setPotenza(rs.getInt("potenza"));
                psu.setCertificazione(rs.getString("certificazione"));
                psu.setModulare(Modulare.valueOf(rs.getString("modulare")));
                psu.setFormato(Formato.valueOf(rs.getString("formato")));

                return psu;
            }
        }

        return null;
    }
    
    @Override
    public synchronized Collection<PSUBean> doRetrieveAll(String categoria, String prezzo, String marca, String potenza, String certificazione, String modulare) throws SQLException
    {
	    	 List<PSUBean> lista = new LinkedList<>();
	    	 
	    	 categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
	    	 marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
	    	 certificazione = (certificazione == null || certificazione.trim().isEmpty()) ? null : certificazione;
	    	 modulare = (modulare == null || modulare.trim().isEmpty()) ? null : modulare;
	
	    	 Double prezzoMax = null;
	    	 if (prezzo != null && !prezzo.trim().isEmpty()) {
	    	     prezzoMax = Double.parseDouble(prezzo);
	    	 }
	
	    	 Integer potenzaInt = null;
	    	 if (potenza != null && !potenza.trim().isEmpty()) {
	    	     potenzaInt = Integer.parseInt(potenza);
	    	 }

         String sql =
             "SELECT * " +
             "FROM psu ps " +
             "JOIN prodotto p ON ps.fk_prodotto = p.id_prodotto " +
             "WHERE (? IS NULL OR p.categoria = ?) " +
             "AND (? IS NULL OR p.marca = ?) " +
             "AND (? IS NULL OR (p.prezzo * (100 - p.sconto) / 100.0) <= ?) " +
             "AND (? IS NULL OR ps.potenza = ?) " +
             "AND (? IS NULL OR ps.certificazione = ?) " +
             "AND (? IS NULL OR ps.modulare = ?)";

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

             if (potenzaInt == null) {
                 ps.setNull(7, java.sql.Types.INTEGER);
                 ps.setNull(8, java.sql.Types.INTEGER);
             } else {
                 ps.setInt(7, potenzaInt);
                 ps.setInt(8, potenzaInt);
             }

             ps.setString(9, certificazione);
             ps.setString(10, certificazione);

             ps.setString(11, modulare);
             ps.setString(12, modulare);
             try (ResultSet rs = ps.executeQuery()){
    	        		while (rs.next()) {

                    PSUBean psu = new PSUBean();

                    psu.setIdProdotto(rs.getInt("id_prodotto"));
                    psu.setNome(rs.getString("nome"));
                    psu.setModello(rs.getString("modello"));
                    psu.setDescrizione(rs.getString("descrizione"));
                    psu.setMarca(rs.getString("marca"));
                    psu.setPrezzo(rs.getDouble("prezzo"));
                    psu.setStock(rs.getInt("stock"));
                    psu.setDimensioni(rs.getString("dimensioni"));
                    psu.setPeso(rs.getString("peso"));
                    psu.setAttivo(rs.getBoolean("attivo"));
                    psu.setSconto(rs.getInt("sconto"));
    		        	    psu.setCategoria(rs.getString("categoria"));

                    psu.setIdPsu(rs.getInt("id_psu"));
                    psu.setPotenza(rs.getInt("potenza"));
                    psu.setCertificazione(rs.getString("certificazione"));
                    psu.setModulare(Modulare.valueOf(rs.getString("modulare")));
                    psu.setFormato(Formato.valueOf(rs.getString("formato")));

    	            		lista.add(psu);
    	        		}
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
