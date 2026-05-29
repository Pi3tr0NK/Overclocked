package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.ImmagineBean;
import model.ProdottoBean;


public class ImmaginiDAOImpl implements ImmaginiDAO {
	private static final String TABLE_NAME = "immagini";
    private DataSource ds = null;
    
    public ImmaginiDAOImpl(DataSource ds) {
        this.ds = ds;
    }
	
    public synchronized void doSave(ImmagineBean i, int idProdotto) throws SQLException {
        String sql = "INSERT INTO "+TABLE_NAME+" (path, fk_prodotto) VALUES (?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, i.getPath());
            ps.setInt(2, idProdotto);
            ps.executeUpdate();
        }
    }
    
    public synchronized boolean doDelete(int code) throws SQLException
    {
	    	String sql = "DELETE FROM "+TABLE_NAME+ " WHERE id_immagine = ?";
	
	        try (Connection con = ds.getConnection();
	             PreparedStatement ps = con.prepareStatement(sql)) {
	
	            ps.setInt(1, code);
	            return ps.executeUpdate() > 0;
	        }
    }

	public synchronized List<ImmagineBean> doRetrieveByProdotto(int idProdotto) throws SQLException
	{
	    List<ImmagineBean> lista = new LinkedList<>();
        String sql = "SELECT * FROM "+TABLE_NAME+ " WHERE fk_prodotto=?";

        try (Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

               ps.setInt(1, idProdotto);
               ResultSet rs = ps.executeQuery();
               
               ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
               ProdottoBean prodotto = prodottoDAO.doRetrieveByKey(idProdotto);

            while (rs.next()) {
            		
	            ImmagineBean i = new ImmagineBean();
	            i.setIdImmagine(rs.getInt("id_immagine"));
	            i.setPath(rs.getString("path"));
	            lista.add(i);
            }
        }
        return lista;
	}
	
	public synchronized void updateImage(int idImmagine, String path) throws SQLException
	{
		String sql = "UPDATE "+TABLE_NAME+" SET path= ? WHERE id_immagine= ?";
		
		 try(Connection con = ds.getConnection();
			        PreparedStatement ps = con.prepareStatement(sql)) {

			        ps.setString(1, path);
			        ps.setInt(2, idImmagine);

			        ps.executeUpdate();
			    }
	}
}
